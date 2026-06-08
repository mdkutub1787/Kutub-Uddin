class StationService {
  static const List<String> stations = [
    'Dhaka', 'Chittagong', 'Sylhet', 'Rajshahi', 'Khulna', 'Barisal', 'Rangpur',
    'Mymensingh', 'Comilla', 'Gazipur', 'Narayanganj', 'Bogra', 'Kushtia',
    'Jessore', 'Dinajpur', 'Tangail', 'Feni', 'Brahmanbaria', 'Noakhali',
    'Chandpur', 'Pabna', 'Sirajganj', 'Jamalpur', 'Netrokona', 'Sherpur',
    'Kishoreganj', 'Munshiganj', 'Manikganj', 'Narsingdi', 'Madaripur',
    'Shariatpur', 'Rajbari', 'Gopalganj', 'Faridpur', 'Magura', 'Narail',
    'Bagerhat', 'Satkhira', 'Meherpur', 'Chuadanga', 'Jhenaidah', 'Nilphamari',
    'Kurigram', 'Lalmonirhat', 'Gaibandha', 'Thakurgaon', 'Panchagarh',
    'Joypurhat', 'Naogaon', 'Natore', 'Chapai Nawabganj', 'Habiganj',
    'Moulvibazar', 'Sunamganj', 'Cox\'s Bazar', 'Bandarban', 'Rangamati',
    'Khagrachari', 'Lakshmipur', 'Bhola', 'Patuakhali', 'Pirojpur', 'Jhalokati', 'Barguna'
  ];

  static List<String> getSuggestions(String query) {
    List<String> matches = [];
    matches.addAll(stations);
    matches.retainWhere((s) => s.toLowerCase().contains(query.toLowerCase()));
    return matches;
  }
}
