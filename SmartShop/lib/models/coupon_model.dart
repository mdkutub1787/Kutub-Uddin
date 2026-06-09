import 'package:flutter/material.dart';

enum CouponType { percentage, fixedAmount, freeDelivery }

class CouponModel {
  final String id;
  final String code;
  final String title;
  final String description;
  final double discountValue;
  final CouponType type;
  final double minPurchase;
  final DateTime expiryDate;
  final bool isActive;

  CouponModel({
    required this.id,
    required this.code,
    required this.title,
    required this.description,
    required this.discountValue,
    required this.type,
    required this.minPurchase,
    required this.expiryDate,
    this.isActive = true,
  });

  bool get isExpired => DateTime.now().isAfter(expiryDate);

  factory CouponModel.fromMap(Map<String, dynamic> map, String id) {
    return CouponModel(
      id: id,
      code: map['code'] ?? '',
      title: map['title'] ?? '',
      description: map['description'] ?? '',
      discountValue: (map['discountValue'] ?? 0).toDouble(),
      type: CouponType.values.firstWhere(
        (e) => e.toString() == 'CouponType.${map['type']}',
        orElse: () => CouponType.percentage,
      ),
      minPurchase: (map['minPurchase'] ?? 0).toDouble(),
      expiryDate: (map['expiryDate'] as DateTime),
      isActive: map['isActive'] ?? true,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'code': code,
      'title': title,
      'description': description,
      'discountValue': discountValue,
      'type': type.toString().split('.').last,
      'minPurchase': minPurchase,
      'expiryDate': expiryDate,
      'isActive': isActive,
    };
  }
}
