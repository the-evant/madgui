plugins {
    java
    id("com.gradleup.shadow") version "9.5.0" apply false
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

pluginManager.apply("com.gradleup.shadow")

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    named<Jar>("jar") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    named<Jar>("shadowJar") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        archiveClassifier.set("")
    }

    build {
        dependsOn("shadowJar")
    }
}
