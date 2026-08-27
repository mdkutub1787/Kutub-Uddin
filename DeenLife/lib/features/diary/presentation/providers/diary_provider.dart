import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/models/diary_entry.dart';

final diaryProvider = StateNotifierProvider<DiaryNotifier, List<DiaryEntry>>((ref) {
  return DiaryNotifier();
});

class DiaryNotifier extends StateNotifier<List<DiaryEntry>> {
  DiaryNotifier() : super(_initialData);

  static final List<DiaryEntry> _initialData = [
    DiaryEntry(
      id: '1',
      title: 'আজকের আমল',
      content: 'আলহামদুলিল্লাহ, আজ ফজর জামাতে পড়তে পেরেছি। কোরআন তেলাওয়াত মনকে খুব প্রশান্তি দিয়েছে।',
      mood: 'happy',
      tag: 'আমল',
      date: DateTime.now().subtract(const Duration(days: 0)),
    ),
    DiaryEntry(
      id: '2',
      title: 'শুকরিয়া আদায়',
      content: 'আজকের দিনটা বেশ ব্যস্ততায় কেটেছে, তবে আল্লাহর রহমতে সব কাজ ঠিকঠাক শেষ করতে পেরেছি।',
      mood: 'happy',
      tag: 'কৃতজ্ঞতা',
      date: DateTime.now().subtract(const Duration(days: 1)),
    ),
    DiaryEntry(
      id: '3',
      title: 'নিজেকে শুধরানোর চেষ্টা',
      content: 'আজ একটু রেগে গিয়েছিলাম। নবীজির (সা.) ধৈর্য্যের কথা মনে করে নিজেকে সামলে নিই।',
      mood: 'neutral',
      tag: 'আত্মশুদ্ধি',
      date: DateTime.now().subtract(const Duration(days: 2)),
    ),
  ];

  void addEntry(DiaryEntry entry) {
    state = [entry, ...state];
  }

  void deleteEntry(String id) {
    state = state.where((e) => e.id != id).toList();
  }
}
