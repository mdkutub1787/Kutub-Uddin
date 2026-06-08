class CurrencyRate {
  final int id;
  final String name;
  final String slug;
  final String code;
  final String minimumAmount;
  final double rate;
  final String image;
  final String flag;

  CurrencyRate({
    required this.id,
    required this.name,
    required this.slug,
    required this.code,
    required this.minimumAmount,
    required this.rate,
    required this.image,
    required this.flag,
  });

  factory CurrencyRate.fromJson(Map<String, dynamic> json) {
    return CurrencyRate(
      id: json['id'],
      name: json['name'],
      slug: json['slug'],
      code: json['code'],
      minimumAmount: json['minimum_amount'],
      rate: double.tryParse(json['rate'].toString()) ?? 0.0,
      image: json['image'],
      flag: json['flag'],
    );
  }
}
