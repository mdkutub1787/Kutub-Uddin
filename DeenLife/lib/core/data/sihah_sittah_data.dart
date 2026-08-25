class HadithBook {
  final String id;
  final String nameEn;
  final String nameBn;
  final String description;

  const HadithBook({
    required this.id,
    required this.nameEn,
    required this.nameBn,
    required this.description,
  });
}

const List<HadithBook> sihahSittahBooks = [
  HadithBook(
    id: 'bukhari',
    nameEn: 'Sahih al-Bukhari',
    nameBn: 'সহিহ বুখারী',
    description: 'The most authentic book after the Quran.',
  ),
  HadithBook(
    id: 'muslim',
    nameEn: 'Sahih Muslim',
    nameBn: 'সহিহ মুসলিম',
    description: 'Second most authentic book of Hadith.',
  ),
  HadithBook(
    id: 'abudawud',
    nameEn: 'Sunan Abu Dawood',
    nameBn: 'সুনান আবু দাউদ',
    description: 'Focuses primarily on legal hadiths (Ahkam).',
  ),
  HadithBook(
    id: 'tirmidhi',
    nameEn: 'Jami at-Tirmidhi',
    nameBn: 'জামে তিরমিযী',
    description: 'Includes extensive commentary on hadith grading.',
  ),
  HadithBook(
    id: 'nasai',
    nameEn: 'Sunan an-Nasa\'i',
    nameBn: 'সুনান আন-নাসাঈ',
    description: 'Known for its strict criteria in narration.',
  ),
  HadithBook(
    id: 'ibnmajah',
    nameEn: 'Sunan Ibn Majah',
    nameBn: 'সুনান ইবনে মাজাহ',
    description: 'The sixth book of the Sihah Sittah collection.',
  ),
];
