plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.logicsoftbd.fflipy"
    compileSdk = flutter.compileSdkVersion

//    signingConfigs {
//        create("release") {
//            val keyPropertiesFile = rootProject.file("../key.properties")
//            if (keyPropertiesFile.exists()) {
//                val keyProperties = java.util.Properties()
//                keyProperties.load(keyPropertiesFile.inputStream())
//                keyAlias = keyProperties.getProperty("keyAlias")
//                keyPassword = keyProperties.getProperty("keyPassword")
//                storeFile = file(keyProperties.getProperty("storeFile"))
//                storePassword = keyProperties.getProperty("storePassword")
//            }
//        }
//    }

    externalNativeBuild {
        cmake {
            // Kotlin DSL সিনট্যাক্স (file(...) ব্যবহার করতে হবে)
            path = file("src/main/cpp/CMakeLists.txt")
            // Java DSL সিনট্যাক্স
//            path "src/main/cpp/CMakeLists.txt"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        applicationId = "com.logicsoftbd.fflipy"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
//            signingConfig = signingConfigs.getByName("release")
        }
    }
}

flutter {
    source = "../.."
}
