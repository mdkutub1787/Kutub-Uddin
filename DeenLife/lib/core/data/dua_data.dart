class Dua {
  final int id;
  final String title;
  final String arabic;
  final String meaningEn;
  final String meaningBn;

  const Dua({
    required this.id,
    required this.title,
    required this.arabic,
    required this.meaningEn,
    required this.meaningBn,
  });
}

const List<Dua> dailyDuas = [
  Dua(
    id: 1,
    title: 'Before Sleeping',
    arabic: 'بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا',
    meaningEn: 'In Your name, O Allah, I die and I live.',
    meaningBn:
        'হে আল্লাহ! আপনার নামেই আমি মৃত্যুবরণ করি এবং আপনার নামেই জীবিত হই।',
  ),
  Dua(
    id: 2,
    title: 'Upon Waking Up',
    arabic: 'الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ',
    meaningEn: 'Praise is to Allah Who gives us life after He has caused us to die and to Him is the return.',
    meaningBn: 'সব প্রশংসা আল্লাহর জন্য, যিনি আমাদের মৃত্যুর পর পুনরায় জীবন দান করেছেন, আর তাঁর কাছেই আমাদের ফিরে যেতে হবে।',
  ),
  Dua(
    id: 3,
    title: 'Before Eating',
    arabic: 'بِسْمِ اللَّهِ',
    meaningEn: 'In the name of Allah.',
    meaningBn: 'আল্লাহর নামে শুরু করছি।',
  ),
  Dua(
    id: 4,
    title: 'After Eating',
    arabic: 'الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ',
    meaningEn: 'Praise be to Allah Who has fed us and given us drink and made us Muslims.',
    meaningBn: 'সব প্রশংসা আল্লাহর, যিনি আমাদের খাইয়েছেন, পান করিয়েছেন এবং মুসলিম বানিয়েছেন।',
  ),
  Dua(
    id: 5,
    title: 'Entering the Toilet',
    arabic: 'اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ',
    meaningEn: 'O Allah, I seek refuge with You from male and female devils.',
    meaningBn: 'হে আল্লাহ! আমি পুরুষ ও নারী শয়তানদের থেকে আপনার আশ্রয় চাই।',
  ),
  Dua(
    id: 6,
    title: 'Leaving the Toilet',
    arabic: 'غُفْرَانَكَ',
    meaningEn: 'I ask You for forgiveness.',
    meaningBn: 'আমি আপনার কাছে ক্ষমা প্রার্থনা করছি।',
  ),
  Dua(
    id: 7,
    title: 'Leaving the House',
    arabic: 'بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ',
    meaningEn: 'In the name of Allah, I place my trust in Allah, and there is no might nor power except with Allah.',
    meaningBn: 'আল্লাহর নামে শুরু করছি, আমি আল্লাহর উপর ভরসা করলাম। আর আল্লাহ ছাড়া কোনো ক্ষমতা বা শক্তি নেই।',
  ),
  Dua(
    id: 8,
    title: 'Entering the House',
    arabic: 'بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى رَبِّنَا تَوَكَّلْنَا',
    meaningEn: 'In the name of Allah we enter, in the name of Allah we leave, and upon our Lord we depend.',
    meaningBn: 'আল্লাহর নামে আমরা প্রবেশ করলাম, আল্লাহর নামে আমরা বের হলাম এবং আমাদের রবের ওপর আমরা ভরসা করলাম।',
  ),
  Dua(
    id: 9,
    title: 'When in Distress',
    arabic:
        'لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ',
    meaningEn: 'There is none worthy of worship but You, glory is to You. Surely, I was among the wrongdoers.',
    meaningBn: 'আপনি ছাড়া আর কোনো মাবুদ নেই, আপনি পবিত্র। নিশ্চয়ই আমি জালিমদের অন্তর্ভুক্ত।',
  ),
  Dua(
    id: 10,
    title: 'For Parents',
    arabic: 'رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا',
    meaningEn: 'My Lord, have mercy upon them as they brought me up [when I was] small.',
    meaningBn: 'হে আমার রব! তাদের উভয়ের প্রতি রহম করুন, যেমনভাবে শৈসবে তারা আমাকে লালন-পালন করেছেন।',
  ),
];
