import 'dart:io';

void main() {
  final dir = Directory('lib');
  final files = dir.listSync(recursive: true).whereType<File>().where((f) => f.path.endsWith('.dart'));

  final RegExp regex = RegExp(r'CircularProgressIndicator\([^)]*\)');
  final RegExp constRegex = RegExp(r'const\s+CircularProgressIndicator\([^)]*\)');
  final RegExp defaultRegex = RegExp(r'CircularProgressIndicator\(\)');

  int count = 0;
  for (final file in files) {
    String content = file.readAsStringSync();
    bool changed = false;
    
    if (content.contains('CircularProgressIndicator')) {
      content = content.replaceAll(constRegex, 'const CustomLoading()');
      content = content.replaceAll(regex, 'CustomLoading()');
      content = content.replaceAll('CircularProgressIndicator', 'CustomLoading'); // Catch any bare ones if left
      
      changed = true;
    }
    
    if (changed) {
      // Calculate relative path to lib/widgets/custom_loading.dart
      final parts = file.path.replaceAll('\\\\', '/').split('/');
      final depth = parts.length - 2; 
      String prefix = '';
      if (depth <= 0) {
        prefix = './';
      } else {
        prefix = List.filled(depth, '../').join('');
      }
      
      final importStmt = "import '${prefix}widgets/custom_loading.dart';";
      
      if (!content.contains('custom_loading.dart') && !content.contains('CustomLoading')) {
         // this check might be wrong since we just added CustomLoading, so just check for custom_loading.dart
      }
      
      if (!content.contains('custom_loading.dart')) {
        final importIdx = content.lastIndexOf(RegExp(r'^import .*;', multiLine: true));
        if (importIdx != -1) {
          final endOfImport = content.indexOf(';', importIdx) + 1;
          content = content.substring(0, endOfImport) + '\n' + importStmt + content.substring(endOfImport);
        } else {
          content = importStmt + '\n' + content;
        }
      }

      file.writeAsStringSync(content);
      count++;
      print('Updated ${file.path}');
    }
  }
  print('Replaced in $count files.');
}
