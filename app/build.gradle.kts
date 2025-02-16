plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.funcablaze.codeworkshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.funcablaze.codeworkshop"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // 脱糖
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    // 脱糖
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    // SoraEditor
    implementation(platform("io.github.Rosemoe.sora-editor:bom:0.23.5"))
    implementation("io.github.Rosemoe.sora-editor:editor:0.9.3")
    implementation("io.github.Rosemoe.sora-editor:language-textmate:0.23.5")
    implementation("io.github.Rosemoe.sora-editor:language-treesitter:0.23.5")
    implementation("io.github.Rosemoe.sora-editor:editor-lsp:0.23.5")
    // 重启应用
    implementation("com.jakewharton:process-phoenix:3.0.0")

//    implementation(libs.recyclerview.v7)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}