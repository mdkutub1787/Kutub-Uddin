class MailVerifyModel {
  final String? code;
  final String? token;

  MailVerifyModel({this.code, this.token});

  Map<String, dynamic> toJson() {
    return {
      'code': code,
    };
  }
}