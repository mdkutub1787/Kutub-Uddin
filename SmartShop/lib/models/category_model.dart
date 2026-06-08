import 'package:cloud_firestore/cloud_firestore.dart';
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

  factory CategoryModel.fromFirestore(DocumentSnapshot doc) {
    Map<String, dynamic> data = doc.data() as Map<String, dynamic>;
    return CategoryModel(
      id: doc.id,
      name: data['name'] ?? '',
      icon: _getIconData(data['icon'] ?? ''),
      color: Color(data['color'] ?? 0xFF000000),
    );
  }

  static IconData _getIconData(String iconName) {
    switch (iconName) {
      case 'medical_services':
        return Icons.medical_services;
      case 'shopping_basket':
        return Icons.shopping_basket;
      case 'face':
        return Icons.face;
      case 'handyman':
        return Icons.handyman;
      case 'checkroom':
        return Icons.checkroom;
      default:
        return Icons.help_outline;
    }
  }
}
