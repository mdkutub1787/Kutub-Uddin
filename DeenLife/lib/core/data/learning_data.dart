class IslamicResource {
  final String title;
  final String subtitle;
  final String content;
  final String category;

  IslamicResource({
    required this.title,
    required this.subtitle,
    required this.content,
    required this.category,
  });
}

final List<IslamicResource> namazResources = [
  IslamicResource(
    title: 'Five Daily Prayers',
    subtitle: 'Rules and Rakats',
    content: 'Fajr: 2 Sunnat, 2 Farz\nDhuhr: 4 Sunnat, 4 Farz...',
    category: 'Basics',
  ),
  IslamicResource(
    title: 'Janaza Prayer',
    subtitle: 'Method of Funeral Prayer',
    content: 'Step by step guide for Janaza...',
    category: 'Special',
  ),
  IslamicResource(
    title: 'Eid Prayer',
    subtitle: 'How to perform Eid Salah',
    content: 'Rules for 6 extra Takbirs...',
    category: 'Special',
  ),
];

final List<IslamicResource> quranLearningResources = [
  IslamicResource(
    title: 'Arabic Alphabet',
    subtitle: 'Lesson 1',
    content: 'Alif, Ba, Ta, Tha...',
    category: 'Noorani Qaida',
  ),
  IslamicResource(
    title: 'Harakat',
    subtitle: 'Lesson 2',
    content: 'Zabr, Zer, Pesh rules...',
    category: 'Noorani Qaida',
  ),
  IslamicResource(
    title: 'Tajweed Rules',
    subtitle: 'Advanced',
    content: 'Madd, Ghunnah, Ikhfa...',
    category: 'Tajweed',
  ),
];

final List<IslamicResource> islamicBooks = [
  IslamicResource(
    title: 'Ar-Raheeq Al-Makhtum',
    subtitle: 'The Sealed Nectar',
    content: 'Biography of Prophet (PBUH)...',
    category: 'Seerah',
  ),
  IslamicResource(
    title: 'Riyadus Salihin',
    subtitle: 'Gardens of the Righteous',
    content: 'Collection of Hadith on Ethics...',
    category: 'Hadith',
  ),
  IslamicResource(
    title: 'Tafsir Ibn Kathir',
    subtitle: 'Volume 1',
    content: 'Detailed Quranic Commentary...',
    category: 'Tafsir',
  ),
];

class ImamInfo {
  final String name;
  final String title;
  final String description;
  final String years;
  final String madhhab;

  ImamInfo({
    required this.name,
    required this.title,
    required this.description,
    required this.years,
    required this.madhhab,
  });
}

final List<ImamInfo> fourImams = [
  ImamInfo(
    name: 'Abu Hanifa',
    title: 'Imam-e-Azam',
    years: '80 AH - 150 AH',
    madhhab: 'Hanafi',
    description: 'The founder of the Hanafi school of Fiqh. Known for using legal logic and rationality (Qiyas) in jurisprudence.',
  ),
  ImamInfo(
    name: 'Malik bin Anas',
    title: 'Imam Dar al-Hijrah',
    years: '93 AH - 179 AH',
    madhhab: 'Maliki',
    description: 'The founder of the Maliki school. He authored the Al-Muwatta and focused heavily on the traditions of Madinah.',
  ),
  ImamInfo(
    name: 'Muhammad ash-Shafi\'i',
    title: 'Imam al-Shafi\'i',
    years: '150 AH - 204 AH',
    madhhab: 'Shafi\'i',
    description: 'The architect of Islamic jurisprudence. He balanced between Hadith and legal reasoning in his Usul al-Fiqh.',
  ),
  ImamInfo(
    name: 'Ahmad bin Hanbal',
    title: 'Imam al-Hadith',
    years: '164 AH - 241 AH',
    madhhab: 'Hanbali',
    description: 'Known for his immense knowledge of Hadith and his steadfastness during the Inquisition (Mihna).',
  ),
];
