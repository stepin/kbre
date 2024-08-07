// FILE IS GENERATED AUTOMATICALLY BY kbre. DON'T EDIT MANUALLY.

plugins {
    kotlin("multiplatform") version libs.versions.kotlin
    alias(libs.plugins.sonar)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.task.tree)
    alias(libs.plugins.kotlinx.serialization)
}

group = "name.stepin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    commonMainImplementation(libs.clikt)
    commonTestImplementation(kotlin("test"))
    commonMainImplementation(libs.yamlkt)

    commonMainImplementation(libs.okio)
}

kotlin {
    linuxX64().binaries.executable { entryPoint = "name.stepin.main" }
    linuxArm64().binaries.executable { entryPoint = "name.stepin.main" }
    macosX64().binaries.executable { entryPoint = "name.stepin.main" }
    macosArm64().binaries.executable { entryPoint = "name.stepin.main" }

//    val hostOs = System.getProperty("os.name")
//    val hostOsArch = System.getProperty("os.arch")
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget =
//        if (hostOs == "Mac OS X") {
//            if (hostOsArch == "aarch64") {
//                macosArm64("native")
//            } else {
//                macosX64("native")
//            }
//        } else if (hostOs == "Linux") {
//            linuxX64("native")
//        } else if (isMingwX64) {
//            mingwX64("native")
//        } else {
//            throw RuntimeException("Host OS is not supported in Kotlin/Native.")
//        }
//    nativeTarget.binaries.executable { entryPoint = "name.stepin.main" }

    applyDefaultHierarchyTemplate()

//    sourceSets {
//        nativeMain {
//        }
//        nativeTest {
//        }
//    }
}

sonar {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/xml/report.xml")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectKey", "kbre")
        property("sonar.projectName", "kbre")
        property("sonar.token", "sqp_821b1d3209761625bdd29259674237d429bce626")
    }
}
