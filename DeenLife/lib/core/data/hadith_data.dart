class Hadith {
  final int id;
  final String narrator;
  final String textEn;
  final String textBn;
  final String reference;

  const Hadith({
    required this.id,
    required this.narrator,
    required this.textEn,
    required this.textBn,
    required this.reference,
  });
}

const List<Hadith> fortyHadithData = [
  Hadith(
    id: 1,
    narrator: 'Umar bin Al-Khattab',
    textEn: 'I heard Allah\'s Messenger (ﷺ) saying, "The reward of deeds depends upon the intentions and every person will get the reward according to what he has intended."',
    textBn: 'আমি রাসূলুল্লাহ (ﷺ)-কে বলতে শুনেছি, "সকল কাজের ফলাফল নিয়তের উপর নির্ভরশীল, এবং প্রত্যেক ব্যক্তি তার নিয়ত অনুযায়ী প্রতিফল পাবে।"',
    reference: 'Sahih al-Bukhari 1',
  ),
  Hadith(
    id: 2,
    narrator: 'Abu Huraira',
    textEn: 'The Prophet (ﷺ) said, "Religion is very easy and whoever overburdens himself in his religion will not be able to continue in that way. So you should not be extremists, but try to be near to perfection and receive the good tidings that you will be rewarded."',
    textBn: 'নবী (ﷺ) বলেছেন, "ধর্ম অত্যন্ত সহজ। যে ব্যক্তি ধর্ম নিয়ে বাড়াবাড়ি করবে, সে টিকতে পারবে না। তাই তোমরা মধ্যপন্থা অবলম্বন করো, পূর্ণতার কাছাকাছি থাকার চেষ্টা করো এবং সুসংবাদ গ্রহণ করো।"',
    reference: 'Sahih al-Bukhari 39',
  ),
  Hadith(
    id: 3,
    narrator: 'Abu Mas\'ud Al-Ansari',
    textEn: 'The Prophet (ﷺ) said, "If somebody recites the last two Verses of Surat Al-Baqara at night, that will be sufficient for him."',
    textBn: 'নবী (ﷺ) বলেছেন, "যে ব্যক্তি রাতে সূরা বাকারার শেষ দুটি আয়াত তিলাওয়াত করবে, তা তার জন্য যথেষ্ট হবে।"',
    reference: 'Sahih al-Bukhari 5009',
  ),
  Hadith(
    id: 4,
    narrator: 'Anas bin Malik',
    textEn: 'The Prophet (ﷺ) said, "None of you will have faith till he wishes for his (Muslim) brother what he likes for himself."',
    textBn: 'নবী (ﷺ) বলেছেন, "তোমাদের কেউ ততক্ষণ পর্যন্ত পূর্ণ মুমিন হতে পারবে না, যতক্ষণ না সে তার ভাইয়ের জন্য তা-ই পছন্দ করবে যা সে নিজের জন্য পছন্দ করে।"',
    reference: 'Sahih al-Bukhari 13',
  ),
  Hadith(
    id: 5,
    narrator: 'Abu Huraira',
    textEn: 'The Prophet (ﷺ) said, "A man is upon the religion of his best friend, so let one of you look at whom he befriends."',
    textBn: 'নবী (ﷺ) বলেছেন, "মানুষ তার বন্ধুর দ্বীনের (স্বভাবের) উপর থাকে। সুতরাং তোমাদের প্রত্যেকের দেখা উচিত সে কার সাথে বন্ধুত্ব করছে।"',
    reference: 'Sunan Abi Dawud 4833',
  ),
  Hadith(
    id: 6,
    narrator: 'Aisha',
    textEn: 'Allah\'s Messenger (ﷺ) said, "The most hated person in the sight of Allah is the most quarrelsome person."',
    textBn: 'রাসূলুল্লাহ (ﷺ) বলেছেন, "আল্লাহর কাছে সবচেয়ে ঘৃণিত ব্যক্তি হলো সে, যে সবচেয়ে বেশি ঝগড়াটে।"',
    reference: 'Sahih al-Bukhari 2457',
  ),
  Hadith(
    id: 7,
    narrator: 'Abdullah bin Amr',
    textEn: 'The Prophet (ﷺ) said, "A Muslim is the one who avoids harming Muslims with his tongue and hands."',
    textBn: 'নবী (ﷺ) বলেছেন, "প্রকৃত মুসলিম সে-ই, যার জিহ্বা ও হাত থেকে অন্য মুসলিমরা নিরাপদ থাকে।"',
    reference: 'Sahih al-Bukhari 10',
  ),
  Hadith(
    id: 8,
    narrator: 'Abu Darda',
    textEn: 'The Prophet (ﷺ) said, "Nothing is heavier on the Scale of Deeds than one\'s good manners."',
    textBn: 'নবী (ﷺ) বলেছেন, "কিয়ামতের দিন মুমিনের আমলনামায় উত্তম চরিত্রের চেয়ে ভারী আর কিছুই হবে না।"',
    reference: 'Jami` at-Tirmidhi 2002',
  ),
  Hadith(
    id: 9,
    narrator: 'Abu Huraira',
    textEn: 'The Messenger of Allah (ﷺ) said: "He who does not thank the people is not thankful to Allah."',
    textBn: 'রাসূলুল্লাহ (ﷺ) বলেছেন, "যে ব্যক্তি মানুষের প্রতি কৃতজ্ঞতা প্রকাশ করে না, সে আল্লাহর প্রতিও কৃতজ্ঞ হয় না।"',
    reference: 'Sunan Abi Dawud 4811',
  ),
  Hadith(
    id: 10,
    narrator: 'Uthman bin Affan',
    textEn: 'The Prophet (ﷺ) said, "The best among you (Muslims) are those who learn the Qur\'an and teach it."',
    textBn: 'নবী (ﷺ) বলেছেন, "তোমাদের মধ্যে সর্বোত্তম সে, যে কুরআন শেখে এবং অন্যদের তা শিক্ষা দেয়।"',
    reference: 'Sahih al-Bukhari 5027',
  ),
];
