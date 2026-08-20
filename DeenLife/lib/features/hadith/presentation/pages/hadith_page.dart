import 'package:flutter/material.dart';

class Hadith {
  final String narrator;
  final String text;
  final String reference;

  Hadith({
    required this.narrator,
    required this.text,
    required this.reference,
  });
}

class HadithPage extends StatelessWidget {
  HadithPage({super.key});

  final List<Hadith> hadiths = [
    Hadith(
      narrator: 'Umar bin Al-Khattab',
      text: 'I heard Allah\'s Messenger (ﷺ) saying, "The reward of deeds depends upon the intentions and every person will get the reward according to what he has intended."',
      reference: 'Sahih al-Bukhari 1',
    ),
    Hadith(
      narrator: 'Abu Huraira',
      text: 'The Prophet (ﷺ) said, "Religion is very easy and whoever overburdens himself in his religion will not be able to continue in that way. So you should not be extremists, but try to be near to perfection and receive the good tidings that you will be rewarded."',
      reference: 'Sahih al-Bukhari 39',
    ),
    Hadith(
      narrator: 'Abu Mas\'ud Al-Ansari',
      text: 'The Prophet (ﷺ) said, "If somebody recites the last two Verses of Surat Al-Baqara at night, that will be sufficient for him."',
      reference: 'Sahih al-Bukhari 5009',
    ),
    Hadith(
      narrator: 'Aisha',
      text: 'The Prophet (ﷺ) said, "The most hated person in the sight of Allah is the most quarrelsome person."',
      reference: 'Sahih al-Bukhari 2457',
    ),
    Hadith(
      narrator: 'Anas bin Malik',
      text: 'The Prophet (ﷺ) said, "None of you will have faith till he wishes for his (Muslim) brother what he likes for himself."',
      reference: 'Sahih al-Bukhari 13',
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Hadith Collection'),
        centerTitle: true,
      ),
      body: ListView.separated(
        padding: const EdgeInsets.all(16.0),
        itemCount: hadiths.length,
        separatorBuilder: (context, index) => const SizedBox(height: 16),
        itemBuilder: (context, index) {
          final hadith = hadiths[index];
          return Card(
            elevation: 2,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      Icon(Icons.person, color: Theme.of(context).colorScheme.primary, size: 20),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          'Narrated by ${hadith.narrator}:',
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: Theme.of(context).colorScheme.primary,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),
                  Text(
                    hadith.text,
                    style: const TextStyle(
                      fontSize: 18,
                      height: 1.5,
                      fontStyle: FontStyle.italic,
                    ),
                  ),
                  const SizedBox(height: 16),
                  Align(
                    alignment: Alignment.centerRight,
                    child: Container(
                      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                      decoration: BoxDecoration(
                        color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: Text(
                        hadith.reference,
                        style: TextStyle(
                          fontSize: 12,
                          fontWeight: FontWeight.bold,
                          color: Theme.of(context).colorScheme.primary,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
