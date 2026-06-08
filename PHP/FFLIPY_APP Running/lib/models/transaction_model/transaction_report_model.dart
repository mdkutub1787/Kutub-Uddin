class TransactionReportResponse {
  final bool success;
  final String message;
  final TransactionReportData data;

  TransactionReportResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory TransactionReportResponse.fromJson(Map<String, dynamic> json) {
    return TransactionReportResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? '',
      data: TransactionReportData.fromJson(json['data']),
    );
  }

  TransactionReportResponse copyWith({
    bool? success,
    String? message,
    TransactionReportData? data,
  }) {
    return TransactionReportResponse(
      success: success ?? this.success,
      message: message ?? this.message,
      data: data ?? this.data,
    );
  }
}

class TransactionReportData {
  final TransactionPagination transactions;
  final List<Country> countries;
  final List<Purpose> sendingPurposes;
  final List<Relationship> relationships;

  TransactionReportData({
    required this.transactions,
    required this.countries,
    required this.sendingPurposes,
    required this.relationships,
  });

  factory TransactionReportData.fromJson(Map<String, dynamic> json) {
    return TransactionReportData(
      transactions: TransactionPagination.fromJson(json['transactions']),
      countries: (json['countries'] as List?)
              ?.map((e) => Country.fromJson(e))
              .toList() ??
          [],
      sendingPurposes: (json['sending_purposes'] as List?)
              ?.map((e) => Purpose.fromJson(e))
              .toList() ??
          [],
      relationships: (json['relationships'] as List?)
              ?.map((e) => Relationship.fromJson(e))
              .toList() ??
          [],
    );
  }

  TransactionReportData copyWith({
    TransactionPagination? transactions,
    List<Country>? countries,
    List<Purpose>? sendingPurposes,
    List<Relationship>? relationships,
  }) {
    return TransactionReportData(
      transactions: transactions ?? this.transactions,
      countries: countries ?? this.countries,
      sendingPurposes: sendingPurposes ?? this.sendingPurposes,
      relationships: relationships ?? this.relationships,
    );
  }
}

class TransactionPagination {
  final int currentPage;
  final List<TransactionModel> data;
  final int lastPage;
  final int perPage;
  final int total;
  final String? firstPageUrl;
  final String? lastPageUrl;
  final String? nextPageUrl;
  final String? prevPageUrl;
  final String? path;
  final int? from;
  final int? to;

  TransactionPagination({
    required this.currentPage,
    required this.data,
    required this.lastPage,
    required this.perPage,
    required this.total,
    this.firstPageUrl,
    this.lastPageUrl,
    this.nextPageUrl,
    this.prevPageUrl,
    this.path,
    this.from,
    this.to,
  });

  factory TransactionPagination.fromJson(Map<String, dynamic> json) {
    return TransactionPagination(
      currentPage: json['current_page'] ?? 1,
      data: (json['data'] as List?)
              ?.map((e) => TransactionModel.fromJson(e))
              .toList() ??
          [],
      lastPage: json['last_page'] ?? 1,
      perPage: json['per_page'] ?? 10,
      total: json['total'] ?? 0,
      firstPageUrl: json['first_page_url'],
      lastPageUrl: json['last_page_url'],
      nextPageUrl: json['next_page_url'],
      prevPageUrl: json['prev_page_url'],
      path: json['path'],
      from: json['from'],
      to: json['to'],
    );
  }

  TransactionPagination copyWith({
    int? currentPage,
    List<TransactionModel>? data,
    int? lastPage,
    int? perPage,
    int? total,
    String? firstPageUrl,
    String? lastPageUrl,
    String? nextPageUrl,
    String? prevPageUrl,
    String? path,
    int? from,
    int? to,
  }) {
    return TransactionPagination(
      currentPage: currentPage ?? this.currentPage,
      data: data ?? this.data,
      lastPage: lastPage ?? this.lastPage,
      perPage: perPage ?? this.perPage,
      total: total ?? this.total,
      firstPageUrl: firstPageUrl ?? this.firstPageUrl,
      lastPageUrl: lastPageUrl ?? this.lastPageUrl,
      nextPageUrl: nextPageUrl ?? this.nextPageUrl,
      prevPageUrl: prevPageUrl ?? this.prevPageUrl,
      path: path ?? this.path,
      from: from ?? this.from,
      to: to ?? this.to,
    );
  }
}

class TransactionModel {
  final int id;
  final String recipientName;
  final String recipientBank;
  final String recipientAccountNo;
  final String refNo;
  final String? txPaidDate;
  final String? createdAt;
  final String status;
  final String paymentStatus;
  final String payableAmount;
  final String sendCurr;
  final String fees;
  final String sendAmount;
  final String recipientGetAmount;
  final String receiveCurr;
  final String invoice;
  final num totalPay;
  final num totalBaseAmountPay;
  final num totalBaseAmountChargePay;
  final num refundableAmount;

