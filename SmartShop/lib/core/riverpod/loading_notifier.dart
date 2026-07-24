import 'package:flutter_riverpod/flutter_riverpod.dart';

class LoadingState {
  final bool isLoading;
  final String message;

  LoadingState({
    this.isLoading = false,
    this.message = '',
  });
}

class LoadingNotifier extends Notifier<LoadingState> {
  @override
  LoadingState build() {
    return LoadingState();
  }
}

final loadingProvider = NotifierProvider<LoadingNotifier, LoadingState>(() {
  return LoadingNotifier();
});
