import 'dart:convert';

InvoiceModel invoiceModelFromJson(String str) => InvoiceModel.fromJson(json.decode(str));

String invoiceModelToJson(InvoiceModel data) => json.encode(data.toJson());

class InvoiceModel {
    InvoiceModel({
        this.status,
        this.data,
    });

    String? status;
    Data? data;

    factory InvoiceModel.fromJson(Map<String, dynamic> json) => InvoiceModel(
        status: json["status"],
        data: json["data"] == null ? null : Data.fromJson(json["data"]),
    );

    Map<String, dynamic> toJson() => {
        "status": status,
        "data": data?.toJson(),
    };
}

class Data {
    Data({
        this.contact,
        this.invoice,
        this.transactionType,
    });

    Contact? contact;
    Invoice? invoice;
    String? transactionType;

    factory Data.fromJson(Map<String, dynamic> json) => Data(
        contact: json["contact"] == null ? null : Contact.fromJson(json["contact"]),
        invoice: json["invoice"] == null ? null : Invoice.fromJson(json["invoice"]),
        transactionType: json["transactionType"],
    );

    Map<String, dynamic> toJson() => {
        "contact": contact?.toJson(),
        "invoice": invoice?.toJson(),
        "transactionType": transactionType,
    };
}

class Contact {
    Contact({
        this.email,
        this.phone,
        this.address,
    });

    String? email;
    String? phone;
    String? address;

    factory Contact.fromJson(Map<String, dynamic> json) => Contact(
        email: json["email"],
        phone: json["phone"],
        address: json["address"],
    );

    Map<String, dynamic> toJson() => {
        "email": email,
        "phone": phone,
        "address": address,
    };
}

class Invoice {
    Invoice({
        this.transaction,
        this.status,
        this.transactionDate,
        this.service,
        this.serviceProvider,
        this.sendAmount,
        this.fees,
        this.discountYes,
        this.discount,
        this.totalSendAmount,
        this.recipientAmount,
        this.rate,
        this.sender,
        this.fundingSource,
        this.sendingPurpose,
        this.recipient,
    });

    String? transaction;
    String? status;
    String? transactionDate;
    String? service;
    String? serviceProvider;
    String? sendAmount;
    String? fees;
    int? discountYes;
    String? discount;
    String? totalSendAmount;
    String? recipientAmount;
    String? rate;
    Sender? sender;
    String? fundingSource;
    String? sendingPurpose;
    Recipient? recipient;

    factory Invoice.fromJson(Map<String, dynamic> json) => Invoice(
        transaction: json["Transaction"],
        status: json["status"],
        transactionDate: json["TransactionDate"],
        service: json["Service"],
        serviceProvider: json["ServiceProvider"],
        sendAmount: json["SendAmount"],
        fees: json["Fees"],
        discountYes: json["discountYes"],
        discount: json["Discount"],
        totalSendAmount: json["TotalSendAmount"],
        recipientAmount: json["RecipientAmount"],
        rate: json["Rate"],
        sender: json["Sender"] == null ? null : Sender.fromJson(json["Sender"]),
        fundingSource: json["FundingSource"],
        sendingPurpose: json["SendingPurpose"],
        recipient: json["Recipient"] == null ? null : Recipient.fromJson(json["Recipient"]),
    );

    Map<String, dynamic> toJson() => {
        "Transaction": transaction,
        "status": status,
        "TransactionDate": transactionDate,
        "Service": service,
        "ServiceProvider": serviceProvider,
        "SendAmount": sendAmount,
        "Fees": fees,
        "discountYes": discountYes,
        "Discount": discount,
        "TotalSendAmount": totalSendAmount,
        "RecipientAmount": recipientAmount,
        "Rate": rate,
        "Sender": sender?.toJson(),
        "FundingSource": fundingSource,
        "SendingPurpose": sendingPurpose,
        "Recipient": recipient?.toJson(),
    };
}

class Recipient {
    Recipient({
        this.name,
        this.email,
        this.phone,
        this.accountNumber,
    });

    String? name;
    String? email;
    String? phone;
    String? accountNumber;

    factory Recipient.fromJson(Map<String, dynamic> json) => Recipient(
        name: json["Name"],
        email: json["Email"],
        phone: json["Phone"],
        accountNumber: json["AccountNumber"],
    );

    Map<String, dynamic> toJson() => {
        "Name": name,
        "Email": email,
        "Phone": phone,
        "AccountNumber": accountNumber,
    };
}

class Sender {
    Sender({
        this.name,
        this.phone,
        this.address,
        this.city,
        this.postCode,
        this.country,
    });

    String? name;
    String? phone;
    String? address;
    String? city;
    String? postCode;
    String? country;

    factory Sender.fromJson(Map<String, dynamic> json) => Sender(
        name: json["Name"],
        phone: json["Phone"],
        address: json["Address"],
        city: json["City"],
        postCode: json["PostCode"],
        country: json["Country"],
    );

    Map<String, dynamic> toJson() => {
        "Name": name,
        "Phone": phone,
        "Address": address,
        "City": city,
        "PostCode": postCode,
        "Country": country,
    };
}
