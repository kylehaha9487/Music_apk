package com.aura.music.data

object PreloadCatalog {

    val categories: List<MusicCategory> = listOf(
        MusicCategory(
            key = "new_releases_today",
            title = "🆕 全球熱播榜",
            badge = "🆕 全球熱播",
            defaultQuery = "2026 Top Hits Lady Gaga Bruno Mars Billie Eilish",
            seeds = listOf(
                "Lady Gaga & Bruno Mars" to listOf("Die With A Smile", "Disease", "Abracadabra"),
                "ROSÉ & Bruno Mars" to listOf("APT.", "number one girl", "toxic till the end"),
                "Billie Eilish" to listOf("BIRDS OF A FEATHER", "LUNCH", "CHIHIRO", "WILDFLOWER"),
                "Sabrina Carpenter" to listOf("Espresso", "Please Please Please", "Taste", "Bed Chem"),
                "aespa" to listOf("Supernova", "Whiplash", "Armageddon", "UP"),
                "NewJeans" to listOf("How Sweet", "Bubble Gum", "Supernatural", "Ditto", "Hype Boy"),
                "Creepy Nuts" to listOf("Bling-Bang-Bang-Born", "Otonoke"),
                "Mrs. GREEN APPLE" to listOf("ライラック", "ケセラセラ", "Soranji", "ダンスホール"),
                "YOASOBI" to listOf("アイドル", "夜に駆ける", "群青", "UNDEAD", "勇者"),
                "告五人" to listOf("又到天黑", "披星戴月的想你", "愛人錯過", "唯一", "好不容易"),
                "周杰倫" to listOf("最偉大的作品", "晴天", "七里香", "告白氣球", "夜曲")
            )
        ),
        MusicCategory(
            key = "mandopop",
            title = "🏮 華語流行金曲",
            badge = "🏮 華語流行",
            defaultQuery = "華語流行金曲 周杰倫 林俊傑 鄧紫棋 陳奕迅",
            seeds = listOf(
                "周杰倫" to listOf("晴天", "告白氣球", "七里香", "一路向北", "夜曲", "稻香", "青花瓷", "擱淺", "簡單愛", "不能說的秘密"),
                "林俊傑" to listOf("修煉愛情", "不為誰而作的歌", "江南", "小酒窩", "可惜沒如果", "她說", "交換餘生"),
                "鄧紫棋" to listOf("光年之外", "泡沫", "來自天堂的魔鬼", "倒數", "句號", "多遠都要在一起"),
                "陳奕迅" to listOf("孤勇者", "十年", "浮誇", "富士山下", "愛情轉移", "紅玫瑰", "好久不見"),
                "蔡依林" to listOf("玫瑰少年", "怪美的", "倒帶", "日不落", "說愛你", "親愛的對象"),
                "五月天" to listOf("突然好想你", "倔強", "知足", "溫柔", "我不願讓你一個人", "後來的我們", "乾杯"),
                "韋禮安" to listOf("如果可以", "女孩", "還是會", "慢慢等"),
                "周興哲" to listOf("以後別做朋友", "你，好不好？", "怎麼了", "永不失聯的愛", "摯友"),
                "田馥甄" to listOf("小幸運", "寂寞寂寞就好", "魔鬼中的天使", "無人知曉")
            )
        ),
        MusicCategory(
            key = "tw_indie",
            title = "🎸 台灣獨立樂團",
            badge = "🎸 台灣樂團",
            defaultQuery = "台灣獨立樂團 告五人 草東沒有派對 茄子蛋 落日飛車",
            seeds = listOf(
                "告五人" to listOf("披星戴月的想你", "愛人錯過", "在這座城市遺失了你", "唯一", "好不容易", "帶我去找夜生活"),
                "草東沒有派對" to listOf("大風吹", "山海", "爛泥", "鬼", "勇敢的人", "但"),
                "茄子蛋" to listOf("浪子回頭", "浪流連", "愛情你比我想的閣較偉大", "親愛的無情孫小美"),
                "落日飛車" to listOf("My Jinji", "Burgundy Red", "Slow", "Greedy", "Candlelight"),
                "理想混蛋" to listOf("不是因為天氣晴朗才愛你", "愚者", "行星", "離開的一路上"),
                "滅火器" to listOf("島嶼天光", "長途夜車", "海上的人", "晚安台灣"),
                "老王樂隊" to listOf("我還年輕 我還年輕", "安九", "他們在鐵皮屋頂上奔跑"),
                "美秀集團" to listOf("捲菸", "擋一根", "電火王", "我要你愛", "心悶"),
                "deca joins" to listOf("浴室", "海浪", "巫賭", "夜間獨白")
            )
        ),
        MusicCategory(
            key = "jpop_top100",
            title = "🏆 J-Pop 排行榜",
            badge = "🏆 J-Pop 榜",
            defaultQuery = "J-Pop ランキング Mrs. GREEN APPLE YOASOBI Vaundy 藤井風",
            seeds = listOf(
                "Mrs. GREEN APPLE" to listOf("ライラック", "ケセラセラ", "Soranji", "ダンスホール", "青と夏", "点描の唄"),
                "Creepy Nuts" to listOf("オトノケ", "Bling-Bang-Bang-Born", "二度寝", "のびしろ"),
                "YOASOBI" to listOf("アイドル", "夜に駆ける", "群青", "怪物", "勇者", "祝福"),
                "tuki." to listOf("晩餐歌", "地獄恋文", "サクラキミワタシ"),
                "Vaundy" to listOf("怪獣の花唄", "タイムパラドックス", "踊り子", "不可幸力"),
                "藤井風" to listOf("満ちてゆく", "死ぬのがいいわ", "きらり", "花", "まつり"),
                "米津玄師" to listOf("Lemon", "KICK BACK", "さよーならまたいつか！", "アイネクライネ", "感電"),
                "Official髭男dism" to listOf("Pretender", "Subtitle", "ミックスナッツ", "I LOVE..."),
                "Ado" to listOf("唱", "新時代", "うっせぇわ", "逆光", "私は最強")
            )
        ),
        MusicCategory(
            key = "kpop",
            title = "✨ K-Pop 韓流榜",
            badge = "✨ K-Pop",
            defaultQuery = "K-Pop Hits NewJeans aespa BLACKPINK IVE BTS",
            seeds = listOf(
                "NewJeans" to listOf("Ditto", "Hype Boy", "Super Shy", "OMG", "Attention", "ETA"),
                "aespa" to listOf("Supernova", "Whiplash", "Armageddon", "Drama", "Spicy", "Next Level"),
                "BLACKPINK" to listOf("Shut Down", "Pink Venom", "How You Like That", "DDU-DU DDU-DU", "Lovesick Girls"),
                "IVE" to listOf("I AM", "LOVE DIVE", "After LIKE", "ELEVEN", "Baddie", "HEYA"),
                "BTS" to listOf("Dynamite", "Butter", "Boy With Luv", "Spring Day", "DNA", "FAKE LOVE"),
                "LE SSERAFIM" to listOf("Perfect Night", "ANTIFRAGILE", "CRAZY", "EASY", "Smart"),
                "IU" to listOf("Love wins all", "LILAC", "Blueming", "Eight", "Celebrity", "Through the Night"),
                "DAY6" to listOf("You Were Beautiful", "Time of Our Life", "Welcome to the Show", "HAPPY")
            )
        ),
        MusicCategory(
            key = "anime_ost",
            title = "🎬 動漫神曲 OST",
            badge = "🎬 動漫 OST",
            defaultQuery = "Anime OP ED LiSA Aimer 結束バンド Eve 羊文学",
            seeds = listOf(
                "LiSA" to listOf("紅蓮華", "炎", "crossing field", "Rising Hope", "明け星"),
                "Aimer" to listOf("残響散歌", "Brave Shine", "カタオモイ", "Ref:rain", "LAST STARDUST"),
                "結束バンド" to listOf("青春コンプレックス", "星座になれたら", "ギターと孤独と蒼い惑星", "あのバンド"),
                "Eve" to listOf("廻廻奇譚", "ファイトソング", "闇夜", "蒼のワルツ"),
                "キタニタツヤ" to listOf("青のすみか", "ファタール", "スカー"),
                "羊文学" to listOf("more than words", "光るとき", "Burning"),
                "TK from 凛として時雨" to listOf("unravel", "first death", "katharsis")
            )
        ),
        MusicCategory(
            key = "citypop",
            title = "🌆 東京 City Pop",
            badge = "🌆 City Pop",
            defaultQuery = "Japanese City Pop 竹内まりや 松原みき 杏里 山下達郎",
            seeds = listOf(
                "竹内まりや" to listOf("Plastic Love", "September", "夢の続き", "元気を出して"),
                "松原みき" to listOf("真夜中のドア / Stay With Me", "愛はエネルギー", "Jazzy Night"),
                "杏里 Anri" to listOf("悲しみがとまらない", "Remember Summer Days", "Last Summer Whisper", "CAT'S EYE"),
                "山下達郎" to listOf("RIDE ON TIME", "SPARKLE", "クリスマス・イブ", "MORNING GLORY"),
                "大貫妙子" to listOf("4:00A.M.", "都会", "サマー・コネクション"),
                "泰葉 Yasuha" to listOf("Flyday Chinatown")
            )
        ),
        MusicCategory(
            key = "western",
            title = "🌎 歐美 Billboard",
            badge = "🌎 歐美流行",
            defaultQuery = "Billboard Hot 100 Taylor Swift The Weeknd Bruno Mars",
            seeds = listOf(
                "Taylor Swift" to listOf("Cruel Summer", "Anti-Hero", "Blank Space", "Love Story", "Fortnight"),
                "Bruno Mars" to listOf("Just the Way You Are", "Uptown Funk", "That's What I Like", "24K Magic", "Locked Out of Heaven"),
                "The Weeknd" to listOf("Blinding Lights", "Starboy", "Save Your Tears", "Die For You"),
                "Dua Lipa" to listOf("Levitating", "Don't Start Now", "Houdini", "New Rules"),
                "Ed Sheeran" to listOf("Shape of You", "Perfect", "Thinking Out Loud", "Bad Habits"),
                "Coldplay" to listOf("Viva La Vida", "Yellow", "The Scientist", "Fix You", "A Sky Full of Stars")
            )
        ),
        MusicCategory(
            key = "vocal_full",
            title = "🎤 獨立人聲全曲",
            badge = "🎤 開放全曲",
            defaultQuery = "Indie Pop Acoustic Vocal Jamendo Audius",
            seeds = listOf(
                "Cigarettes After Sex" to listOf("Apocalypse", "Sunsetz", "K.", "Sweet"),
                "Lana Del Rey" to listOf("Summertime Sadness", "Young and Beautiful", "Born to Die"),
                "keshi" to listOf("LIMBO", "UNDERSTAND", "2 soon", "like i need u"),
                "Lauv" to listOf("I Like Me Better", "Paris in the Rain", "Steal The Show"),
                "HONNE" to listOf("Day 1", "Location Unknown", "Warm on a Cold Night")
            )
        ),
        MusicCategory(
            key = "live_radio",
            title = "📻 24H 全球音樂電台",
            badge = "📻 24H 電台",
            defaultQuery = "Live Radio",
            seeds = emptyList()
        )
    )

