import 'package:flutter/material.dart';

class Preloader extends StatelessWidget {
  const Preloader({super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: SizedBox(
        width: 60,
        height: 60,
        child: Image.asset('assets/preloader/preloader.gif'),
      ),
    );
  }
}
