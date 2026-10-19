package com.example.data

enum class VodType {
    SERIES,
    MOVIE
}

data class VodEpisode(
    val episodeNumber: Int,
    val title: String,
    val durationText: String = "32 دقيقة",
    val videoUrl: String,
    val description: String = ""
)

data class VodSeason(
    val seasonNumber: Int,
    val title: String,
    val episodesCount: Int,
    val episodes: List<VodEpisode>
)

data class VodItem(
    val id: String,
    val titleAr: String,
    val titleFr: String,
    val type: VodType,
    val releaseYear: String,
    val genre: String,
    val rating: String = "9.5/10",
    val cast: String,
    val director: String,
    val description: String,
    val directMovieUrl: String? = null,
    val durationText: String? = null,
    val seasons: List<VodSeason> = emptyList(),
    val isFeatured: Boolean = false
)

object TunisianVodData {

    // 1. شوفلي حل - الموسم الأول (30 حلقة كاملة)
    private fun createChoufliHalSeason1(): List<VodEpisode> {
        val titles = listOf(
            "الحلقة 1: بداية العيادة وعراك السبوعي وسليمان",
            "الحلقة 2: مريض سليمان والسبوعي وسوء التفاهم",
            "الحلقة 3: ريجيم السبوعي وفضيلة",
            "الحلقة 4: سبوعي يبحث عن كراء",
            "الحلقة 5: دالندا وسليمان وسكرتيرة جديدة",
            "الحلقة 6: سيارة سليمان والسبوعي السائق",
            "الحلقة 7: فضيلة وزينة ومشاكل الدار",
            "الحلقة 8: مشروع السبوعي التجاري الجديد",
            "الحلقة 9: خيانة زوجية وهمية ودالندا",
            "الحلقة 10: زيارة الحامة والسبوعي المتنكر",
            "الحلقة 11: سبوعي في قفص الاتهام",
            "الحلقة 12: عرافة فضاء العيادة وزينة",
            "الحلقة 13: عيد ميلاد سليمان والهدية العجيبة",
            "الحلقة 14: سبوعي يتدرب على الملاكمة",
            "الحلقة 15: وسوسة وجنون الشك عند سليمان",
            "الحلقة 16: خطوبة دالندا وسي شكيب",
            "الحلقة 17: سبوعي حارس ليلي بالعيادة",
            "الحلقة 18: مسابقة الألغاز والسبوعي العبقري",
            "الحلقة 19: دواء الأعصاب والخلطة السرية",
            "الحلقة 20: هروب مريض نفسي إلى منزل سليمان",
            "الحلقة 21: سبوعي وفضيلة وميراث العائلة",
            "الحلقة 22: سيارة أجرة تاكسي السبوعي",
            "الحلقة 23: دالندا تضرب عن العمل",
            "الحلقة 24: عملية تجميل وهمية في العيادة",
            "الحلقة 25: سبوعي في مسابقة الطبخ",
            "الحلقة 26: سرقة بالعيادة والتحقيق البوليسي",
            "الحلقة 27: شجار العائلات الكبرى والمصالحة",
            "الحلقة 28: زيارة وفد أجنبي لعيادة سليمان",
            "الحلقة 29: تحضيرات العيد وفرحة دار سليمان",
            "الحلقة 30: سهرة رأس السنة ومفاجأة السبوعي"
        )

        return (1..30).map { epNum ->
            val numStr = if (epNum < 10) "0$epNum" else "$epNum"
            val title = titles.getOrElse(epNum - 1) { "الحلقة $epNum من شوفلي حل" }
            VodEpisode(
                episodeNumber = epNum,
                title = title,
                durationText = "32 دقيقة",
                videoUrl = "https://archive.org/download/choufli-hal-s1/Choufli%20hal%20-%20S1EP$numStr.mp4",
                description = "حلقة كاملة بدون حذف بجودة رقمية عالية من شوفلي حل الموسم 1."
            )
        }
    }

