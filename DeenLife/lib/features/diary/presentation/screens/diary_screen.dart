import 'package:flutter/material.dart';

class DiaryScreen extends StatelessWidget {
  const DiaryScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F9FA),
      appBar: AppBar(
        title: const Text('ডায়েরি (Diary)', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: const Color(0xFF1B3B2B),
        foregroundColor: Colors.white,
        centerTitle: true,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.menu_book,
              size: 80,
              color: Colors.grey.withAlpha(100),
            ),
            const SizedBox(height: 16),
            const Text(
              'আপনার ব্যক্তিগত ডায়েরি',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.w600,
                color: Color(0xFF1B3B2B),
              ),
            ),
            const SizedBox(height: 12),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 40.0),
              child: Text(
                'খুব শিগগিরই এখানে আপনি আপনার দৈনন্দিন আমল ও হিসাব লিখে রাখতে পারবেন।',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 14,
                  color: Colors.grey,
                ),
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('ডায়েরি লেখার অপশন শীঘ্রই যুক্ত করা হবে!')),
          );
        },
        backgroundColor: const Color(0xFF1B3B2B),
        child: const Icon(Icons.add, color: Colors.white),
      ),
    );
  }
}

