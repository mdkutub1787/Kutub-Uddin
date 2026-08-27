import '../screens/dua_screen.dart';

final List<DuaCategory> allDuaCategories = [
  DuaCategory(
    title: 'Ablution (Wudhu)',
    iconAsset: '💧',
    duas: [
      DuaItem(
        arabic: 'بِسْمِ اللَّهِ',
        transliteration: 'Bismillah',
        translation: 'In the name of Allah.',
        reference: 'Abu Dawud',
      ),
      DuaItem(
        arabic: 'أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ',
        transliteration: 'Ashhadu an la ilaha illallah wahdahu la sharika lah, wa ashhadu anna Muhammadan abduhu wa rasuluh.',
        translation: 'I bear witness that none has the right to be worshipped but Allah alone, Who has no partner; and I bear witness that Muhammad is His slave and His Messenger.',
        reference: 'Muslim 1/209',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ',
        transliteration: 'Allahummaj-alni minat-tawwabina waj-alni minal-mutatahhireen.',
        translation: 'O Allah, make me among those who turn to You in repentance, and make me among those who are purified.',
        reference: 'At-Tirmidhi 1/78',
      ),
    ],
  ),
  DuaCategory(
    title: 'Athan',
    iconAsset: '🕌',
    duas: [
      DuaItem(
        arabic: 'اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ، وَالصَّلاَةِ الْقَائِمَةِ، آتِ مُحَمَّداً الْوَسِيلَةَ وَالْفَضِيلَةَ، وَابْعَثْهُ مَقَاماً مَحْمُوداً الَّذِي وَعَدْتَهُ',
        transliteration: 'Allahumma Rabba hadhihi-d-da\'watit-tammah, was-salatil qa\'imah, ati Muhammadan al-wasilata wal-fadhilah, wab\'ath-hu maqaman mahmudan alladhi wa\'adtah.',
        translation: 'O Allah! Lord of this perfect call and of the regular prayer which is going to be established, give Muhammad the right of intercession and illustriousness, and resurrect him to the best and the highest place in Paradise that You promised him.',
        reference: 'Al-Bukhari 1/152',
      ),
      DuaItem(
        arabic: 'وَأَنَا أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ، رَضِيتُ بِاللَّهِ رَبًّا وَبِمُحَمَّدٍ رَسُولًا وَبِالْإِسْلَامِ دِينًا',
        transliteration: 'Wa ana ashhadu an la ilaha illallah wahdahu la sharika lah, wa anna Muhammadan abduhu wa rasuluh, radhitu Billahi Rabban, wa bi-Muhammadin Rasulan, wa bil-islami dinan.',
        translation: 'I also bear witness that none has the right to be worshipped but Allah alone, Who has no partner, and that Muhammad is His slave and His Messenger. I am pleased with Allah as my Lord, with Muhammad as my Messenger and with Islam as my religion.',
        reference: 'Muslim 1/290',
      ),
    ],
  ),
  DuaCategory(
    title: 'Clothing',
    iconAsset: '👕',
    duas: [
      DuaItem(
        arabic: 'الْحَمْدُ لِلَّهِ الَّذِي كَسَانِي هَذَا (الثَّوْبَ) وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ',
        transliteration: 'Alhamdu lillahil-ladhi kasani hadha (ath-thawb) wa razaqanihi min ghayri hawlin minni wa la quwwatin.',
        translation: 'Praise is to Allah Who has clothed me with this (garment) and provided it for me, though I was powerless myself and incapable.',
        reference: 'Al-Bukhari',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ لَكَ الْحَمْدُ أَنْتَ كَسَوْتَنِيهِ، أَسْأَلُكَ مِنْ خَيْرِهِ وَخَيْرِ مَا صُنِعَ لَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّهِ وَشَرِّ مَا صُنِعَ لَهُ',
        transliteration: 'Allahumma lakal-hamd, Anta kasawtanihi, as\'aluka min khayrihi wa khayri ma suni\'a lah, wa a\'udhu bika min sharrihi wa sharri ma suni\'a lah.',
        translation: 'O Allah, praise is to You. You have clothed me. I ask You for its goodness and the goodness of what it has been made for, and I seek Your protection from the evil of it and the evil of what it has been made for.',
        reference: 'Abu Dawud',
      ),
      DuaItem(
        arabic: 'بِسْمِ اللَّهِ',
        transliteration: 'Bismillah',
        translation: 'In the Name of Allah.',
        reference: 'At-Tirmidhi',
      ),
    ],
  ),
  DuaCategory(
    title: 'Difficulties & Distress',
    iconAsset: '😔',
    duas: [
      DuaItem(
        arabic: 'لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ',
        transliteration: 'La ilaha illa anta subhanaka inni kuntu minadh-dhalimin.',
        translation: 'None has the right to be worshipped but You (O Allah), Glorified (and Exalted) are You. Truly, I have been of the wrong-doers.',
        reference: 'Quran 21:87',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ لَا سَهْلَ إِلَّا مَا جَعَلْتَهُ سَهْلاً، وَأَنْتَ تَجْعَلُ الْحَزَنَ إِذَا شِئْتَ سَهْلاً',
        transliteration: 'Allahumma la sahla illa ma ja\'altahu sahlan, wa Anta taj\'alul-hazana idha shi\'ta sahlan.',
        translation: 'O Allah, there is no ease other than what You make easy. If You please You ease sorrow.',
        reference: 'Ibn Hibban',
      ),
      DuaItem(
        arabic: 'حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ',
        transliteration: 'Hasbunallahu wa ni\'mal-wakil.',
        translation: 'Allah (Alone) is Sufficient for us, and He is the Best Disposer of affairs (for us).',
        reference: 'Quran 3:173',
      ),
      DuaItem(
        arabic: 'إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي، وَأَخْلِفْ لِي خَيْرًا مِنْهَا',
        transliteration: 'Inna lillahi wa inna ilayhi raji\'un, Allahumma\'jurni fi musibati, wa akhlif li khayran minha.',
        translation: 'We are from Allah and unto Him we return. O Allah take me out of my plight and bring to me after it something better.',
        reference: 'Muslim 2/632',
      ),
    ],
  ),
  DuaCategory(
    title: 'Emotion Based',
    iconAsset: '🎭',
    duas: [
      DuaItem(
        arabic: 'اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ',
        transliteration: 'Allahumma inni a\'udhu bika minal-hammi wal-hazan, wal-\'ajzi wal-kasal, wal-bukhli wal-jubn, wa dhala\'id-dayni wa ghalabatir-rijal.',
        translation: 'O Allah, I seek refuge in You from anxiety and sorrow, weakness and laziness, miserliness and cowardice, the burden of debts and from being overpowered by men.',
        reference: 'Al-Bukhari 7/158',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ إِنِّي أَسْأَلُكَ نَفْسًا بِكَ مُطْمَئِنَّةً، تُؤْمِنُ بِلِقَائِكَ، وَتَرْضَى بِقَضَائِكَ، وَتَقْنَعُ بِعَطَائِكَ',
        transliteration: 'Allahumma inni as\'aluka nafsan bika mutma\'innah, tu\'minu biliqa\'ika, wa tardha biqada\'ika, wa taqna\'u bi\'ata\'ika.',
        translation: 'O Allah, I ask You for a soul that finds peace in You, believes in meeting You, is satisfied with Your decree, and is content with Your gift.',
        reference: 'At-Tabarani',
      ),
    ],
  ),
  DuaCategory(
    title: 'Home',
    iconAsset: '🏠',
    duas: [
      DuaItem(
        arabic: 'بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى رَبِّنَا تَوَكَّلْنَا',
        transliteration: 'Bismillahi walajna, wa bismillahi kharajna, wa \'ala Rabbina tawakkalna.',
        translation: 'In the name of Allah we enter, in the name of Allah we leave, and upon our Lord we depend.',
        reference: 'Abu Dawud 4/325',
      ),
      DuaItem(
        arabic: 'بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ',
        transliteration: 'Bismillahi tawakkaltu \'alallahi, wa la hawla wa la quwwata illa billah.',
        translation: 'In the Name of Allah, I have placed my trust in Allah, there is no might and no power except by Allah.',
        reference: 'Abu Dawud 4/325',
      ),
    ],
  ),
  DuaCategory(
    title: 'Marketplace / Shopping',
    iconAsset: '🏪',
    duas: [
      DuaItem(
        arabic: 'لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، يُحْيِي وَيُمِيتُ، وَهُوَ حَيٌّ لَا يَمُوتُ، بِيَدِهِ الْخَيْرُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ',
        transliteration: 'La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu, yuhyi wa yumitu, wa Huwa hayyun la yamutu, biyadihil-khayru, wa Huwa \'ala kulli shay\'in qadir.',
        translation: 'None has the right to be worshipped but Allah alone, Who has no partner. His is the dominion and His is the praise. He brings life and He causes death, and He is living and does not die. In His Hand is all good, and He is Able to do all things.',
        reference: 'At-Tirmidhi 5/291',
      ),
    ],
  ),
  DuaCategory(
    title: 'Morning',
    iconAsset: '🌅',
    duas: [
      DuaItem(
        arabic: 'الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ',
        transliteration: 'Alhamdu lillahil-ladhi ahyana ba\'da ma amatana wa ilaihin-nushur.',
        translation: 'Praise is to Allah Who gives us life after He has caused us to die and to Him is the return.',
        reference: 'Al-Bukhari',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ',
        transliteration: 'Allahumma bika asbahna wa bika amsayna, wa bika nahya wa bika namutu wa ilaykan-nushur.',
        translation: 'O Allah, by You we enter the morning and by You we enter the evening, by You we live and by You we die, and to You is the Final Return.',
        reference: 'At-Tirmidhi',
      ),
    ],
  ),
  DuaCategory(
    title: 'Night',
    iconAsset: '🌙',
    duas: [
      DuaItem(
        arabic: 'بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا',
        transliteration: 'Bismika Allahumma amutu wa ahya.',
        translation: 'In Your name O Allah, I live and die.',
        reference: 'Al-Bukhari',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ',
        transliteration: 'Allahumma qini \'adhabaka yawma tab\'athu \'ibadak.',
        translation: 'O Allah, save me from Your punishment on the Day that You resurrect Your slaves.',
        reference: 'Abu Dawud',
      ),
    ],
  ),
  DuaCategory(
    title: 'Toilet',
    iconAsset: '🚽',
    duas: [
      DuaItem(
        arabic: 'اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ',
        transliteration: 'Allahumma inni a\'udhu bika minal khubuthi wal khaba-ith.',
        translation: 'O Allah, I seek refuge with You from all offensive and wicked things (evil demons).',
        reference: 'Al-Bukhari 1/45',
      ),
      DuaItem(
        arabic: 'غُفْرَانَكَ',
        transliteration: 'Ghufranaka.',
        translation: 'I ask Your forgiveness.',
        reference: 'Abu Dawud',
      ),
    ],
  ),
  DuaCategory(
    title: 'Travel',
    iconAsset: '🚌',
    duas: [
      DuaItem(
        arabic: 'بِسْمِ اللَّهِ ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ',
        transliteration: 'Bismillaah. Subhaanal-lathee sakhkhara lanaa haathaa wa maa kunnaa lahu muqrineen. Wa innaa ilaa Rabbinaa lamunqaliboon.',
        translation: 'In the name of Allah. All praise is for Allah who is free from all and any imperfections. The One Who has placed this (transport) at our service and we ourselves would not have been capable of that, and to our Lord is our ultimate final destination.',
        reference: 'Quran 43:13-14',
      ),
      DuaItem(
        arabic: 'اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى',
        transliteration: 'Allahumma inna nas\'aluka fi safarina hadhal-birra wat-taqwa, wa minal-\'amali ma tardha.',
        translation: 'O Allah, we ask You on this our journey for goodness and piety, and for works that are pleasing to You.',
        reference: 'Muslim',
      ),
    ],
  ),
];
