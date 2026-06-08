class EmailNotVerifiedException implements Exception {
  final String token;
  final String email;
  EmailNotVerifiedException({required this.token, required this.email});
}
