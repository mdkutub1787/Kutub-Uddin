import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/providers.dart';
import '../../../models/review_model.dart';
import '../repositories/review_repository.dart';

final reviewRepositoryProvider = Provider<ReviewRepository>((ref) {
  return ReviewRepository(ref.watch(supabaseClientProvider));
});

final reviewNotifierProvider = AsyncNotifierProvider.family<ReviewNotifier, List<ReviewModel>, String>(() {
  return ReviewNotifier();
});

class ReviewNotifier extends FamilyAsyncNotifier<List<ReviewModel>, String> {
  late ReviewRepository _repository;

  @override
  FutureOr<List<ReviewModel>> build(String arg) async {
    _repository = ref.watch(reviewRepositoryProvider);
    return await _fetchReviews(arg);
  }

  Future<List<ReviewModel>> _fetchReviews(String productId) async {
    try {
      return await _repository.getProductReviews(productId);
    } catch (e) {
      return [];
    }
  }

  Future<void> submitReview(ReviewModel review) async {
    try {
      await _repository.addReview(review);
      state = AsyncData([review, ...?state.value]);
    } catch (e) {
      rethrow;
    }
  }
}
