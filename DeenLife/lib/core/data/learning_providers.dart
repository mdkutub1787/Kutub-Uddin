import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class NamazTopic {
  final String id;
  final String title;
  final String content;

  NamazTopic({required this.id, required this.title, required this.content});

  factory NamazTopic.fromJson(Map<String, dynamic> json) {
    return NamazTopic(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      content: json['content'] ?? '',
    );
  }
}

class NamazCategory {
  final String id;
  final String title;
  final String icon;
  final List<NamazTopic> topics;

  NamazCategory({
    required this.id,
    required this.title,
    required this.icon,
    required this.topics,
  });

  factory NamazCategory.fromJson(Map<String, dynamic> json) {
    return NamazCategory(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      icon: json['icon'] ?? '',
      topics: (json['topics'] as List)
          .map((e) => NamazTopic.fromJson(e))
          .toList(),
    );
  }
}

class IslamicBook {
  final String id;
  final String title;
  final String author;
  final String category;
  final String description;
  final String content;

  IslamicBook({
    required this.id,
    required this.title,
    required this.author,
    required this.category,
    required this.description,
    required this.content,
  });

  factory IslamicBook.fromJson(Map<String, dynamic> json) {
    return IslamicBook(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      author: json['author'] ?? '',
      category: json['category'] ?? '',
      description: json['description'] ?? '',
      content: json['content'] ?? '',
    );
  }
}

// Providers
final namazShikkhaProvider = FutureProvider<List<NamazCategory>>((ref) async {
  final String response = await rootBundle.loadString('assets/data/namaz_shikkha.json');
  final data = await json.decode(response) as List;
  return data.map((e) => NamazCategory.fromJson(e)).toList();
});

final islamicLibraryProvider = FutureProvider<List<IslamicBook>>((ref) async {
  final String response = await rootBundle.loadString('assets/data/islamic_library.json');
  final data = await json.decode(response) as List;
  return data.map((e) => IslamicBook.fromJson(e)).toList();
});
