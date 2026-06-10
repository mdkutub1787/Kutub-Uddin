import 'package:flutter/material.dart';
import '../models/order_model.dart';
import '../repositories/order_repository.dart';

class OrderViewModel extends ChangeNotifier {
  final OrderRepository _repository = OrderRepository();
  List<OrderModel> _userOrders = [];
  List<OrderModel> _allOrders = [];
  bool _isLoading = false;

  List<OrderModel> get userOrders => _userOrders;
  List<OrderModel> get allOrders => _allOrders;
  bool get isLoading => _isLoading;

  void fetchAllOrders() {
    _isLoading = true;
    _repository.getAllOrders().listen((orders) {
      _allOrders = orders;
      _isLoading = false;
      notifyListeners();
    });
  }

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

  Future<void> refreshUserOrders(String userId) async {
    _isLoading = true;
    notifyListeners();
    final stream = _repository.getUserOrders(userId);
    await for (final orders in stream) {
      _userOrders = orders;
      _isLoading = false;
      notifyListeners();
      break;
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
