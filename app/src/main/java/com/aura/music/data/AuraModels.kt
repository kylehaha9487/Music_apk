package com.aura.music.data

data class AuraTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSec: Int = 215,
    val coverUrl: String = "",
    val directStreamUrl: String = "",
    val isRadio: Boolean = false,
    val badge: String = "🔥 完整全曲"
)

data class LyricLine(
    val timeMs: Long,
    val mainText: String,
    val subText: String = ""
)

data class ResolvedAudioStream(
    val streamUrl: String,
    val mimeType: String,
    val sourceName: String,
    val durationSec: Int,
    val coverUrl: String,
    val headers: Map<String, String> = emptyMap()
)

data class MusicCategory(
    val key: String,
    val title: String,
    val badge: String,
    val defaultQuery: String,
    val seeds: List<Pair<String, List<String>>>
)

data class SdlcTestResult(
    val reqId: String,
    val name: String,
    val passed: Boolean,
    val detail: String
)
