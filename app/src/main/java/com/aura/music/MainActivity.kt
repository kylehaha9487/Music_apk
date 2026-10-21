package com.aura.music

import android.Manifest
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.aura.music.data.AuraTrack
import com.aura.music.ui.AuraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuraMusicTheme {
                AuraMainScreen()
            }
        }
    }
}

@Composable
fun AuraMusicTheme(content: @Composable () -> Unit) {
    val darkScheme = darkColorScheme(
        primary = Color(0xFF1DB954),
        secondary = Color(0xFF3B82F6),
        background = Color(0xFF07080C),
        surface = Color(0xFF10121A),
        onPrimary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )
    MaterialTheme(colorScheme = darkScheme, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraMainScreen(vm: AuraViewModel = viewModel()) {
    val context = LocalContext.current
    val categories by vm.categories.let { remember { mutableStateOf(it) } }
    val selectedCategory by vm.selectedCategory.collectAsState()
    val tracks by vm.tracks.collectAsState()
    val currentTrack by vm.currentTrack.collectAsState()
    val isPlaying by vm.isPlaying.collectAsState()
    val statusText by vm.statusText.collectAsState()
    val positionMs by vm.currentPositionMs.collectAsState()
    val durationMs by vm.durationMs.collectAsState()
    val lyrics by vm.lyrics.collectAsState()
    val lyricOffsetMs by vm.lyricOffsetMs.collectAsState()
    val playbackSpeed by vm.playbackSpeed.collectAsState()
    val favorites by vm.favorites.collectAsState()
    val sdlcResults by vm.sdlcResults.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableIntStateOf(0) } // 0: 曲庫, 1: 雙語歌詞, 2: SDLC 診斷

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        vm.loadLocalMusic()
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10121A))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1DB954))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AURA MUSIC v32.0",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = statusText,
                        fontSize = 11.sp,
                        color = Color(0xFF4DF086),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 頂部膠囊搜尋列 + 本機音樂 / 系統 EQ 快捷鍵
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("搜尋全球歌曲、歌手或專輯...", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        trailingIcon = {
                            IconButton(onClick = { vm.searchOnline(searchQuery) }) {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF1DB954))
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    FilledTonalButton(
                        onClick = {
                            val perms = mutableListOf<String>()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                perms.add(Manifest.permission.READ_MEDIA_AUDIO)
                                perms.add(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                perms.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                            permissionLauncher.launch(perms.toTypedArray())
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text("📂 本機", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    FilledTonalButton(
                        onClick = {
                            try {
                                val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text("🎚️ EQ", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 橫向音樂專區導覽列
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = cat.key == selectedCategory.key
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                activeTab = 0
                                vm.selectCategory(cat)
                            },
                            label = { Text(cat.title, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1DB954),
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0B0D14))
            ) {
                // 迷你歌詞即時字幕列
                val effectiveMs = positionMs + lyricOffsetMs + 180L
                val currentLyric = lyrics.lastOrNull { it.timeMs <= effectiveMs }
                if (currentLyric != null) {
                    Text(
                        text = if (currentLyric.subText.isNotEmpty())
                            "🎤 ${currentLyric.mainText} (${currentLyric.subText})"
                        else "🎤 ${currentLyric.mainText}",
                        color = Color(0xFF4DF086),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeTab = 1 }
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }

                // 進度條
                val progress = if (durationMs > 0) (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
                Slider(
                    value = progress,
                    onValueChange = { ratio ->
                        if (durationMs > 0) vm.seekTo((ratio * durationMs).toLong())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                        .padding(horizontal = 12.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF1DB954),
                        activeTrackColor = Color(0xFF1DB954)
                    )
                )

                // 底部播放控制器
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeTab = if (activeTab == 1) 0 else 1 }
                    ) {
                        AsyncImage(
                            model = currentTrack?.coverUrl?.ifEmpty { "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200" }
                                ?: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200",
                            contentDescription = "Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF222738))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = currentTrack?.title ?: "點選歌曲開始播放完整全曲",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${currentTrack?.artist ?: "支援鎖屏與背景播放"} · ${formatMs(positionMs)} / ${formatMs(durationMs)}",
                                color = Color(0xFF9CA3B8),
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { vm.cyclePlaybackSpeed() }) {
                            Text("${playbackSpeed}x", color = Color(0xFF4DF086), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { vm.playPrevious() }) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = "Prev", tint = Color.White)
                        }
                        FloatingActionButton(
                            onClick = { vm.togglePlayPause() },
                            containerColor = Color(0xFF1DB954),
                            contentColor = Color.Black,
                            shape = CircleShape,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "PlayPause"
                            )
                        }
                        IconButton(onClick = { vm.playNext() }) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
                        }
                    }
                }

                // 分頁切換列 (曲庫 / 雙語歌詞 / SDLC 診斷)
                NavigationBar(
                    containerColor = Color(0xFF10121A),
                    modifier = Modifier.height(54.dp)
                ) {
                    NavigationBarItem(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        icon = { Icon(Icons.Default.LibraryMusic, contentDescription = null) },
                        label = { Text("專區曲庫", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        icon = { Icon(Icons.Default.Lyrics, contentDescription = null) },
                        label = { Text("雙語歌詞", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        icon = { Icon(Icons.Default.VerifiedUser, contentDescription = null) },
                        label = { Text("SDLC 診斷", fontSize = 11.sp) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF182032), Color(0xFF07080C))
                    )
                )
        ) {
            when (activeTab) {
                0 -> TrackListView(
                    tracks = tracks,
                    currentTrack = currentTrack,
                    favorites = favorites,
                    onPlay = { vm.playTrack(it) },
                    onToggleFav = { vm.toggleFavorite(it.id) },
                    onArtistClick = { artist -> vm.searchOnline(artist) }
                )
                1 -> LyricsScreenView(
                    lyrics = lyrics,
                    positionMs = positionMs,
                    lyricOffsetMs = lyricOffsetMs,
                    onSeekTo = { vm.seekTo(it) },
                    onAdjustOffset = { vm.adjustLyricOffset(it) },
                    onAlignNext = { vm.alignNextLyricToNow() }
                )
                2 -> SdlcReportView(results = sdlcResults)
            }
        }
    }
}

@Composable
fun TrackListView(
    tracks: List<AuraTrack>,
    currentTrack: AuraTrack?,
    favorites: Set<String>,
    onPlay: (AuraTrack) -> Unit,
    onToggleFav: (AuraTrack) -> Unit,
    onArtistClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(tracks, key = { _, t -> t.id }) { idx, track ->
            val isCurrent = currentTrack?.id == track.id
            val isLiked = favorites.contains(track.id)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isCurrent) Color(0x331DB954) else Color(0x14FFFFFF))
                    .clickable { onPlay(track) }
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isCurrent) "🔊" else "${idx + 1}",
                    color = if (isCurrent) Color(0xFF1DB954) else Color(0xFF9CA3B8),
                    fontSize = 12.sp,
                    modifier = Modifier.width(28.dp)
                )
                AsyncImage(
                    model = track.coverUrl.ifEmpty { "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200" },
                    contentDescription = track.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF222738))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${track.artist} · ${track.badge}",
                        color = Color(0xFF9CA3B8),
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { onArtistClick(track.artist) }
                    )
                }
                IconButton(onClick = { onToggleFav(track) }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color(0xFFFF4757) else Color(0xFF9CA3B8)
                    )
                }
            }
        }
    }
}

