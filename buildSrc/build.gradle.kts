
plugins {
    kotlin("jvm") version "1.7.10"
    `kotlin-dsl`
}

group = "org.spike"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openapitools:openapi-generator-gradle-plugin:6.4.0") {
        exclude(group = "io.swagger.parser.v3", module = "swagger-parser")
    }
    implementation("io.swagger.parser.v3:swagger-parser:2.1.10")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}