    val curatedRadios: List<AuraTrack> = listOf(
        AuraTrack(
            id = "rad_1",
            title = "Nightwave Plaza (東京 City Pop 24H 電台)",
            artist = "Plaza.one Tokyo Stream",
            album = "City Pop / Future Funk",
            durationSec = 0,
            coverUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=400",
            directStreamUrl = "https://radio.plaza.one/mp3",
            isRadio = true,
            badge = "📻 24H 電台直連"
        ),
        AuraTrack(
            id = "rad_2",
            title = "SomaFM PopTron (歐美流行電音舞曲)",
            artist = "SomaFM San Francisco",
            album = "Electropop & Indie Dance",
            durationSec = 0,
            coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400",
            directStreamUrl = "https://ice1.somafm.com/poptron-128-mp3",
            isRadio = true,
            badge = "📻 24H 電台直連"
        ),
        AuraTrack(
            id = "rad_3",
            title = "Radio Paradise Main Mix (全球流行搖滾 192k)",
            artist = "Radio Paradise California",
            album = "HD Pop & Rock Mix",
            durationSec = 0,
            coverUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400",
            directStreamUrl = "https://stream.radioparadise.com/mp3-192",
            isRadio = true,
            badge = "📻 24H 電台直連"
        ),
        AuraTrack(
            id = "rad_4",
            title = "SomaFM Groove Salad (Lo-Fi 弛放節奏)",
            artist = "SomaFM Chillout",
            album = "Ambient & Chill Beats",
            durationSec = 0,
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=400",
            directStreamUrl = "https://ice1.somafm.com/groovesalad-128-mp3",
            isRadio = true,
            badge = "📻 24H 電台直連"
        ),
        AuraTrack(
            id = "rad_5",
            title = "SomaFM Indie Pop Rocks!",
            artist = "SomaFM Indie Channel",
            album = "Indie Pop Hits",
            durationSec = 0,
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400",
            directStreamUrl = "https://ice1.somafm.com/indiepop-128-mp3",
            isRadio = true,
            badge = "📻 24H 電台直連"
        )
    )

    fun buildInitialTracksForCategory(catKey: String): List<AuraTrack> {
        if (catKey == "live_radio") return curatedRadios
        val cat = categories.find { it.key == catKey } ?: categories.first()
        val buckets = cat.seeds.mapIndexed { aIdx, (artist, songs) ->
            songs.mapIndexed { sIdx, songTitle ->
                val dur = 210 + ((aIdx * 13 + sIdx * 17) % 90)
                AuraTrack(
                    id = "seed_${cat.key}_${aIdx}_$sIdx",
                    title = songTitle,
                    artist = artist,
                    album = cat.badge,
                    durationSec = dur,
                    coverUrl = "",
                    badge = cat.badge
                )
            }
        }
        // Interleave tracks across artists so the playlist feels diverse
        val interleaved = mutableListOf<AuraTrack>()
        val maxLen = buckets.maxOfOrNull { it.size } ?: 0
        for (i in 0 until maxLen) {
            for (b in buckets) {
                if (i < b.size) interleaved.add(b[i])
            }
        }
        return interleaved
    }
}