@Composable
fun LyricsScreenView(
    lyrics: List<com.aura.music.data.LyricLine>,
    positionMs: Long,
    lyricOffsetMs: Long,
    onSeekTo: (Long) -> Unit,
    onAdjustOffset: (Long) -> Unit,
    onAlignNext: () -> Unit
) {
    val listState = rememberLazyListState()
    val effectiveMs = positionMs + lyricOffsetMs + 180L
    val activeIdx = remember(lyrics, effectiveMs) {
        lyrics.indexOfLast { it.timeMs <= effectiveMs }.coerceAtLeast(0)
    }

    LaunchedEffect(activeIdx) {
        if (lyrics.isNotEmpty() && activeIdx in lyrics.indices) {
            listState.animateScrollToItem((activeIdx - 2).coerceAtLeast(0))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 歌詞時間軸微調與一鍵對齊工具列 (REQ-SYNC-12)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10121A))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "⏱️ 偏移: ${String.format("%.1f", lyricOffsetMs / 1000.0)}s",
                color = Color(0xFF4DF086),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = { onAdjustOffset(-300L) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) { Text("-0.3s", fontSize = 11.sp) }

                OutlinedButton(
                    onClick = { onAdjustOffset(300L) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) { Text("+0.3s", fontSize = 11.sp) }

                Button(
                    onClick = onAlignNext,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) { Text("🎯 對齊下一句", fontSize = 11.sp) }
            }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(lyrics) { idx, line ->
                val isActive = idx == activeIdx
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isActive) Color(0x281DB954) else Color.Transparent)
                        .clickable { onSeekTo((line.timeMs - lyricOffsetMs).coerceAtLeast(0L)) }
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = line.mainText,
                        color = if (isActive) Color.White else Color(0x77FFFFFF),
                        fontSize = if (isActive) 17.sp else 14.sp,
                        fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal
                    )
                    if (line.subText.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = line.subText,
                            color = if (isActive) Color(0xFF86EFAC) else Color(0x5586EFAC),
                            fontSize = 12.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SdlcReportView(results: List<com.aura.music.data.SdlcTestResult>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "🛡️ AURA MUSIC Android 原生版 // SDLC 工程自檢報告",
                color = Color(0xFF4DF086),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
        items(results) { r ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181B26)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("[${r.reqId}] ${r.name}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(r.detail, color = Color(0xFF9CA3B8), fontSize = 11.5.sp)
                    }
                    Text(
                        text = if (r.passed) "✅ PASS" else "❌ FAIL",
                        color = if (r.passed) Color(0xFF4DF086) else Color(0xFFFF4757),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val totalSec = ms / 1000L
    val m = totalSec / 60L
    val s = totalSec % 60L
    return "$m:${if (s < 10L) "0$s" else "$s"}"
}
