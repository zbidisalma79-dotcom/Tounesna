package com.example.data

enum class ChannelCategory(val id: String, val titleAr: String, val iconName: String) {
    ALL("all", "الكل", "LiveTv"),
    NATIONAL("national", "الوطنية والأخبار", "Public"),
    VARIETY("variety", "منوعات ودراما", "TheaterComedy"),
    RADIO_VISUAL("radio_visual", "إذاعات مرئية ومباشرة", "Radio"),
    CULTURE("culture", "ثقافة وشباب", "AutoStories"),
    RELIGIOUS("religious", "دينية واجتماعية", "Mosque")
}

data class StreamServer(
    val id: String,
    val name: String,
    val url: String,
    val quality: String = "HD 1080p",
    val description: String = "بث مباشر أصلي"
)

data class TvChannel(
    val id: String,
    val nameAr: String,
    val nameFr: String,
    val category: ChannelCategory,
    val logoUrl: String? = null,
    val accentColorHex: Long = 0xFFE70013,
    val primaryServer: StreamServer,
    val backupServers: List<StreamServer> = emptyList(),
    val currentProgram: String,
    val currentProgramTime: String = "يعرض الآن",
    val nextProgram: String = "نشرة الأخبار المسائية",
    val description: String,
    val badge: String = "HD",
    val frequencyNilesat: String = "11658 V 27500",
    val frequencyBadr: String? = "12643 H 27500",
    val isFeatured: Boolean = false
) {
    val allServers: List<StreamServer>
        get() = listOf(primaryServer) + backupServers
}

data class ProgramItem(
    val id: String,
    val channelId: String,
    val channelName: String,
    val title: String,
    val timeSlot: String,
    val presenterOrActors: String,
    val genre: String,
    val isCurrentlyAiring: Boolean = false
)

data class SatelliteFrequency(
    val satellite: String,
    val frequency: String,
    val polarization: String,
    val symbolRate: String,
    val fec: String,
    val coverage: String,
    val channels: List<String>
)
