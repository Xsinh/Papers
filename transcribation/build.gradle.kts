plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.implosion.transcribation"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }

        externalNativeBuild {
            cmake {
                // When set, builds whisper.android against the version located
                // at GGML_HOME instead of the copy bundled with whisper.cpp.
                val hasGgmlHome = project.hasProperty("GGML_HOME")
                val clblastOn = project.findProperty("GGML_CLBLAST") == "ON"
                if (hasGgmlHome && clblastOn) {
                    // Turning on CLBlast requires GGML_HOME
                    arguments(
                        "-DGGML_HOME=${project.property("GGML_HOME")}",
                        "-DGGML_CLBLAST=ON",
                        "-DOPENCL_LIB=${project.property("OPENCL_LIB")}",
                        "-DCLBLAST_HOME=${project.property("CLBLAST_HOME")}",
                        "-DOPENCL_ROOT=${project.property("OPENCL_ROOT")}",
                        "-DCMAKE_FIND_ROOT_PATH_MODE_INCLUDE=BOTH",
                        "-DCMAKE_FIND_ROOT_PATH_MODE_LIBRARY=BOTH"
                    )
                } else if (hasGgmlHome) {
                    arguments("-DGGML_HOME=${project.property("GGML_HOME")}")
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/whisper/CMakeLists.txt")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}