  TransactionModel({
    required this.id,
    required this.recipientName,
    required this.recipientBank,
    required this.recipientAccountNo,
    required this.refNo,
    this.txPaidDate,
    this.createdAt,
    required this.status,
    required this.paymentStatus,
    required this.payableAmount,
    required this.sendCurr,
    required this.fees,
    required this.sendAmount,
    required this.recipientGetAmount,
    required this.receiveCurr,
    required this.invoice,
    required this.totalPay,
    required this.totalBaseAmountPay,
    required this.totalBaseAmountChargePay,
    required this.refundableAmount,
  });

  factory TransactionModel.fromJson(Map<String, dynamic> json) {
    return TransactionModel(
      id: json['id'],
      recipientName: json['recipient_name'] ?? '',
      recipientBank: json['recipient_bank'] ?? '',
      recipientAccountNo: json['recipient_account_no'] ?? '',
      refNo: json['ref_no'] ?? '',
      txPaidDate: json['tx_paid_date'],
      createdAt: json['created_at'],
      status: json['status']?.toString() ?? '0',
      paymentStatus: json['payment_status']?.toString() ?? '0',
      payableAmount: json['payable_amount']?.toString() ?? '0.00',
      sendCurr: json['send_curr'] ?? '',
      fees: json['fees']?.toString() ?? '0.00',
      sendAmount: json['send_amount']?.toString() ?? '0.00',
      recipientGetAmount: json['recipient_get_amount']?.toString() ?? '0.00',
      receiveCurr: json['receive_curr'] ?? '',
      invoice: json['invoice']?.toString() ?? '0',
      totalPay: json['totalPay'] ?? 0,
      totalBaseAmountPay: json['totalBaseAmountPay'] ?? 0,
      totalBaseAmountChargePay: json['totalBaseAmountChargePay'] ?? 0,
      refundableAmount: json['refundable_amount'] ?? 0,
    );
  }
}

class Country {
  final int id;
  final String name;
  final String isoCode;
  final String slug;
  final String code;
  final String minimumAmount;
  final String maximumAmount;
  final String image;
  final String continentId;
  final List<Facility> facilities;
  final String rate;
  final String status;
  final String sendFrom;
  final String sendTo;
  final String details;
  final String createdAt;
  final String updatedAt;
  final String flag;

  Country({
    required this.id,
    required this.name,
    required this.isoCode,
    required this.slug,
    required this.code,
    required this.minimumAmount,
    required this.maximumAmount,
    required this.image,
    required this.continentId,
    required this.facilities,
    required this.rate,
    required this.status,
    required this.sendFrom,
    required this.sendTo,
    required this.details,
    required this.createdAt,
    required this.updatedAt,
    required this.flag,
  });

  factory Country.fromJson(Map<String, dynamic> json) {
    return Country(
      id: json['id'],
      name: json['name'] ?? '',
      isoCode: json['iso_code'] ?? '',
      slug: json['slug'] ?? '',
      code: json['code'] ?? '',
      minimumAmount: json['minimum_amount']?.toString() ?? '0.00',
      maximumAmount: json['maximum_amount']?.toString() ?? '0.00',
      image: json['image'] ?? '',
      continentId: json['continent_id']?.toString() ?? '',
      facilities: (json['facilities'] as List?)
              ?.map((e) => Facility.fromJson(e))
              .toList() ??
          [],
      rate: json['rate']?.toString() ?? '0',
      status: json['status']?.toString() ?? '0',
      sendFrom: json['send_from']?.toString() ?? '0',
      sendTo: json['send_to']?.toString() ?? '0',
      details: json['details'] ?? '',
      createdAt: json['created_at'] ?? '',
      updatedAt: json['updated_at'] ?? '',
      flag: json['flag'] ?? '',
    );
  }
}

class Facility {
  final int id;
  final String name;

  Facility({
    required this.id,
    required this.name,
  });

  factory Facility.fromJson(Map<String, dynamic> json) {
    return Facility(
      id: json['id'],
      name: json['name'] ?? '',
    );
  }
}

class Purpose {
  final String title;

  Purpose({required this.title});

  factory Purpose.fromJson(Map<String, dynamic> json) {
    return Purpose(title: json['title'] ?? '');
  }
}

class Relationship {
  final int id;
  final String title;

  Relationship({required this.id, required this.title});

  factory Relationship.fromJson(Map<String, dynamic> json) {
    return Relationship(
      id: json['id'],
      title: json['title'] ?? '',
    );
  }
}
