import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

kotlin {
    android {
        namespace = "io.github.andannn.popup"
        compileSdk = 36
    }

    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "PopupHost"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.ui)
        }
    }
}

mavenPublishing {
    pom {
        name = "PopupHost"
        description = "A small helper for Jetpack Compose that simplifies sending results between composables."
        url = "https://github.com/andannn/PopupHost"

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("andannn")
                name.set("Andannn")
            }
        }

        scm {
            url = "https://github.com/andannn/PopupHost.git"
            connection = "scm:git:git://github.com/andannn/PopupHost.git"
            developerConnection = "scm:git:ssh://git@github.com/andannn/PopupHost.git"
        }
    }
}
