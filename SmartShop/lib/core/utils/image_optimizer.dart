import 'dart:io';
import 'package:flutter_image_compress/flutter_image_compress.dart';
import 'package:path_provider/path_provider.dart';

class ImageOptimizer {
  static Future<File?> compressImage(File file) async {
    try {
      final tempDir = await getTemporaryDirectory();
      final targetPath = '${tempDir.path}/${DateTime.now().millisecondsSinceEpoch}.jpg';

      final result = await FlutterImageCompress.compressAndGetFile(
        file.absolute.path,
        targetPath,
        quality: 70, // 70% quality is usually perfect for mobile apps
        minWidth: 800,
        minHeight: 800,
      );

      return result != null ? File(result.path) : null;
    } catch (e) {
      return file; // Fallback to original if compression fails
    }
  }
}
