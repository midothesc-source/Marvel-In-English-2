package com.example.data

import com.example.model.CefrLevel
import com.example.model.DesertRank
import com.example.model.QuizQuestion
import com.example.model.SkillCategory

object QuestionBank {

    private val validVipCodes = setOf(
        "MARVEL-VIP",
        "DESERT-FORCE",
        "CINEMA-2026",
        "HERO-C1"
    )

    fun isValidVipCode(input: String): Boolean {
        return input.trim().uppercase() in validVipCodes
    }

    val desertRanks: List<DesertRank> = listOf(
        DesertRank(
            id = "scout",
            title = "Desert Scout",
            arabicTitle = "كشّاف الصحراء",
            minXp = 0,
            mottoEn = "Every legend begins with the courage to enter the arena.",
            mottoAr = "أول خطوة في الصحرا بتفرق بين المتفرج والبطل."
        ),
        DesertRank(
            id = "pathfinder",
            title = "Sand Pathfinder",
            arabicTitle = "دليل الرمال",
            minXp = 150,
            mottoEn = "You spot idioms and phrasal traps before the wind shifts.",
            mottoAr = "عينك بدأت تلقط التعبيرات والتريكات قبل ما تقع فيها."
        ),
        DesertRank(
            id = "vanguard",
            title = "Oasis Vanguard",
            arabicTitle = "طليعة الواحة",
            minXp = 350,
            mottoEn = "Calm under pressure, razor-sharp with everyday fluency.",
            mottoAr = "ثابت في الكلام، وكلمتك دايماً في الجون."
        ),
        DesertRank(
            id = "tactician",
            title = "Mirage Tactician",
            arabicTitle = "مخطط السراب",
            minXp = 650,
            mottoEn = "Collocations and proverbs flow naturally in your speech.",
            mottoAr = "الأمثال والمتلازمات اللفظية بقت بتطلع منك بسلاسة."
        ),
        DesertRank(
            id = "commander",
            title = "Falcon Commander",
            arabicTitle = "قائد الصقور",
            minXp = 1000,
            mottoEn = "Your English strikes with cinematic clarity and power.",
            mottoAr = "تركيزك عالي وإنجليزيتك بتتكلم زي نجوم السينما."
        ),
        DesertRank(
            id = "master",
            title = "Desert Force Master",
            arabicTitle = "سيد قوة الصحراء",
            minXp = 1500,
            mottoEn = "Supreme C1–C2 fluency. You own the Ultimate Arena.",
            mottoAr = "أسطورة الساحة.. قفلت اللعبة يا بطل!"
        )
    )

    fun rankForXp(xp: Int): DesertRank {
        return desertRanks.lastOrNull { xp >= it.minXp } ?: desertRanks.first()
    }

    fun nextRankForXp(xp: Int): DesertRank? {
        return desertRanks.firstOrNull { it.minXp > xp }
    }