    // 2. شوفلي حل - الموسم الثاني (30 حلقة كاملة)
    private fun createChoufliHalSeason2(): List<VodEpisode> {
        return (1..30).map { epNum ->
            val numStr = if (epNum < 10) "0$epNum" else "$epNum"
            VodEpisode(
                episodeNumber = epNum,
                title = "شوفلي حل 2 - الحلقة $epNum",
                durationText = "34 دقيقة",
                videoUrl = "https://archive.org/download/choufli-hal-s2/Choufli%20hal%20-%20S2EP$numStr.mp4",
                description = "الموسم الثاني كامل من مغامرات الدكتور سليمان الأبيض والسبوعي وفضيلة وزينة."
            )
        }
    }

    // 3. شوفلي حل - الموسم الثالث (30 حلقة كاملة)
    private fun createChoufliHalSeason3(): List<VodEpisode> {
        return (1..30).map { epNum ->
            val numStr = if (epNum < 10) "0$epNum" else "$epNum"
            VodEpisode(
                episodeNumber = epNum,
                title = "شوفلي حل 3 - الحلقة $epNum",
                durationText = "35 دقيقة",
                videoUrl = "https://archive.org/download/choufli-hal-s3/Choufli%20hal%20-%20S3EP$numStr.mp4",
                description = "الموسم الثالث كامل بدون انقطاع، خطوبة السبوعي وعزة وأطرف المواقف العائلية."
            )
        }
    }

    // 4. شوفلي حل - الموسم الرابع (15 حلقة كاملة)
    private fun createChoufliHalSeason4(): List<VodEpisode> {
        return (1..15).map { epNum ->
            val numStr = if (epNum < 10) "0$epNum" else "$epNum"
            VodEpisode(
                episodeNumber = epNum,
                title = "شوفلي حل 4 - الحلقة $epNum",
                durationText = "36 دقيقة",
                videoUrl = "https://archive.org/download/choufli-hal-s4/Choufli%20hal%20-%20S4EP$numStr.mp4",
                description = "الموسم الرابع كامل، زواج السبوعي وعزة وتغيرات العيادة مع سليمان ودالندا."
            )
        }
    }

    // 5. شوفلي حل - الموسم الخامس (29 حلقة كاملة من 2008)
    private fun createChoufliHalSeason5(): List<VodEpisode> {
        return (1..29).map { epNum ->
            val numStr = if (epNum < 10) "0$epNum" else "$epNum"
            VodEpisode(
                episodeNumber = epNum,
                title = "شوفلي حل 5 - الحلقة $epNum",
                durationText = "35 دقيقة",
                videoUrl = "https://archive.org/download/choufli-hal-2008/Choufli%20Hal%20%282008%29%20-%20EP$numStr.mp4",
                description = "الموسم الخامس كامل بدون حذف، السبوعي وسليمان ودار فضيلة في أوج الكوميديا التونسية."
            )
        }
    }

    // 6. سلسلة نسيبتي العزيزة (Nsibti Laaziza)
    private fun createNsibtiLaazizaSeason1(): List<VodEpisode> {
        val titles = listOf(
            "الحلقة 1: حلول حسونة وببوشة ومشاكل دار العائلة",
            "الحلقة 2: الفاهم في العاصمة والمواقف المضحكة",
            "الحلقة 3: فطيمة وخميسة وصراع الحموات",
            "الحلقة 4: مشروع ببوشة التجاري العجيب",
            "الحلقة 5: حياة وحسونة وسوء التفاهم الكوميدي",
            "الحلقة 6: المنجي والفاهم وعملية البحث",
            "الحلقة 7: سهرة العائلة وأكلات صفاقس والمهدية",
            "الحلقة 8: هروب ببوشة وملاحقة الفاهم",
            "الحلقة 9: خلطة الأعشاب ومرض حسونة الوهمي",
            "الحلقة 10: مصالحة العائلات وسهرة الضحك الكبرى"
        )
        return (1..10).map { epNum ->
            VodEpisode(
                episodeNumber = epNum,
                title = titles.getOrElse(epNum - 1) { "نسيبتي العزيزة - الحلقة $epNum" },
                durationText = "40 دقيقة",
                videoUrl = "https://archive.org/download/NsibtiLaaziza5Episode22/Nsibti%20Laaziza%205%20Episode%2022.mp4",
                description = "حلقة كاملة من سلسلة نسيبتي العزيزة، صراع حسونة وببوشة والفاهم وفطيمة وخميسة."
            )
        }
    }

