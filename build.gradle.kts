import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jreleaser.model.Active

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.dokka") version "1.9.20"
    id("maven-publish")
    id("org.jreleaser") version "1.15.0" // or the latest version
}

group = "io.github.jkratz55"
version = "1.1-RELEASE"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(group = "org.springframework", name = "spring-context", version = "5.1.4.RELEASE")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.25")
    implementation(group = "javax.validation", name = "validation-api", version = "2.0.1.Final")

    testImplementation(kotlin("test-junit"))
    testImplementation(group = "org.springframework", name = "spring-test", version = "5.1.4.RELEASE")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "2.23.4")
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

// Create Sources and Javadoc JARs
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)

            pom {
                name.set("Spring Mediator")
                description.set("A mediator library for Spring Framework")
                url.set("https://github.com/jkratz55/spring-mediator")
                inceptionYear.set("2020")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.html")
                    }
                }

                developers {
                    developer {
                        id.set("jkratz55")
                        name.set("Joseph Kratz")
                        email.set("4985721+jkratz55@users.noreply.github.com") // Update to your actual email
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/jkratz55/spring-mediator.git")
                    developerConnection.set("scm:git:ssh://git@github.com:jkratz55/spring-mediator.git")
                    url.set("https://github.com/jkratz55/spring-mediator")
                }
            }
        }
    }

    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}