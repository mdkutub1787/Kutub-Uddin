import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        return windows;
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  // TODO: Replace these with your actual Firebase configuration values after running `flutterfire configure`
  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyBMwJOVrQctCz_hBskT8_6PWbkuX4rEl9g',
    appId: '1:877918542878:web:placeholder',
    messagingSenderId: '877918542878',
    projectId: 'smart-shop-da4db',
    authDomain: 'smart-shop-da4db.firebaseapp.com',
    storageBucket: 'smart-shop-da4db.firebasestorage.app',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyBMwJOVrQctCz_hBskT8_6PWbkuX4rEl9g',
    appId: '1:877918542878:android:903b6f044cc387daa1220f',
    messagingSenderId: '877918542878',
    projectId: 'smart-shop-da4db',
    storageBucket: 'smart-shop-da4db.firebasestorage.app',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyBMwJOVrQctCz_hBskT8_6PWbkuX4rEl9g',
    appId: '1:877918542878:ios:placeholder',
    messagingSenderId: '877918542878',
    projectId: 'smart-shop-da4db',
    storageBucket: 'smart-shop-da4db.firebasestorage.app',
    iosBundleId: 'com.kutub.smart_shop',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'AIzaSyBMwJOVrQctCz_hBskT8_6PWbkuX4rEl9g',
    appId: '1:877918542878:ios:placeholder',
    messagingSenderId: '877918542878',
    projectId: 'smart-shop-da4db',
    storageBucket: 'smart-shop-da4db.firebasestorage.app',
    iosBundleId: 'com.kutub.smart_shop',
  );

  static const FirebaseOptions windows = FirebaseOptions(
    apiKey: 'AIzaSyBMwJOVrQctCz_hBskT8_6PWbkuX4rEl9g',
    appId: '1:877918542878:web:placeholder',
    messagingSenderId: '877918542878',
    projectId: 'smart-shop-da4db',
    authDomain: 'smart-shop-da4db.firebaseapp.com',
    storageBucket: 'smart-shop-da4db.firebasestorage.app',
  );
}