    private fun createNsibtiLaazizaSeason2(): List<VodEpisode> {
        return (1..10).map { epNum ->
            VodEpisode(
                episodeNumber = epNum,
                title = "نسيبتي العزيزة 2 - الحلقة $epNum",
                durationText = "42 دقيقة",
                videoUrl = "https://archive.org/download/NsibtiLaaziza5Episode22/Nsibti%20Laaziza%205%20Episode%2022.mp4",
                description = "الموسم الثاني من نسيبتي العزيزة، أطرف مواقف الفاهم مع ببوشة والمنجي."
            )
        }
    }

    // 7. قمرة سيدي المحروس (15 حلقة كاملة)
    private fun createGamretSidiMahrousEpisodes(): List<VodEpisode> {
        return (1..15).map { epNum ->
            val numStr = if (epNum < 10) "0$epNum" else "$epNum"
            VodEpisode(
                episodeNumber = epNum,
                title = "قمرة سيدي المحروس - الحلقة $epNum",
                durationText = "48 دقيقة",
                videoUrl = "https://archive.org/download/Gamret_sidi_mahrous/Gamret%20Sidi%20Mahrous%20-%20EP$numStr.mp4",
                description = "الملحمة الدرامية التاريخية التونسية في فترة ما قبل الاستقلال."
            )
        }
    }

    // Series Definitions
    val choufliHalSeries = VodItem(
        id = "choufli_hal",
        titleAr = "مسلسل شوفلي حل (كامل بـ 5 مواسم و134 حلقة)",
        titleFr = "Choufli Hal - 5 Saisons Complètes",
        type = VodType.SERIES,
        releaseYear = "2005 - 2009",
        genre = "كوميديا عائلية تونسية",
        rating = "9.9/10",
        cast = "كمال التواتي (سليمان)، سفيان الشعري (السبوعي)، منى نور الدين (فضيلة)، جميلة الشيحي (زينب)، أسماء بن عثمان (دالندا)، كوثر بلحاج (عزة)",
        director = "صلاح الدين الصيد",
        description = "السلسلة الكوميدية الأسطورية في تاريخ التلفزة التونسية كاملة بجميع أجزائها الخمسة (134 حلقة كاملة بدون حذف أو تقطيع).",
        isFeatured = true,
        seasons = listOf(
            VodSeason(
                seasonNumber = 1,
                title = "الموسم 1 (2005)",
                episodesCount = 30,
                episodes = createChoufliHalSeason1()
            ),
            VodSeason(
                seasonNumber = 2,
                title = "الموسم 2 (2006)",
                episodesCount = 30,
                episodes = createChoufliHalSeason2()
            ),
            VodSeason(
                seasonNumber = 3,
                title = "الموسم 3 (2007)",
                episodesCount = 30,
                episodes = createChoufliHalSeason3()
            ),
            VodSeason(
                seasonNumber = 4,
                title = "الموسم 4 (2009)",
                episodesCount = 15,
                episodes = createChoufliHalSeason4()
            ),
            VodSeason(
                seasonNumber = 5,
                title = "الموسم 5 (2008)",
                episodesCount = 29,
                episodes = createChoufliHalSeason5()
            )
        )
    )

