import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:ticket_app/services/firebase_service.dart';
import 'package:ticket_app/models/app_user.dart';
import 'package:ticket_app/screens/auth/login_screen.dart';
import 'package:ticket_app/screens/home_screen.dart';
import 'package:ticket_app/screens/admin/admin_home_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Rail Sheba',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
        useMaterial3: true,
        fontFamily: 'Roboto',
      ),
      home: const AuthWrapper(),
    );
  }
}

class AuthWrapper extends StatelessWidget {
  const AuthWrapper({super.key});

  @override
  Widget build(BuildContext context) {
    final firebaseService = FirebaseService();

    return StreamBuilder<User?>(
      stream: firebaseService.authStateChanges,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(body: Center(child: CircularProgressIndicator()));
        }

        if (snapshot.hasData) {
          // User is logged in, check role
          return FutureBuilder<AppUser?>(
            future: firebaseService.getCurrentUserData(),
            builder: (context, userSnapshot) {
              if (userSnapshot.connectionState == ConnectionState.waiting) {
                return const Scaffold(body: Center(child: CircularProgressIndicator()));
              }

              if (userSnapshot.hasData) {
                final appUser = userSnapshot.data!;
                if (appUser.role == UserRole.admin) {
                  return AdminHomeScreen();
                } else {
                  return HomeScreen();
                }
              }

              // Data not found? Logout and return to login
              firebaseService.signOut();
              return const LoginScreen();
            },
          );
        }

        return const LoginScreen();
      },
    );
  }
}
