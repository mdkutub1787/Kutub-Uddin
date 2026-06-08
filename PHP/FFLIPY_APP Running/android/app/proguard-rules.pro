# Flutter-specific rules.
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.**  { *; }
-keep class io.flutter.util.**  { *; }
-keep class io.flutter.view.**  { *; }
-keep class io.flutter.embedding.**  { *; }
-keep class io.flutter.embedding.engine.plugins.**  { *; }
-dontwarn io.flutter.embedding.**

-keep class retrofit2.** { *; }
-keep class com.google.gson.** { *; }
