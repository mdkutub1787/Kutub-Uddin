class DiaryEntry {
  final String id;
  final String title;
  final String content;
  final String mood;
  final String tag;
  final DateTime date;

  DiaryEntry({
    required this.id,
    required this.title,
    required this.content,
    required this.mood,
    required this.tag,
    required this.date,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'content': content,
      'mood': mood,
      'tag': tag,
      'date': date.toIso8601String(),
    };
  }

  factory DiaryEntry.fromMap(Map<String, dynamic> map) {
    return DiaryEntry(
      id: map['id'] ?? '',
      title: map['title'] ?? '',
      content: map['content'] ?? '',
      mood: map['mood'] ?? 'happy',
      tag: map['tag'] ?? '',
      date: DateTime.parse(map['date']),
    );
  }
}
