import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/category_model.dart';

class CategoryRepository {
  final CollectionReference _categoriesRef =
      FirebaseFirestore.instance.collection('categories');

  Stream<List<CategoryModel>> getCategories() {
    return _categoriesRef.snapshots().map((snapshot) {
      return snapshot.docs.map((doc) => CategoryModel.fromFirestore(doc)).toList();
    });
  }
}
