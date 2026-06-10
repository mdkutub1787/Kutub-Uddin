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
      icon: _getIconData(data['icon'] ?? 'category'),
      color: Color(data['color'] ?? 0xFF1A237E),
    );
  }

  static IconData _getIconData(String iconName) {
    final Map<String, IconData> iconMap = {
      'shopping_bag': Icons.shopping_bag,
      'laptop': Icons.laptop,
      'face': Icons.face,
      'home': Icons.home,
      'fastfood': Icons.fastfood,
      'electrical_services': Icons.electrical_services,
      'watch': Icons.watch,
      'directions_car': Icons.directions_car,
      'medical_services': Icons.medical_services,
      'checkroom': Icons.checkroom,
      'smartphone': Icons.smartphone,
      'tv': Icons.tv,
      'restaurant': Icons.restaurant,
      'local_pizza': Icons.local_pizza,
      'icecream': Icons.icecream,
      'chair': Icons.chair,
      'bed': Icons.bed,
      'kitchen': Icons.kitchen,
      'health_and_safety': Icons.health_and_safety,
      'medication': Icons.medication,
      'sports_basketball': Icons.sports_basketball,
      'brush': Icons.brush,
      'toys': Icons.toys,
      'local_grocery_store': Icons.local_grocery_store,
      'build': Icons.build,
      'child_care': Icons.child_care,
      'book': Icons.book,
      'pets': Icons.pets,
      'dry_cleaning': Icons.dry_cleaning,
      'style': Icons.style,
      'category': Icons.category,
      'electric_bike': Icons.electric_bike,
      'wine_bar': Icons.wine_bar,
      'liquor': Icons.liquor,
      'bakery_dining': Icons.bakery_dining,
      'breakfast_dining': Icons.breakfast_dining,
      'dinner_dining': Icons.dinner_dining,
      'ice_skating': Icons.ice_skating,
      'fitness_center': Icons.fitness_center,
      'auto_awesome': Icons.auto_awesome,
      'palette': Icons.palette,
      'videogame_asset': Icons.videogame_asset,
      'memory': Icons.memory,
      'mouse': Icons.mouse,
      'keyboard': Icons.keyboard,
      'print': Icons.print,
      'camera_alt': Icons.camera_alt,
      'headphones': Icons.headphones,
      'speaker': Icons.speaker,
    };
    return iconMap[iconName] ?? Icons.category;
  }

  static String getIconName(IconData icon) {
    if (icon == Icons.shopping_bag) return 'shopping_bag';
    if (icon == Icons.laptop) return 'laptop';
    if (icon == Icons.face) return 'face';
    if (icon == Icons.home) return 'home';
    if (icon == Icons.fastfood) return 'fastfood';
    if (icon == Icons.electrical_services) return 'electrical_services';
    if (icon == Icons.watch) return 'watch';
    if (icon == Icons.directions_car) return 'directions_car';
    if (icon == Icons.medical_services) return 'medical_services';
    if (icon == Icons.checkroom) return 'checkroom';
    if (icon == Icons.smartphone) return 'smartphone';
    if (icon == Icons.tv) return 'tv';
    if (icon == Icons.restaurant) return 'restaurant';
    if (icon == Icons.local_pizza) return 'local_pizza';
    if (icon == Icons.icecream) return 'icecream';
    if (icon == Icons.chair) return 'chair';
    if (icon == Icons.bed) return 'bed';
    if (icon == Icons.kitchen) return 'kitchen';
    if (icon == Icons.health_and_safety) return 'health_and_safety';
    if (icon == Icons.medication) return 'medication';
    if (icon == Icons.sports_basketball) return 'sports_basketball';
    if (icon == Icons.fitness_center) return 'fitness_center';
    if (icon == Icons.brush) return 'brush';
    if (icon == Icons.toys) return 'toys';
    if (icon == Icons.local_grocery_store) return 'local_grocery_store';
    if (icon == Icons.build) return 'build';
    if (icon == Icons.child_care) return 'child_care';
    if (icon == Icons.book) return 'book';
    if (icon == Icons.pets) return 'pets';
    if (icon == Icons.dry_cleaning) return 'dry_cleaning';
    if (icon == Icons.style) return 'style';
    if (icon == Icons.electric_bike) return 'electric_bike';
    if (icon == Icons.wine_bar) return 'wine_bar';
    if (icon == Icons.liquor) return 'liquor';
    if (icon == Icons.bakery_dining) return 'bakery_dining';
    if (icon == Icons.breakfast_dining) return 'breakfast_dining';
    if (icon == Icons.dinner_dining) return 'dinner_dining';
    if (icon == Icons.ice_skating) return 'ice_skating';
    if (icon == Icons.auto_awesome) return 'auto_awesome';
    if (icon == Icons.palette) return 'palette';
    if (icon == Icons.videogame_asset) return 'videogame_asset';
    if (icon == Icons.memory) return 'memory';
    if (icon == Icons.mouse) return 'mouse';
    if (icon == Icons.keyboard) return 'keyboard';
    if (icon == Icons.print) return 'print';
    if (icon == Icons.camera_alt) return 'camera_alt';
    if (icon == Icons.headphones) return 'headphones';
    if (icon == Icons.speaker) return 'speaker';
    return 'category';
  }
}
