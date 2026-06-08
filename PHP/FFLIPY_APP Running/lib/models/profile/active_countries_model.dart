class ActiveCountriesResponse {
    final bool success;
    final int status;
    final String message;
    final List<Country> data;

    ActiveCountriesResponse({
        required this.success,
        required this.status,
        required this.message,
        required this.data,
    });

    factory ActiveCountriesResponse.fromJson(Map<String, dynamic> json) => ActiveCountriesResponse(
        success: json["success"],
        status: json["status"],
        message: json["message"],
        data: List<Country>.from(json["data"].map((x) => Country.fromJson(x))),
    );
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
    final DateTime createdAt;
    final DateTime updatedAt;
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

    factory Country.fromJson(Map<String, dynamic> json) => Country(
        id: json["id"],
        name: json["name"],
        isoCode: json["iso_code"],
        slug: json["slug"],
        code: json["code"],
        minimumAmount: json["minimum_amount"],
        maximumAmount: json["maximum_amount"],
        image: json["image"],
        continentId: json["continent_id"],
        facilities: List<Facility>.from(json["facilities"].map((x) => Facility.fromJson(x))),
        rate: json["rate"],
        status: json["status"],
        sendFrom: json["send_from"],
        sendTo: json["send_to"],
        details: json["details"],
        createdAt: DateTime.parse(json["created_at"]),
        updatedAt: DateTime.parse(json["updated_at"]),
        flag: json["flag"],
    );
}

class Facility {
    final int id;
    final String name;

    Facility({
        required this.id,
        required this.name,
    });

    factory Facility.fromJson(Map<String, dynamic> json) => Facility(
        id: json["id"],
        name: json["name"],
    );
}
