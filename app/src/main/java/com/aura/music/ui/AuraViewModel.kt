package com.aura.music.ui

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.aura.music.data.*
import com.aura.music.service.AuraPlaybackService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AuraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuraRepository(application)
    private var mediaController: MediaController? = null

    val categories = PreloadCatalog.categories

    private val _selectedCategory = MutableStateFlow(categories.first())
    val selectedCategory: StateFlow<MusicCategory> = _selectedCategory.asStateFlow()

    private val _tracks = MutableStateFlow<List<AuraTrack>>(emptyList())
    val tracks: StateFlow<List<AuraTrack>> = _tracks.asStateFlow()

    private val _currentTrack = MutableStateFlow<AuraTrack?>(null)
    val currentTrack: StateFlow<AuraTrack?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _statusText = MutableStateFlow("🟢 Media3 雙核引擎就緒")
    val statusText: StateFlow<String> = _statusText.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _lyrics = MutableStateFlow<List<LyricLine>>(emptyList())
    val lyrics: StateFlow<List<LyricLine>> = _lyrics.asStateFlow()

    private val _lyricOffsetMs = MutableStateFlow(0L)
    val lyricOffsetMs: StateFlow<Long> = _lyricOffsetMs.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _sdlcResults = MutableStateFlow<List<SdlcTestResult>>(emptyList())
    val sdlcResults: StateFlow<List<SdlcTestResult>> = _sdlcResults.asStateFlow()

    private var playResolveJob: Job? = null
    private var currentQueue: List<AuraTrack> = emptyList()
    private var currentQueueIndex: Int = -1

    init {
        selectCategory(categories.first())
        connectMediaSession()
        startProgressSyncLoop()
        _sdlcResults.value = repository.runSdlcSelfDiagnostics()
    }

    private fun connectMediaSession() {
        val context = getApplication<Application>()
        val sessionToken = SessionToken(context, ComponentName(context, AuraPlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                mediaController = controllerFuture.get()
                mediaController?.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _isPlaying.value = isPlaying
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            playNext()
                        }
                    }
                })
            } catch (_: Exception) {}
        }, MoreExecutors.directExecutor())
    }

    private fun startProgressSyncLoop() {
        viewModelScope.launch {
            while (isActive) {
                val ctrl = mediaController
                if (ctrl != null && ctrl.isPlaying) {
                    _currentPositionMs.value = ctrl.currentPosition.coerceAtLeast(0L)
                    val dur = ctrl.duration
                    if (dur > 0) _durationMs.value = dur
                }
                delay(100L) // 100ms (10fps) 毫秒級歌詞與進度條同步
            }
        }
    }

    fun selectCategory(category: MusicCategory) {
        _selectedCategory.value = category
        val initial = PreloadCatalog.buildInitialTracksForCategory(category.key)
        _tracks.value = initial
        _statusText.value = "🟢 ${category.title} (${initial.size} 首)"

        // 背景非同步補齊該專區前 12 首歌的原曲高清專輯封面
        viewModelScope.launch {
            val updated = initial.toMutableList()
            for (i in 0 until minOf(updated.size, 12)) {
                val item = updated[i]
                if (item.coverUrl.isEmpty() && !item.isRadio) {
                    val cov = repository.resolveTrackCover(item.title, item.artist)
                    if (cov.isNotEmpty()) {
                        updated[i] = item.copy(coverUrl = cov)
                        _tracks.value = updated.toList()
                    }
                }
            }
        }
    }

    fun searchOnline(query: String) {
        if (query.isBlank()) return
        _statusText.value = "🔍 正在全網檢索「$query」..."
        viewModelScope.launch {
            val res = repository.searchCatalogOnline(query.trim())
            if (res.isNotEmpty()) {
                _tracks.value = res
                _statusText.value = "🟢 找到 ${res.size} 首完整全曲"
            } else {
                _statusText.value = "⚠️ 未找到相關曲目"
            }
        }
    }

    fun loadLocalMusic() {
        viewModelScope.launch {
            val local = repository.scanLocalDeviceMusic()
            _tracks.value = local
            _statusText.value = "💻 本機音樂庫 (${local.size} 首)"
        }
    }

    fun playTrack(track: AuraTrack) {
        currentQueue = _tracks.value
        currentQueueIndex = currentQueue.indexOfFirst { it.id == track.id }
        _currentTrack.value = track
        _currentPositionMs.value = 0L
        _durationMs.value = track.durationSec * 1000L
        _lyricOffsetMs.value = 0L
        _statusText.value = "⚡ 正在解析「${track.title}」最佳無損/全曲音源..."

        // 立即停止上一首音軌 (REQ-TRANS-09 零殘音鎖)
        mediaController?.stop()
        mediaController?.clearMediaItems()

        playResolveJob?.cancel()
        playResolveJob = viewModelScope.launch {
            // 同步啟動雙語歌詞檢索
            launch {
                _lyrics.value = listOf(LyricLine(0L, "⚡ 正在對齊雙語時間軸歌詞...", "${track.title} - ${track.artist}"))
                _lyrics.value = repository.fetchSyncedBilingualLyrics(track.title, track.artist, track.durationSec)
            }

            val resolved = repository.resolvePlayableStream(track)
            if (resolved == null) {
                _statusText.value = "⚠️ 無法取得音源，請嘗試切換下一首"
                return@launch
            }

            val updatedTrack = track.copy(
                coverUrl = resolved.coverUrl.ifEmpty { track.coverUrl },
                durationSec = if (resolved.durationSec > 0) resolved.durationSec else track.durationSec
            )
            _currentTrack.value = updatedTrack
            _durationMs.value = updatedTrack.durationSec * 1000L
            _statusText.value = resolved.sourceName

            val metadata = MediaMetadata.Builder()
                .setTitle(updatedTrack.title)
                .setArtist(updatedTrack.artist)
                .setAlbumTitle(updatedTrack.album)
                .apply {
                    if (updatedTrack.coverUrl.isNotEmpty()) {
                        setArtworkUri(Uri.parse(updatedTrack.coverUrl))
                    }
                }
                .build()

            val mediaItem = MediaItem.Builder()
                .setMediaId(updatedTrack.id)
                .setUri(resolved.streamUrl)
                .setMediaMetadata(metadata)
                .build()

            mediaController?.apply {
                setMediaItem(mediaItem)
                prepare()
                play()
            }
        }
    }

    fun togglePlayPause() {
        val ctrl = mediaController ?: return
        if (ctrl.isPlaying) {
            ctrl.pause()
        } else {
            if (ctrl.mediaItemCount == 0 && _tracks.value.isNotEmpty()) {
                playTrack(_tracks.value.first())
            } else {
                ctrl.play()
            }
        }
    }

    fun playNext() {
        if (currentQueue.isEmpty()) currentQueue = _tracks.value
        if (currentQueue.isEmpty()) return
        currentQueueIndex = (currentQueueIndex + 1) % currentQueue.size
        playTrack(currentQueue[currentQueueIndex])
    }

    fun playPrevious() {
        if (currentQueue.isEmpty()) currentQueue = _tracks.value
        if (currentQueue.isEmpty()) return
        currentQueueIndex = (currentQueueIndex - 1 + currentQueue.size) % currentQueue.size
        playTrack(currentQueue[currentQueueIndex])
    }

    fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
        _currentPositionMs.value = positionMs
    }

    fun adjustLyricOffset(deltaMs: Long) {
        _lyricOffsetMs.value += deltaMs
    }

    fun alignNextLyricToNow() {
        val list = _lyrics.value
        if (list.isEmpty()) return
        val now = _currentPositionMs.value
        val nextLine = list.firstOrNull { it.timeMs > now + _lyricOffsetMs.value } ?: list.last()
        _lyricOffsetMs.value = nextLine.timeMs - now
    }

    fun cyclePlaybackSpeed() {
        val speeds = listOf(1.0f, 1.25f, 1.5f, 0.75f)
        val next = speeds[(speeds.indexOf(_playbackSpeed.value) + 1) % speeds.size]
        _playbackSpeed.value = next
        mediaController?.setPlaybackParameters(PlaybackParameters(next))
    }

    fun toggleFavorite(trackId: String) {
        val set = _favorites.value.toMutableSet()
        if (!set.add(trackId)) set.remove(trackId)
        _favorites.value = set
    }

    override fun onCleared() {
        mediaController?.release()
        super.onCleared()
    }
}
