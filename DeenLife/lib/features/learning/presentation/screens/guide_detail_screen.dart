import 'package:flutter/material.dart';
import '../../../../core/data/learning_providers.dart';

class GuideDetailScreen extends StatelessWidget {
  final NamazTopic topic;

  const GuideDetailScreen({super.key, required this.topic});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: Text(topic.title, style: const TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: const Color(0xFF1E3A5F),
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Container(
          width: double.infinity,
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
            boxShadow: [
              BoxShadow(color: Colors.black.withAlpha(5), blurRadius: 10),
            ],
          ),
          child: Text(
            topic.content,
            style: const TextStyle(
              fontSize: 18,
              height: 1.6,
              color: Colors.black87,
            ),
          ),
        ),
      ),
    );
  }
}

