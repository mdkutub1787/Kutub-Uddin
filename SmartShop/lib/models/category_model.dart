import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';

class CategoryModel {
  final String id;
  final String name;
  final IconData icon;
  final Color color;

  CategoryModel({
    required this.id,
    required this.name,
    required this.icon,
    required this.color,
  });

  factory CategoryModel.fromSnapshot(DataSnapshot snapshot) {
    Map<dynamic, dynamic> data = snapshot.value as Map<dynamic, dynamic>;
    return CategoryModel(
      id: snapshot.key ?? '',
      name: data['name'] ?? '',
      icon: _getIconData(data['icon'] ?? ''),
      color: Color(data['color'] ?? 0xFF1A237E),
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'icon': icon.codePoint.toString(), // Simplified for example
      'color': color.value,
    };
  }

  static IconData _getIconData(dynamic iconData) {
    // If it's a string name or a codePoint, handle accordingly
    return Icons.category; // Defaulting to category icon for now
  }
}
