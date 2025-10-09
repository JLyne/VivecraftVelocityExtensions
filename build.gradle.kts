plugins {
    java
    `maven-publish`
}

group = "uk.co.notnull"
version = "1.2-SNAPSHOT"
description = "Vivecraft velocity extensions"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    maven {
        url = uri("https://repo.not-null.co.uk/releases/")
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(libs.velocityApi)

    annotationProcessor(libs.velocityApi)
}

tasks {
    compileJava {
        options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-processing"))
        options.encoding = "UTF-8"
    }

    processResources {
        expand("version" to project.version)
    }
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components.getByName("java"))
            pom {
                name = "VivecraftVelocityExtensions"
                description = "Velocity support plugin for Vivecraft Spigot Extensions"
                url = "https://github.com/JLyne/VivecraftVelocityExtensions"
                developers {
                    developer {
                        id = "jim"
                        name = "James Lyne"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/JLyne/VivecraftVelocityExtensions.git"
                    developerConnection = "scm:git:ssh://github.com/JLyne/VivecraftVelocityExtensions.git"
                    url = "https://github.com/JLyne/VivecraftVelocityExtensions"
                }
            }
        }
    }
    repositories {
        maven {
            name = "notnull"
            credentials(PasswordCredentials::class)
            val releasesRepoUrl = uri("https://repo.not-null.co.uk/releases/") // gradle -Prelease publish
            val snapshotsRepoUrl = uri("https://repo.not-null.co.uk/snapshots/")
            url = if (project.hasProperty("release")) releasesRepoUrl else snapshotsRepoUrl
        }
    }
}
