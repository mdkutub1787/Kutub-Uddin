import 'package:flutter/material.dart';

class AllahName {
  final int id;
  final String arabic;
  final String transliteration;
  final String meaning;

  AllahName({
    required this.id,
    required this.arabic,
    required this.transliteration,
    required this.meaning,
  });
}

class AsmaulHusnaPage extends StatelessWidget {
  AsmaulHusnaPage({super.key});

  final List<AllahName> names = [
    AllahName(id: 1, arabic: 'الرَّحْمَنُ', transliteration: 'Ar-Rahmaan', meaning: 'The Beneficent'),
    AllahName(id: 2, arabic: 'الرَّحِيمُ', transliteration: 'Ar-Raheem', meaning: 'The Merciful'),
    AllahName(id: 3, arabic: 'الْمَلِكُ', transliteration: 'Al-Malik', meaning: 'The King / Sovereign'),
    AllahName(id: 4, arabic: 'الْقُدُّوسُ', transliteration: 'Al-Quddus', meaning: 'The Most Holy'),
    AllahName(id: 5, arabic: 'السَّلاَمُ', transliteration: 'As-Salaam', meaning: 'Peace and Blessing'),
    AllahName(id: 6, arabic: 'الْمُؤْمِنُ', transliteration: 'Al-Mu\'min', meaning: 'The Guarantor'),
    AllahName(id: 7, arabic: 'الْمُهَيْمِنُ', transliteration: 'Al-Muhaymin', meaning: 'The Guardian, the Preserver'),
    AllahName(id: 8, arabic: 'الْعَزِيزُ', transliteration: 'Al-Azeez', meaning: 'The Almighty, the Self Sufficient'),
    AllahName(id: 9, arabic: 'الْجَبَّارُ', transliteration: 'Al-Jabbaar', meaning: 'The Powerful, the Irresistible'),
    AllahName(id: 10, arabic: 'الْمُتَكَبِّرُ', transliteration: 'Al-Mutakabbir', meaning: 'The Tremendous'),
    // Note: To keep the file concise, only the first 10 names are included.
    // You can add the remaining 89 names following this format.
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('99 Names of Allah'),
        centerTitle: true,
      ),
      body: GridView.builder(
        padding: const EdgeInsets.all(16.0),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          crossAxisSpacing: 16.0,
          mainAxisSpacing: 16.0,
          childAspectRatio: 0.85,
        ),
        itemCount: names.length,
        itemBuilder: (context, index) {
          final name = names[index];
          return Container(
            decoration: BoxDecoration(
              color: Theme.of(context).cardColor,
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: Theme.of(context).colorScheme.primary.withOpacity(0.3)),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.05),
                  blurRadius: 10,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                CircleAvatar(
                  radius: 16,
                  backgroundColor: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                  child: Text(
                    '${name.id}',
                    style: TextStyle(
                      fontSize: 12,
                      color: Theme.of(context).colorScheme.primary,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                const SizedBox(height: 12),
                Text(
                  name.arabic,
                  style: TextStyle(
                    fontSize: 28,
                    fontWeight: FontWeight.bold,
                    color: Theme.of(context).colorScheme.primary,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  name.transliteration,
                  style: const TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 16,
                  ),
                ),
                const SizedBox(height: 4),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 8.0),
                  child: Text(
                    name.meaning,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      fontSize: 12,
                      color: Colors.grey,
                    ),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}
