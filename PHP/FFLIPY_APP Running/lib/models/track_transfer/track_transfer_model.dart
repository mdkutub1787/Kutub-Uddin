class TrackTransferResponse {
  bool? success;
  String? message;
  Data? data;

  TrackTransferResponse({this.success, this.message, this.data});

  TrackTransferResponse.fromJson(Map<String, dynamic> json) {
    success = json['success'];
    message = json['message'];
    data = json['data'] != null ? Data.fromJson(json['data']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['success'] = this.success;
    data['message'] = this.message;
    if (this.data != null) {
      data['data'] = this.data!.toJson();
    }
    return data;
  }
}

class Data {
  String? refNo;
  String? amount;
  dynamic currency;
  String? paymentStatus;
  dynamic statusTitleEn;
  dynamic statusTitleEs;
  String? senderName;
  List<TrackerLogs>? trackerLogs;

  Data(
      {this.refNo,
      this.amount,
      this.currency,
      this.paymentStatus,
      this.statusTitleEn,
      this.statusTitleEs,
      this.senderName,
      this.trackerLogs});

  Data.fromJson(Map<String, dynamic> json) {
    refNo = json['ref_no'];
    amount = json['amount'];
    currency = json['currency'];
    paymentStatus = json['payment_status'];
    statusTitleEn = json['status_title_en'];
    statusTitleEs = json['status_title_es'];
    senderName = json['sender_name'];
    if (json['tracker_logs'] != null) {
      trackerLogs = <TrackerLogs>[];
      json['tracker_logs'].forEach((v) {
        trackerLogs!.add(TrackerLogs.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ref_no'] = this.refNo;
    data['amount'] = this.amount;
    data['currency'] = this.currency;
    data['payment_status'] = this.paymentStatus;
    data['status_title_en'] = this.statusTitleEn;
    data['status_title_es'] = this.statusTitleEs;
    data['sender_name'] = this.senderName;
    if (this.trackerLogs != null) {
      data['tracker_logs'] = this.trackerLogs!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class TrackerLogs {
  String? statusId;
  String? operationNameEn;
  String? operationNameEs;
  String? icon;
  String? createdAt;

  TrackerLogs(
      {this.statusId,
      this.operationNameEn,
      this.operationNameEs,
      this.icon,
      this.createdAt});

  TrackerLogs.fromJson(Map<String, dynamic> json) {
    statusId = json['status_id'];
    operationNameEn = json['operation_name_en'];
    operationNameEs = json['operation_name_es'];
    icon = json['icon'];
    createdAt = json['created_at'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status_id'] = this.statusId;
    data['operation_name_en'] = this.operationNameEn;
    data['operation_name_es'] = this.operationNameEs;
    data['icon'] = this.icon;
    data['created_at'] = this.createdAt;
    return data;
  }
}