    val allQuestions: List<QuizQuestion> = listOf(
        // ==========================================
        // LEVEL 1: BEGINNER & ESSENTIAL IDIOMS (A2-B1)
        // ==========================================
        QuizQuestion(
            id = "l1_01",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "piece of cake",
            prompt = "Don't stress about today's quiz—after studying all week, it will be a _____!",
            options = listOf("piece of cake", "cup of tea", "slice of bread", "drop of water"),
            correctIndex = 0,
            explanationAr = "التعبير الإنجليزي الشهير a piece of cake معناه حرفياً 'حتة كيكة'، والمقصود بيه في الكلام اليومي حاجة سهلة جداً زي شرب الميه.",
            proTipAr = "حركة فنان: في العربي بنقول 'زي السكينة في الحلاوة' أو 'سهل مية'، وفي الإنجليزي بيقولوا 'حتة تورتة' = a piece of cake!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_02",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.PHRASAL_VERBS,
            focusTag = "look after",
            prompt = "Could you _____ my backpack for a minute while I grab a coffee?",
            options = listOf("look up", "look after", "look into", "look out"),
            correctIndex = 1,
            explanationAr = "الفعل look بيتغير معناه حسب حرف الجر: look after يعني 'يخلي باله من' أو 'يعتني بـ'، بينما look up يعني يدور على معلومة.",
            proTipAr = "حركة فنان: تخيل إنك بتمشي ورا (after) الحاجة عشان تحرسها وتاخد بالك منها = look after!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_03",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "make vs. do",
            prompt = "Every beginner _____ a few mistakes at first—that's how fluency grows!",
            options = listOf("does", "makes", "takes", "puts"),
            correctIndex = 1,
            explanationAr = "الغلطة في الإنجليزي مش بتتعمل بـ do، إحنا بنصنعها بـ make! فدايماً بنقول make a mistake مش do a mistake.",
            proTipAr = "حركة فنان: الحاجات اللي فيها نتيجة أو قرار جديد (mistake, decision, noise) تاخد make. الشغل والروتين (homework, favors) ياخدوا do!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_04",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "under the weather",
            prompt = "Salma skipped training tonight because she is feeling a bit _____.",
            options = listOf("over the moon", "under the weather", "in hot water", "on cloud nine"),
            correctIndex = 1,
            explanationAr = "تعبير under the weather معناه 'تعبان شوية أو حاسس بإرهاق وبرد خفيف'، ومالوش علاقة بالطقس حرفياً!",
            proTipAr = "حركة فنان: الجو قالب ومضغوط تحت الغيوم = مزاجك وصحتك مش تمام: under the weather. عكسها طاير من الفرحة = over the moon!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_05",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.PHRASAL_VERBS,
            focusTag = "give up",
            prompt = "Learning pronunciation takes patience, so never _____ on yourself.",
            options = listOf("give away", "give up", "give back", "give in"),
            correctIndex = 1,
            explanationAr = "الفعل give up معناه 'يستسلم' أو 'يبطل محاولة'. أما give away فمعناه يتبرع بحاجة أو يكشف سر.",
            proTipAr = "حركة فنان: بترفع إيديك لفوق (UP) لما ترفع الراية البيضا، بس الأبطال عمرهم ما يعملوا give UP!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_06",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "break a leg",
            prompt = "Before Nadia walked onto the stage, her friends smiled and shouted, \"_____!\"",
            options = listOf("Break a leg", "Hit the sack", "Pull my leg", "Miss the boat"),
            correctIndex = 0,
            explanationAr = "عجيبة شوية بس حقيقية! في المسرح والسينما لما تقول لحد Break a leg معناها 'بالتوفيق وتكسر الدنيا' مش تتكسر رجلك لا قدر الله!",
            proTipAr = "حركة فنان: في مصر بنقول 'هتكسر الدنيا' قبل العرض، وبالظبط في الإنجليزي بيقولوا Break a leg كدعوة بالحظ السعيد!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_07",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.PHRASAL_VERBS,
            focusTag = "turn down",
            prompt = "Can you please _____ the volume? I'm trying to concentrate on my call.",
            options = listOf("turn up", "turn off", "turn down", "turn over"),
            correctIndex = 2,
            explanationAr = "لما تطلب من حد يوطي الصوت (مش يقفله خالص)، بتقول turn down the volume. عكسها turn up يعني يعلّي الصوت.",
            proTipAr = "حركة فنان: DOWN = لتحت (وطّي الصوت أو ارفض عرض). UP = لفوق (علّي الصوت). OFF = اقفل تماماً!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_08",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "do a favor",
            prompt = "Hey Omar, could you _____ me a quick favor and send the notes?",
            options = listOf("make", "do", "give", "bring"),
            correctIndex = 1,
            explanationAr = "كلمة favor ('خدمة أو معروف') بتيجي دايماً مع الفعل do، يعني do me a favor مش make me a favor.",
            proTipAr = "حركة فنان: الخدمة اللي بتعملها لحد ده مجهود ومساعدة، والمجهود دايماً مع DO: do a favor, do your best!",
            baseXp = 25
        ),
        QuizQuestion(
            id = "l1_09",
            level = CefrLevel.LEVEL_1,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "hit the sack",
            prompt = "It's past midnight and I have an early flight—time to _____.",
            options = listOf("hit the sack", "hit the road", "call it a day", "cost an arm"),
            correctIndex = 0,
            explanationAr = "التعبير hit the sack (أو hit the hay) معناه العامي 'أنا داخل أنام خلاص' من كتر التعب!",
            proTipAr = "حركة فنان: زمان المراتب كانت شوال قش (sack)، فلما ترمي نفسك عليه تنام = hit the sack. أما hit the road فمعناها يبدأ سفر!",
            baseXp = 25
        ),

        // ==========================================
        // LEVEL 2: INTERMEDIATE & COLLOQUIAL POWER (B1-B2)
        // ==========================================
        QuizQuestion(
            id = "l2_01",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "hit the nail on the head",
            prompt = "When you said the real problem is timing, not budget, you _____.",
            options = listOf(
                "missed the point",
                "hit the nail on the head",
                "beat around the bush",
                "sat on the fence"
            ),
            correctIndex = 1,
            explanationAr = "معناه 'جبت التايهة' أو 'وصفت المشكلة بالظبط في الصميم'—زي النجار اللي بيخبط على راس المسمار من أول دقة!",
            proTipAr = "حركة فنان: خبطت على المسمار في راسه = جبت الكلام في الجون. عكسها beat around the bush يعني بيلف ويدور!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_02",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "Proverb: Actions vs Words",
            prompt = "Stop promising to practice every day and just start—remember, _____.",
            options = listOf(
                "actions speak louder than words",
                "curiosity killed the cat",
                "every cloud has a silver lining",
                "two wrongs don't make a right"
            ),
            correctIndex = 0,
            explanationAr = "المثل الإنجليزي الشهير Actions speak louder than words معناه 'الأفعال أبلغ من الأقوال' أو بالمصري 'اللي بيتكلم كتير مبيعملش'!",
            proTipAr = "حركة فنان: إحنا في مصر بنقول 'اسمع كلامك أصدقك، أشوف أمورك أستعجب'—في الإنجليزي: Actions speak louder than words!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_03",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "make up your mind",
            prompt = "The train leaves in ten minutes! You need to _____ and pick a seat.",
            options = listOf("make up your mind", "do up your mind", "take up your mind", "keep up your mind"),
            correctIndex = 0,
            explanationAr = "التعبير make up your mind معناه 'تاخد قرار نهائي وتحسم أمرك' بدل التردد.",
            proTipAr = "حركة فنان: ترتّب عقلك وتصنع قرارك = MAKE up your mind (عمرها ما بتيجي مع do!).",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_04",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "spill the beans",
            prompt = "We are planning a surprise party for Karim, so whatever you do, don't _____!",
            options = listOf("break the ice", "spill the beans", "bite your tongue", "burn bridges"),
            correctIndex = 1,
            explanationAr = "تعبير spill the beans معناه 'يفتن أو يفضح السر بالغلط' ويبوظ المفاجأة!",
            proTipAr = "حركة فنان: لو وقّعت الفول (beans) اتفضح الموضوع! بالمصري: 'أوعى تبق البحصة' = don't spill the beans!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_05",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.PHRASAL_VERBS,
            focusTag = "come up with",
            prompt = "Our startup team had to _____ a creative marketing plan overnight.",
            options = listOf("come across", "come down with", "come up with", "come into"),
            correctIndex = 2,
            explanationAr = "الفعل come up with معناه 'يبتكر فكرة أو حل جديد'. أما come across يعني يقابل بالصدفة، و come down with يعني يصاب بمرض.",
            proTipAr = "حركة فنان: اللمبة نورت فوق (UP) دماغك وطلعت بفكرة = come UP with an idea!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_06",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "heavy rain / strong coffee",
            prompt = "I need a cup of _____ coffee before driving through this _____ rain.",
            options = listOf("heavy / strong", "strong / heavy", "big / powerful", "hard / thick"),
            correctIndex = 1,
            explanationAr = "المتلازمات اللفظية (Collocations) بتفرق بين المبتدئ والمحترف: القهوة بتكون strong coffee، والمطر الغزير والمرور الكتير بيكونوا heavy rain / heavy traffic!",
            proTipAr = "حركة فنان: القهوة والرياح والرأى = STRONG. المطر والزحمة والنوم التقيل = HEAVY!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_07",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.PHRASAL_VERBS,
            focusTag = "put off vs. put up with",
            prompt = "I can't _____ this constant construction noise outside my window anymore!",
            options = listOf("put off", "put out", "put up with", "put away"),
            correctIndex = 2,
            explanationAr = "معناه 'يتحمّل أو يصبر على حاجة مزعجة': put up with (= tolerate). بينما put off يعني يؤجل، و put out يعني يطفي نار.",
            proTipAr = "حركة فنان: 3 كلمات = صبر طويل! Put UP WITH = تستحمل شخص أو دوشة غصب عنك!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_08",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "break the ice",
            prompt = "The presenter opened with a relatable Egyptian joke to _____ in the room.",
            options = listOf("break the ice", "rock the boat", "kick the bucket", "call the shots"),
            correctIndex = 0,
            explanationAr = "تعبير break the ice معناه 'يكسر التوتر والكسوف في بداية أي قعدة أو محاضرة' عشان الناس ترتاح وتتكلم.",
            proTipAr = "حركة فنان: التلج بيجمد الجو، وأول ضحكة أو تعارف بيكسره = break the ice!",
            baseXp = 35
        ),
        QuizQuestion(
            id = "l2_09",
            level = CefrLevel.LEVEL_2,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "reliant on",
            prompt = "Modern remote teams are deeply reliant _____ fast cloud collaboration tools.",
            options = listOf("with", "on", "to", "for"),
            correctIndex = 1,
            explanationAr = "عيلة الاعتماد كلها في الإنجليزي (depend, dependent, rely, reliant) بتاخد حرف الجر on. خد بالك إن independent هي اللي بتاخد of أو from!",
            proTipAr = "حركة فنان: معتمد على = واقف على أرض ثابتة = ON! دايماً reliant ON / dependent ON!",
            baseXp = 35
        ),

        // ==========================================
        // LEVEL 3: ADVANCED & CINEMATIC MASTERY (C1-C2 VIP)
        // ==========================================
        QuizQuestion(
            id = "l3_01",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.CINEMATIC_SLANG,
            focusTag = "steal someone's thunder",
            prompt = "Right when Youssef announced his promotion, his cousin revealed her engagement and _____.",
            options = listOf(
                "stole his thunder",
                "threw in the towel",
                "pulled some strings",
                "kept a low profile"
            ),
            correctIndex = 0,
            explanationAr = "تعبير سينمائي ودرامي رائع! steal someone's thunder معناه 'يسرق الأضواء من حد' ويبوظ لحظة تألقه بإعلان تاني أو خطف الاهتمام.",
            proTipAr = "حركة فنان: أصلها مسرحي لما واحد اخترع صوت الرعد في مسرحية ومنافسه سرق الفكرة! بالمصري: 'سحب البساط من تحت رجليه'!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_02",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.CINEMATIC_SLANG,
            focusTag = "cut to the chase",
            prompt = "We only have five minutes before the pitch starts, so please _____ and share the numbers.",
            options = listOf(
                "cut corners",
                "cut to the chase",
                "cut it out",
                "cut loose"
            ),
            correctIndex = 1,
            explanationAr = "أشهر تعبير من أفلام هوليوود! cut to the chase معناه 'ادخل في الموضوع على طول من غير مقدمات طويلة'.",
            proTipAr = "حركة فنان: في الأفلام الصامتة المخرج كان يقص المشاهد المملة ويدخل على مشهد المطاردة (chase) فوراً! بالمصري: 'هات من الآخر'!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_03",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "elephant in the room",
            prompt = "Everyone praised the visuals, yet nobody addressed the _____—the audio was completely broken.",
            options = listOf(
                "elephant in the room",
                "skeleton in the closet",
                "wolf in sheep's clothing",
                "fly on the wall"
            ),
            correctIndex = 0,
            explanationAr = "تعبير عبقري للمواقف المحرجة: elephant in the room معناه 'مشكلة ضخمة وواضحة قدام الكل بس الناس بتتجاهلها وتعمل نفسها مش واخدة بالها'!",
            proTipAr = "حركة فنان: فيل واقف في نص الأوضة والكل ساكت مش عايز يفتحه = elephant in the room!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_04",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.CINEMATIC_SLANG,
            focusTag = "no-brainer",
            prompt = "Getting a full scholarship with free mentorship? Accepting that offer is an absolute _____!",
            options = listOf("long shot", "no-brainer", "red herring", "close call"),
            correctIndex = 1,
            explanationAr = "كلمة slang شيك ومنتشرة جداً في الأفلام والبيزنس: a no-brainer معناها 'قرار سهل وبديهي مش محتاج تفكير أصلاً'!",
            proTipAr = "حركة فنان: من كتر ما القرار واضح مش محتاج تشغل دماغك (no brain needed) = it's a no-brainer!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_05",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "play devil's advocate",
            prompt = "I actually agree with your plan, but let me _____ to test if investors can poke holes in it.",
            options = listOf(
                "play it by ear",
                "play devil's advocate",
                "call it a night",
                "go the extra mile"
            ),
            correctIndex = 1,
            explanationAr = "لما تتعمد تاخد الرأي المعارض في نقاش عشان تختبر قوة الفكرة وتقفل أي ثغرات، ده اسمه play devil's advocate.",
            proTipAr = "حركة فنان: مش معناه إنك مع الطرف الغلط—إنت بس بتعمل دور المعارض الذكي عشان تقوي حجتك قبل المناقشة الحقيقية!",
            baseXp = 50
        ),
        QuizQuestion(
            id = "l3_06",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.COLLOCATIONS,
            focusTag = "hit the ground running",
            prompt = "Because Hana mastered the codebase beforehand, she _____ on her very first day.",
            options = listOf(
                "hit the ground running",
                "missed the mark",
                "jumped the gun",
                "dragged her feet"
            ),
            correctIndex = 0,
            explanationAr = "تعبير احترافي جداً معناه 'بدأ الشغل بحماس وسرعة ومن غير ما يضيع وقت في التأقلم'!",
            proTipAr = "حركة فنان: تخيل بطل أكشن بينط من الهليكوبتر ويجري أول ما رجله تلمس الأرض = hit the ground running!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_07",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.CINEMATIC_SLANG,
            focusTag = "bite the bullet",
            prompt = "I've delayed fixing this tricky bug all week—time to _____ and tackle it head-on.",
            options = listOf(
                "bite the bullet",
                "dodge a bullet",
                "bite off more",
                "take a rain check"
            ),
            correctIndex = 0,
            explanationAr = "تعبير bite the bullet معناه 'تشد على نفسك وتواجه موقف صعب أو تقيل غصب عنك بشجاعة' بدل التأجيل.",
            proTipAr = "حركة فنان: فرق مهم! dodge a bullet = فلتّت من مصيبة على شعرة. bite the bullet = استجمعت شجاعتك وواجهت الصعب!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_08",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.PHRASAL_VERBS,
            focusTag = "burn the midnight oil",
            prompt = "The film editors had to _____ all week to finish the trailer before the festival deadline.",
            options = listOf(
                "burn the midnight oil",
                "burn their bridges",
                "let off steam",
                "call the shots"
            ),
            correctIndex = 0,
            explanationAr = "معناه 'يسهر يشتغل أو يذاكر لوش الفجر'! جاية من أيام لمبة الجاز لما الواحد يولّع الزيت لحد نص الليل عشان يخلص شغله.",
            proTipAr = "حركة فنان: تسهر تذاكر أو تصمم لحد الفجر = burn the midnight oil. أما burn bridges فمعناه تقطع علاقتك بحد للأبد!",
            baseXp = 45
        ),
        QuizQuestion(
            id = "l3_09",
            level = CefrLevel.LEVEL_3,
            category = SkillCategory.IDIOMATIC_POWER,
            focusTag = "read between the lines",
            prompt = "Her email sounded polite, but if you _____, she clearly wasn't happy with the delay.",
            options = listOf(
                "read between the lines",
                "read the riot act",
                "throw the book at her",
                "take it with a pinch of salt"
            ),
            correctIndex = 0,
            explanationAr = "بالظبط زي العربي 'اقرأ ما بين السطور'! معناه تفهم المعنى الخفي أو الإحساس الحقيقي اللي متقالش بشكل مباشر.",
            proTipAr = "حركة فنان: المعنى الحقيقي مستخبي في المسافة البيضا بين السطور = read between the lines!",
            baseXp = 45
        )
    )

    fun questionsForMission(
        level: CefrLevel,
        categoryFilter: SkillCategory? = null
    ): List<QuizQuestion> {
        val pool = allQuestions.filter {
            it.level == level && (categoryFilter == null || it.category == categoryFilter)
        }
        return pool.shuffled()
    }

    fun questionById(id: String): QuizQuestion? = allQuestions.find { it.id == id }
}
