import 'package:equatable/equatable.dart';

class ResendCodeRequest extends Equatable {
  final String? type;
  final String? token;
  final String? email;

  const ResendCodeRequest({this.type, this.token, this.email});

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['type'] = type;
    if (email != null) {
      data['email'] = email;
    }
    return data;
  }

  @override
  List<Object?> get props => [type, token, email];
}

class ResendCodeResponse extends Equatable {
  final String? status;
  final String? message;

  const ResendCodeResponse({this.status, this.message});

  factory ResendCodeResponse.fromJson(Map<String, dynamic> json) {
    return ResendCodeResponse(
      status: json['status'],
      message: json['message'],
    );
  }

  @override
  List<Object?> get props => [status, message];
}
