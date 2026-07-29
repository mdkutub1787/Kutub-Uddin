import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../repositories/review_repository.dart';
import '../../../models/review_model.dart';
import '../../../core/providers.dart';

final reviewRepositoryProvider = Provider<ReviewRepository>((ref) {
  return ReviewRepository(ref.watch(supabaseClientProvider));
});

final productReviewsProvider = FutureProvider.family<List<ReviewModel>, String>((ref, productId) async {
  final repository = ref.watch(reviewRepositoryProvider);
  return repository.getProductReviews(productId);
});