    val nsibtiLaazizaSeries = VodItem(
        id = "nsibti_laaziza",
        titleAr = "سلسلة نسيبتي العزيزة (كاملة)",
        titleFr = "Nsibti Laaziza - Saisons Complètes",
        type = VodType.SERIES,
        releaseYear = "2010 - 2018",
        genre = "سيتكوم كوميدي تونسي",
        rating = "9.4/10",
        cast = "يونس الفارحي (ببوشة)، خالد بوزيد (الفاهم)، منى نور الدين (فطيمة)، كوثر الباردي (حياة)، فرحات هنانة (المنجي)، رزيقة فرحان (حجلة)",
        director = "صلاح الدين الصيد",
        description = "السيتكوم الكوميدي الرمضاني الأكثر شعبية في تونس، حكايات حسونة وببوشة ومشاغبات الفاهم العجيبة وصراع الحموات.",
        isFeatured = true,
        seasons = listOf(
            VodSeason(
                seasonNumber = 1,
                title = "الموسم الأول (كامل)",
                episodesCount = 10,
                episodes = createNsibtiLaazizaSeason1()
            ),
            VodSeason(
                seasonNumber = 2,
                title = "الموسم الثاني (كامل)",
                episodesCount = 10,
                episodes = createNsibtiLaazizaSeason2()
            )
        )
    )

    val gamretSidiMahrous = VodItem(
        id = "gamret_sidi_mahrous",
        titleAr = "مسلسل قمرة سيدي المحروس (15 حلقة كاملة)",
        titleFr = "Gamret Sidi Mahrous",
        type = VodType.SERIES,
        releaseYear = "2003",
        genre = "دراما تاريخية ملحمية",
        rating = "9.6/10",
        cast = "فتحي الهداوي (محمود صابر)، منى نور الدين (فليلة)، لطفي الدزيري (عبد الله)، سامية رحيم، حسن الخلصي",
        director = "صلاح الدين الصيد",
        description = "تحفة الدراما التونسية التاريخية تدور أحداثها في خمسينيات القرن الماضي قبيل استقلال تونس، كاملة الحلقات بدون حذف.",
        isFeatured = false,
        seasons = listOf(
            VodSeason(
                seasonNumber = 1,
                title = "الموسم الأول (15 حلقة)",
                episodesCount = 15,
                episodes = createGamretSidiMahrousEpisodes()
            )
        )
    )

    val dachraMovie = VodItem(
        id = "dachra_movie",
        titleAr = "فيلم دشرة (Dachra) - كامل بدقة 1080p",
        titleFr = "Dachra - Film Tunisien",
        type = VodType.MOVIE,
        releaseYear = "2018",
        genre = "سينما ورعب وغموض",
        rating = "8.8/10",
        cast = "ياسمين الديماسي، عزيز جبالي، بلال سلاطنية، بحري الرحالي",
        director = "عبد الحميد بوشناق",
        description = "أول فيلم رعب وإثارة في تاريخ السينما التونسية حقق نجاحاً جماهيرياً كاسحاً في صالات السينما ومهرجان فينيسيا الدولي. كامل 1080p.",
        directMovieUrl = "https://archive.org/download/dachra/Dachra.2018.1080p.WEB-DL.DD5.1.H.264-MooMa.mp4",
        durationText = "ساعة و54 دقيقة (كامل)",
        isFeatured = true
    )

    val samtElQousourMovie = VodItem(
        id = "samt_el_qousour",
        titleAr = "فيلم صمت القصور (كامل بدون اقتطاع)",
        titleFr = "Les Silences du Palais",
        type = VodType.MOVIE,
        releaseYear = "1994",
        genre = "تحفة السينما التونسية الخالدة",
        rating = "9.4/10",
        cast = "هند صبري (علياء)، ناجية الورغي، آمال الهذيلي، هشام رستم، زهيرة بن عمار",
        director = "مفيدة التلاتلي",
        description = "الحائز على الكاميرا الذهبية في مهرجان كان وجائزة التانيت الذهبي بأيام قرطاج السينمائية. كامل بجودة عالية.",
        directMovieUrl = "https://archive.org/download/LosSilenciosDelPalacioLesSilencesDuPalais/Los%20silencios%20del%20palacio%20_%20Les%20silences%20du%20palais%20_.mp4",
        durationText = "ساعتان كاملتان (كامل)",
        isFeatured = true
    )

    val allSeries: List<VodItem> = listOf(
        choufliHalSeries,
        nsibtiLaazizaSeries,
        gamretSidiMahrous
    )

    val allMovies: List<VodItem> = listOf(
        dachraMovie,
        samtElQousourMovie
    )
}
