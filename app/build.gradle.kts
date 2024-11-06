
plugins {
    id("com.android.application")
    
}

android {
    namespace = "xxx.solzer.dlsbot"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "xxx.solzer.dlsbot"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        
    }
    
}

dependencies {
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("org.opencv:opencv:4.9.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.preference:preference:1.2.0")
}
