import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:encrypt/encrypt.dart' as encrypt;
import '../core/widgets/brand_app_bar.dart';

class TestDecryptionScreen extends StatefulWidget {
  const TestDecryptionScreen({super.key});

  @override
  State<TestDecryptionScreen> createState() => _TestDecryptionScreenState();
}

class _TestDecryptionScreenState extends State<TestDecryptionScreen> {
  final TextEditingController _keyController = TextEditingController();
  final TextEditingController _payloadController = TextEditingController();
  String _result = "Result will appear here...";
  bool _isError = false;

  void _decryptData() {
    FocusScope.of(context).unfocus();

    String keyString = _keyController.text.trim();
    String payload = _payloadController.text.trim();

    if (keyString.isEmpty || payload.isEmpty) {
      setState(() {
        _result = "Please enter both Key and Payload.";
        _isError = true;
      });
      return;
    }

    try {
      // ১. কি (Key) সেট করা
      // যদি Key ৩২ ক্যারেক্টারের কম বা বেশি হয়, তবুও ট্রাই করবে, কিন্তু வানিং প্রিন্ট হবে
      final key = encrypt.Key.fromUtf8(keyString);

      // ২. Base64 ডিকোড করা
      List<int> combined = base64Decode(payload);

      // ৩. IV এবং Encrypted Data আলাদা করা
      // আপনার লজিক অনুযায়ী প্রথম ১৬ বাইট হলো IV
      if (combined.length < 16) {
        throw Exception("Invalid payload size. Must be at least 16 bytes.");
      }

      final ivBytes = combined.sublist(0, 16);
      final iv = encrypt.IV(Uint8List.fromList(ivBytes));

      // বাকি বাইটগুলো হলো আসল এনক্রিপ্টেড ডেটা
      final encryptedBytes = combined.sublist(16);
      final encryptedContent = encrypt.Encrypted(
          Uint8List.fromList(encryptedBytes));

      // ৪. ডিক্রিপ্ট করা (AES-CBC মোডে)
      final encrypter = encrypt.Encrypter(
          encrypt.AES(key, mode: encrypt.AESMode.cbc));
      final decrypted = encrypter.decrypt(encryptedContent, iv: iv);

      setState(() {
        _isError = false;
        try {
          var jsonObject = jsonDecode(decrypted);
          var encoder = const JsonEncoder.withIndent("  ");
          _result = encoder.convert(jsonObject);
        } catch (e) {
          _result = decrypted;
        }
      });
    } catch (e) {
      setState(() {
        _isError = true;
        _result = "❌ Decryption Failed:\n$e";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: BrandAppBar(
        title: const Text("QC: Decryption Tester"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const Text(
                "⚠️ This screen is for QC purposes only.",
                style: TextStyle(
                    color: Colors.red, fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 20),

              TextField(
                controller: _keyController,
                decoration: const InputDecoration(
                  labelText: "Encryption Key (32 chars)",
                  border: OutlineInputBorder(),
                  hintText: "Paste your key here",
                ),
                maxLines: 1,
              ),
              const SizedBox(height: 16),

              TextField(
                controller: _payloadController,
                decoration: const InputDecoration(
                  labelText: "Encrypted Payload (Base64)",
                  border: OutlineInputBorder(),
                  hintText: "Paste the long encrypted string here",
                ),
                maxLines: 5,
              ),
              const SizedBox(height: 20),

              ElevatedButton.icon(
                onPressed: _decryptData,
                icon: const Icon(Icons.lock_open),
                label: const Text("DECRYPT NOW"),
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  backgroundColor: Colors.deepPurple,
                  foregroundColor: Colors.white,
                ),
              ),

              const SizedBox(height: 20),
              const Divider(),
              const Text(
                  "Output:", style: TextStyle(fontWeight: FontWeight.bold)),
              const SizedBox(height: 10),

              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: _isError ? Colors.red.shade50 : Colors.green.shade50,
                  border: Border.all(
                      color: _isError ? Colors.red : Colors.green),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: SelectableText(
                  _result,
                  style: TextStyle(
                    fontFamily: 'Courier',
                    color: _isError ? Colors.red.shade900 : Colors.black87,
                    fontSize: 14,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}