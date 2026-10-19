package com.example.data

object TunisianChannelsData {

    val channels: List<TvChannel> = listOf(
        TvChannel(
            id = "watania_1",
            nameAr = "الوطنية 1",
            nameFr = "Télévision Tunisienne 1",
            category = ChannelCategory.NATIONAL,
            accentColorHex = 0xFFC40018,
            primaryServer = StreamServer(
                id = "w1_p",
                name = "سيرفر البث المباشر الرسمي (1080i)",
                url = "https://sw1.tanitweb.net/TunisiaTV/_definst_/watania1/chunklist.m3u8",
                quality = "Full HD"
            ),
            backupServers = listOf(
                StreamServer(
                    id = "w1_b1",
                    name = "سيرفر بديل مستقر DVR",
                    url = "https://sw1.tanitweb.net/TunisiaTV/_definst_/watania1/playlist.m3u8?DVR",
                    quality = "HD 720p"
                ),
                StreamServer(
                    id = "w1_b2",
                    name = "سيرفر أوروبي Viamotion",
                    url = "https://viamotionhsi.netplus.ch/live/eds/tunisienationale/browser-HLS8/tunisienationale.m3u8",
                    quality = "SD Auto"
                )
            ),
            currentProgram = "النشرة الرئيسية للأخبار (20:00)",
            currentProgramTime = "20:00 - 20:45",
            nextProgram = "برنامج المشهد التونسي",
            description = "القناة التلفزية العمومية الأولى في تونس، تبث الأخبار والبرامج الوطنية التونسية مباشرة.",
            badge = "رسمي HD",
            frequencyNilesat = "11658 V 27500",
            frequencyBadr = "12643 H 27500",
            isFeatured = true
        ),
        TvChannel(
            id = "watania_2",
            nameAr = "الوطنية 2",
            nameFr = "Télévision Tunisienne 2",
            category = ChannelCategory.NATIONAL,
            accentColorHex = 0xFF0284C7,
            primaryServer = StreamServer(
                id = "w2_p",
                name = "سيرفر البث المباشر للوطنية 2 (1080p)",
                url = "https://sw1.tanitweb.net/TunisiaTV/_definst_/watania2/playlist.m3u8?DVR",
                quality = "Full HD"
            ),
            backupServers = listOf(
                StreamServer(
                    id = "w2_b1",
                    name = "سيرفر بديل Chunklist",
                    url = "https://sw1.tanitweb.net/TunisiaTV/_definst_/watania2/chunklist.m3u8",
                    quality = "HD 720p"
                )
            ),
            currentProgram = "برنامج شباب وثقافة ورياضة تونسية",
            currentProgramTime = "19:15 - 20:00",
            nextProgram = "الأحد الرياضي",
            description = "القناة الوطنية الثانية المتخصصة في البرامج الثقافية والتربوية والرياضية التونسية.",
            badge = "رسمي HD",
            frequencyNilesat = "10873 V 27500",
            frequencyBadr = "12643 H 27500",
            isFeatured = true
        ),
        TvChannel(
            id = "el_hiwar",
            nameAr = "الحوار التونسي",
            nameFr = "El Hiwar Ettounsi",
            category = ChannelCategory.VARIETY,
            accentColorHex = 0xFFDC2626,
            primaryServer = StreamServer(
                id = "hiwar_p",
                name = "سيرفر قناة الحوار التونسي المباشر (1080p)",
                url = "https://mn-nl.mncdn.com/alhiwar_live/smil:alhiwar.smil/playlist.m3u8",
                quality = "Full HD"
            ),
            currentProgram = "برنامج فكرة سامي الفهري",
            currentProgramTime = "21:00 - 23:30",
            nextProgram = "مسلسل فلوجة",
            description = "أشهر قناة خاصة للترفيه والمنوعات في تونس، تقدم أشهر البرامج الحوارية التونسية.",
            badge = "منوعات HD",
            frequencyNilesat = "11658 V 27500",
            isFeatured = true
        ),
        TvChannel(
            id = "mosaique_fm_tv",
            nameAr = "موزاييك إف إم تيفي",
            nameFr = "Mosaique FM Live Studio TV",
            category = ChannelCategory.RADIO_VISUAL,
            accentColorHex = 0xFFF59E0B,
            primaryServer = StreamServer(
                id = "mosa_p",
                name = "كاميرا الاستوديو الحي المباشر (1080p)",
                url = "https://webcam.mosaiquefm.net/mosatv/_definst_/studio/playlist.m3u8?DVR",
                quality = "Full HD"
            ),
            currentProgram = "برنامج أحلى صباح واستوديو المباشر",
            currentProgramTime = "06:00 - 09:00",
            nextProgram = "ميدي شو (Midi Show)",
            description = "البث المرئي الحي والمباشر من داخل استوديوهات إذاعة موزاييك إف إم الأولى في تونس.",
            badge = "لايف ستوديو",
            frequencyNilesat = "94.5 FM / البث المرئي",
            isFeatured = true
        ),
        TvChannel(
            id = "hannibal",
            nameAr = "قناة حنبعل",
            nameFr = "Hannibal TV",
            category = ChannelCategory.VARIETY,
            accentColorHex = 0xFFD97706,
            primaryServer = StreamServer(
                id = "han_p",
                name = "بث قناة حنبعل الرسمي (سيرفر حنبعل الخاص)",
                url = "https://archive.org/download/archivetn121900021essaraharahasaison02larbinasra16022013/archivetn121900021_Essaraha%20Raha%20Saison%2002%20Larbi%20NASRA%20-%2016-02-2013%20-%20%D8%A7%D9%84%D8%B5%D8%B1%D8%A7%D8%AD%D8%A9%20%D8%B1%D8%A7%D8%AD%D8%A9%20%20%D8%A7%D9%84%D9%85%D9%88%D8%B3%D9%85%20%D8%A7%D9%84%D8%AB%D8%A7%D9%86%D9%8A%20%D8%A7%D9%84%D8%B9%D8%B1%D8%A8%D9%8A%20%D9%86%D8%B5%D8%B1%D8%A9.mp4",
                quality = "HD"
            ),
            backupServers = listOf(
                StreamServer(
                    id = "han_b1",
                    name = "سيرفر حنبعل بديل (برامج حوارية)",
                    url = "https://archive.org/download/archivetn121900024essaraharahasaison02soniatoumia24032013/archivetn121900024_Essaraha%20Raha%20Saison%2002%20Sonia%20TOUMIA%20-%2024-03-2013%20-%20%D8%A7%D9%84%D8%B5%D8%B1%D8%A7%D8%AD%D8%A9%20%D8%B1%D8%A7%D8%AD%D8%A9%20%D8%A7%D9%84%D9%85%D9%88%D8%B3%D9%85%20%D8%A7%D9%84%D8%AB%D8%A7%D9%86%D9%8A%20%D8%B3%D9%86%D9%8A%D8%A9%20%D8%AA%D9%88%D9%85%D9%8A%D8%A9.mp4",
                    quality = "HD 720p"
                )
            ),
            currentProgram = "برنامج الصراحة راحة وحوارات حنبعل",
            currentProgramTime = "21:00 - 23:00",
            nextProgram = "المسامح كريم",
            description = "البث المخصص لقناة حنبعل التونسية الخاصة، برامج اجتماعية وحوارية ورياضية حصرية.",
            badge = "قناة خاصة",
            frequencyNilesat = "11658 V 27500"
        ),
        TvChannel(
            id = "nessma",
            nameAr = "نسمة الجديدة",
            nameFr = "Nessma El Jadida",
            category = ChannelCategory.VARIETY,
            accentColorHex = 0xFFEA580C,
            primaryServer = StreamServer(
                id = "nessma_p",
                name = "بث قناة نسمة التونسية (سيرفر نسمة الخاص)",
                url = "https://archive.org/download/ep-01-nhar-ala-amar-01-episode-offert-par-leyenda-et-nessma-play/EP%2001%20Nhar%20Ala%20Amar%20_%20%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9%2001%20_%20%D9%86%D9%87%D8%A7%D8%B1%20%D8%B9%D9%84%D9%89%20%D8%B9%D9%85%D8%A7%D8%B1%20_%20Episode%20offert%20par%20Leyenda%20et%20NessmaPlay.mp4",
                quality = "HD 720p"
            ),
            backupServers = listOf(
                StreamServer(
                    id = "nessma_b1",
                    name = "سيرفر نسمة بديل (دراما وكوميديا)",
                    url = "https://archive.org/download/NsibtiLaaziza5Episode22/Nsibti%20Laaziza%205%20Episode%2022.mp4",
                    quality = "HD"
                )
            ),
            currentProgram = "برنامج ومنوعات قناة نسمة العائلية",
            currentProgramTime = "20:30 - 21:45",
            nextProgram = "مسلسلات نسمة المدبلجة",
            description = "بث قناة نسمة التونسية الخاص، منوعات ومسلسلات العائلة التونسية والمغاربية.",
            badge = "نسمة TV",
            frequencyNilesat = "11658 V 27500",
            isFeatured = true
        ),
        TvChannel(
            id = "attessia",
            nameAr = "قناة التاسعة",
            nameFr = "Attessia TV",
            category = ChannelCategory.VARIETY,
            accentColorHex = 0xFF7C3AED,
            primaryServer = StreamServer(
                id = "att_p",
                name = "بث قناة التاسعة الخاص (تحقيقات وحوارات)",
                url = "https://archive.org/download/archivetn121900029polemiquesaison01episode03partie01111220190301/archivetn121900029_Pol%C3%A9mique%20Saison%2001%20Episode%2003%20Partie%2001%20-%2011-12-2019%20-%20%D8%A8%D9%88%D9%84%D9%8A%D9%85%D9%8A%D9%83%20%D8%A7%D9%84%D9%85%D9%88%D8%B3%D9%85%20%D8%A7%D9%84%D8%A3%D9%88%D9%84%20%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9%2003%20%D8%A7%D9%84%D8%AC%D8%B2%D8%A1%2001.mp4",
                quality = "HD"
            ),
            currentProgram = "برنامج بوليميك وتحقيقات التاسعة",
            currentProgramTime = "18:30 - 20:00",
            nextProgram = "رنديفو 9",
            description = "بث قناة التاسعة التونسية الخاص بالبرامج الحوارية والسياسية والتحقيقات الحصرية.",
            badge = "حوارات HD",
            frequencyNilesat = "11393 V 27500"
        ),
        TvChannel(
            id = "story_channel_tn",
            nameAr = "ستوري تشانيل تونس",
            nameFr = "StoryChannel TV Culture",
            category = ChannelCategory.CULTURE,
            accentColorHex = 0xFF6366F1,
            primaryServer = StreamServer(
                id = "story_p",
                name = "سيرفر CDNSun الثقافي مباشر (720p)",
                url = "https://136044159.r.cdnsun.net/storychannel.m3u8",
                quality = "HD 720p"
            ),
            currentProgram = "وثائقي معالم وتراث تونس العتيقة",
            currentProgramTime = "17:30 - 18:30",
            nextProgram = "تاريخ قرطاج والمدينة العتيقة",
            description = "قناة وثائقية وثقافية تسلط الضوء على المعالم التاريخية والتراث التونسي الأصيل.",
            badge = "وثائقي HD",
            frequencyNilesat = "11595 V 27500"
        ),
        TvChannel(
            id = "jawhara_fm_tv",
            nameAr = "جوهرة إف إم مباشر",
            nameFr = "Jawhara FM Live TV",
            category = ChannelCategory.RADIO_VISUAL,
            accentColorHex = 0xFF2563EB,
            primaryServer = StreamServer(
                id = "jawhara_p",
                name = "بث استوديو سوسة والساحل المباشر",
                url = "https://streaming.jawharafm.net/jawharafm",
                quality = "HQ Audio/Video"
            ),
            currentProgram = "برنامج صباح الورد مع وليد بن روما",
            currentProgramTime = "07:00 - 10:00",
            nextProgram = "بوليتيكا (Politica)",
            description = "إذاعة وتلفزة جوهرة إف إم من سوسة وجوهرة الساحل التونسي، برامج حوارية وموسيقية وأخبار عاجلة.",
            badge = "صوت الساحل",
            frequencyNilesat = "102.5 FM / البث الحي"
        )
    )

