import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "pro.ftnl"
version = "1.0"
var mainClassName = "${group}.Main"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.slf4j", "slf4j-api", "1.7.2")
    implementation("ch.qos.logback", "logback-classic", "1.0.9")
    implementation("ch.qos.logback", "logback-core", "1.0.9")

    implementation("net.dv8tion:JDA:5.0.0-alpha.13")
    implementation("com.github.minndevelopment:jda-ktx:d5c5d9d")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.reflections:reflections:0.10.2")

    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:3.1.1")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
    implementation("org.apache.xmlgraphics:batik-codec:1.14")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("QPointBot")
    archiveClassifier.set("")
    archiveVersion.set(version)
}

tasks.withType<org.gradle.jvm.tasks.Jar> {
    manifest {
        attributes["Implementation-Title"] = "QPointBot"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = mainClassName
    }
}