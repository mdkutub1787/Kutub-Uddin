import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';
import 'package:uuid/uuid.dart';
import '../../order/models/order_model.dart';
import '../../product/repositories/review_repository.dart';
import '../../../models/review_model.dart';
import '../../delivery/models/rider_review_model.dart';
import '../../../core/providers.dart';
import '../../auth/riverpod/auth_notifier.dart';


class OrderFeedbackScreen extends ConsumerStatefulWidget {
  final OrderModel order;
  const OrderFeedbackScreen({super.key, required this.order});

  @override
  ConsumerState<OrderFeedbackScreen> createState() => _OrderFeedbackScreenState();
}

class _OrderFeedbackScreenState extends ConsumerState<OrderFeedbackScreen> {
  double _riderRating = 5.0;
  final TextEditingController _riderCommentController = TextEditingController();
  
  // Product Ratings {productId: rating}
  final Map<String, double> _productRatings = {};
  final Map<String, TextEditingController> _productCommentControllers = {};

  bool _isSubmitting = false;

  @override
  void initState() {
    super.initState();
    for (var item in widget.order.items) {
      _productRatings[item.product.id] = 5.0;
      _productCommentControllers[item.product.id] = TextEditingController();
    }
  }

  @override
  void dispose() {
    _riderCommentController.dispose();
    for (var controller in _productCommentControllers.values) {
      controller.dispose();
    }
    super.dispose();
  }

  Future<void> _submitFeedback() async {
    setState(() => _isSubmitting = true);
    final user = ref.read(authNotifierProvider).value;
    if (user == null) return;

    final reviewRepo = ReviewRepository(ref.read(supabaseClientProvider));

    try {
      // 1. Submit Rider Review
      if (widget.order.deliveryManId != null) {
        final riderReview = RiderReviewModel(
          id: const Uuid().v4(),
          riderId: widget.order.deliveryManId!,
          orderId: widget.order.id,
          userId: user.uid,
          userName: user.name,
          userImageUrl: user.imageUrl,
          rating: _riderRating,
          comment: _riderCommentController.text.trim(),
          createdAt: DateTime.now(),
        );
        await reviewRepo.addRiderReview(riderReview.toJson());
      }

      // 2. Submit Product Reviews
      for (var item in widget.order.items) {
        final pId = item.product.id;
        final review = ReviewModel(
          id: const Uuid().v4(),
          productId: pId,
          userId: user.uid,
          userName: user.name,
          userImageUrl: user.imageUrl,
          rating: _productRatings[pId] ?? 5.0,
          comment: _productCommentControllers[pId]?.text.trim() ?? '',
          createdAt: DateTime.now(),
        );
        await reviewRepo.addReview(review);
      }

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Thank you for your feedback!'), backgroundColor: Colors.green));
        Navigator.pop(context);
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Failed to submit: $e'), backgroundColor: Colors.red));
      }
    } finally {
      if (mounted) setState(() => _isSubmitting = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Order Feedback")),
      body: _isSubmitting 
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (widget.order.deliveryManId != null) ...[
                    _buildSectionTitle("Rate your Rider"),
                    _buildRiderReviewCard(),
                    const SizedBox(height: 24),
                  ],
                  _buildSectionTitle("Rate the Products"),
                  ...widget.order.items.map((item) => _buildProductReviewCard(item.product.id, item.product.name, item.product.imageUrl)).toList(),
                  const SizedBox(height: 32),
                  SizedBox(
                    width: double.infinity,
                    height: 50,
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.teal,
                        foregroundColor: Colors.white,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      onPressed: _submitFeedback,
                      child: const Text("Submit Feedback", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    ),
                  ),
                  const SizedBox(height: 32),
                ],
              ),
            ),
    );
  }

  Widget _buildSectionTitle(String title) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16),
      child: Text(title, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
    );
  }

  Widget _buildRiderReviewCard() {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Row(
              children: [
                CircleAvatar(
                  radius: 25,
                  backgroundImage: widget.order.deliveryManImage != null ? NetworkImage(widget.order.deliveryManImage!) : null,
                  child: widget.order.deliveryManImage == null ? const Icon(Icons.person) : null,
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(widget.order.deliveryManName ?? "Rider", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                      const Text("Delivery Partner", style: TextStyle(color: Colors.grey, fontSize: 12)),
                    ],
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            RatingBar.builder(
              initialRating: 5,
              minRating: 1,
              direction: Axis.horizontal,
              allowHalfRating: true,
              itemCount: 5,
              itemPadding: const EdgeInsets.symmetric(horizontal: 4.0),
              itemBuilder: (context, _) => const Icon(Icons.star_rounded, color: Colors.amber),
              onRatingUpdate: (rating) => _riderRating = rating,
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _riderCommentController,
              decoration: InputDecoration(
                hintText: "How was the delivery experience?",
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                filled: true,
                fillColor: Colors.grey[100],
              ),
              maxLines: 2,
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildProductReviewCard(String pId, String pName, String pImage) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Row(
              children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: Image.network(pImage, width: 50, height: 50, fit: BoxFit.cover, errorBuilder: (_,__,___) => const Icon(Icons.image, size: 50)),
                ),
                const SizedBox(width: 16),
                Expanded(child: Text(pName, style: const TextStyle(fontWeight: FontWeight.bold))),
              ],
            ),
            const SizedBox(height: 16),
            RatingBar.builder(
              initialRating: 5,
              minRating: 1,
              direction: Axis.horizontal,
              allowHalfRating: true,
              itemCount: 5,
              itemSize: 30,
              itemPadding: const EdgeInsets.symmetric(horizontal: 2.0),
              itemBuilder: (context, _) => const Icon(Icons.star_rounded, color: Colors.amber),
              onRatingUpdate: (rating) => _productRatings[pId] = rating,
            ),
            const SizedBox(height: 12),
            TextField(
              controller: _productCommentControllers[pId],
              decoration: InputDecoration(
                hintText: "Write a review for this product...",
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                filled: true,
                fillColor: Colors.grey[100],
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12)
              ),
              maxLines: 2,
            ),
          ],
        ),
      ),
    );
  }
}