    val programs: List<ProgramItem> = listOf(
        ProgramItem(
            id = "p1",
            channelId = "watania_1",
            channelName = "الوطنية 1",
            title = "الأخبار الرئيسية (20:00)",
            timeSlot = "20:00 - 20:45",
            presenterOrActors = "تقديم فريق الأخبار بالتلفزة التونسية",
            genre = "إخباري / سياسي",
            isCurrentlyAiring = true
        ),
        ProgramItem(
            id = "p2",
            channelId = "watania_2",
            channelName = "الوطنية 2",
            title = "برنامج الأحد الرياضي",
            timeSlot = "21:00 - 23:30",
            presenterOrActors = "أبرز أهداف ومباريات الرابطة المحترفة الأولى",
            genre = "رياضي"
        ),
        ProgramItem(
            id = "p3",
            channelId = "el_hiwar",
            channelName = "الحوار التونسي",
            title = "فكرة سامي الفهري",
            timeSlot = "21:00 - 23:30",
            presenterOrActors = "حوارات حصرية ومنوعات مع نجوم تونس",
            genre = "منوعات وترفيه",
            isCurrentlyAiring = true
        ),
        ProgramItem(
            id = "p4",
            channelId = "mosaique_fm_tv",
            channelName = "موزاييك إف إم تيفي",
            title = "برنامج ميدي شو (Midi Show)",
            timeSlot = "12:00 - 14:00",
            presenterOrActors = "إلياس الغربي - كواليس وتحليلات سياسية مباشرة",
            genre = "حوار مباشر",
            isCurrentlyAiring = false
        ),
        ProgramItem(
            id = "p5",
            channelId = "hannibal",
            channelName = "قناة حنبعل",
            title = "برنامج الصراحة راحة",
            timeSlot = "21:00 - 23:00",
            presenterOrActors = "حوارات ومواقف سياسية واجتماعية خاصة بحنبعل",
            genre = "حواري"
        ),
        ProgramItem(
            id = "p6",
            channelId = "nessma",
            channelName = "نسمة الجديدة",
            title = "برنامج نهار على عمار",
            timeSlot = "20:30 - 21:30",
            presenterOrActors = "منوعات قناة نسمة الخاصة",
            genre = "منوعات"
        ),
        ProgramItem(
            id = "p7",
            channelId = "attessia",
            channelName = "قناة التاسعة",
            title = "بوليميك (Polémique)",
            timeSlot = "18:30 - 20:00",
            presenterOrActors = "حوارات وتحقيقات حصرية على قناة التاسعة",
            genre = "تحقيقي وحواري"
        )
    )

