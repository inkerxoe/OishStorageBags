import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.2"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    env {
        install(
            UNIVERSAL,
            METRICS,
            BUKKIT_ALL,
            KETHER,
            NMS,
            NMS_UTIL
        )
    }
    description {
        contributors {
            name("InkerXoe")
        }
        desc("Provides a flexible and powerful Pocket Space system for Minecraft Bukkit Server")
    }
    version { taboolib = "6.1.0" }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:v12004:universal")
    // ulid-kotlin
    implementation("com.aallam.ulid:ulid-kotlin:1.3.0")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
