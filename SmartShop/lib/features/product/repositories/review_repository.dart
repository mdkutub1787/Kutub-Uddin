import 'package:supabase_flutter/supabase_flutter.dart';
import '../../../core/constants/constants.dart';
import '../../../models/review_model.dart';

class ReviewRepository {
  final SupabaseClient _supabase;

  ReviewRepository(this._supabase);

  Future<List<ReviewModel>> getProductReviews(String productId) async {
    final response = await _supabase
        .from(AppConstants.reviewsTable)
        .select()
        .eq('productId', productId)
        .order('created_at', ascending: false);
    
    return (response as List).map((json) => ReviewModel.fromJson(json)).toList();
  }

  Future<void> addReview(ReviewModel review) async {
    await _supabase.from(AppConstants.reviewsTable).insert(review.toJson());
    
    // Update product average rating logic can be added here
  }

  Future<void> addRiderReview(Map<String, dynamic> riderReviewJson) async {
    await _supabase.from(AppConstants.riderReviewsTable).insert(riderReviewJson);
  }
}