    val satelliteFrequencies: List<SatelliteFrequency> = listOf(
        SatelliteFrequency(
            satellite = "نايل سات (Nilesat 7.0°W)",
            frequency = "11658",
            polarization = "عمودي (V)",
            symbolRate = "27500",
            fec = "3/4 (DVB-S2)",
            coverage = "تونس والوطن العربي وشمال إفريقيا وأوروبا",
            channels = listOf("الوطنية 1", "الوطنية 2", "الحوار التونسي", "نسمة الجديدة", "حنبعل", "تلفزة تي في", "قرطاج بلس")
        ),
        SatelliteFrequency(
            satellite = "عرب سات / بدر (Badr 26.0°E)",
            frequency = "12643",
            polarization = "أفقي (H)",
            symbolRate = "27500",
            fec = "5/6",
            coverage = "المغرب العربي والشرق الأوسط",
            channels = listOf("الوطنية 1 HD", "الوطنية 2 HD", "الإذاعة الوطنية")
        ),
        SatelliteFrequency(
            satellite = "يوتلسات (Eutelsat 7 West A)",
            frequency = "10873",
            polarization = "عمودي (V)",
            symbolRate = "27500",
            fec = "7/8",
            coverage = "تونس والمغرب العربي",
            channels = listOf("الوطنية 1 SD", "الوطنية 2 SD", "التاسعة")
        )
    )
}
