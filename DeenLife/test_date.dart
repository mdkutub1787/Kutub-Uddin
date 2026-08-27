import 'package:intl/intl.dart';

void main() {
  String iqamahStr = "1:30 PM";
  DateTime parsedTime = DateFormat("h:mm a").parse(iqamahStr);
  print(parsedTime);
  print("Hour: ${parsedTime.hour}, Minute: ${parsedTime.minute}");
}
