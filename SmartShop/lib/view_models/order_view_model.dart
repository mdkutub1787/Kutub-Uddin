import 'package:flutter/material.dart';
import '../models/order_model.dart';
import '../repositories/order_repository.dart';

class OrderViewModel extends ChangeNotifier {
  final OrderRepository _repository = OrderRepository();
  List<OrderModel> _userOrders = [];
  bool _isLoading = false;

  List<OrderModel> get userOrders => _userOrders;
  bool get isLoading => _isLoading;

  Future<bool> placeOrder(OrderModel order) async {
    _isLoading = true;
    notifyListeners();
    try {
      await _repository.placeOrder(order);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  void fetchUserOrders(String userId) {
    _isLoading = true;
    _repository.getUserOrders(userId).listen((orders) {
      _userOrders = orders;
      _isLoading = false;
      notifyListeners();
    });
  }
}
