abstract class Failure {
  final String message;
  Failure(this.message);

  @override
  String toString() => message;
}

class ServerFailure extends Failure {
  ServerFailure([String message = 'A server error occurred. Please try again later.']) : super(message);
}

class NetworkFailure extends Failure {
  NetworkFailure([String message = 'No internet connection. Please check your network.']) : super(message);
}

class AuthFailure extends Failure {
  AuthFailure(super.message);
}

class DatabaseFailure extends Failure {
  DatabaseFailure([String message = 'Database operation failed.']) : super(message);
}
