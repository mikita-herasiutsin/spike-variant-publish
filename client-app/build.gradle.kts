import com.spike.publish.PublishPlugin

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "org.spike"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
//    FIXME jar isn't in classpath as files[] sections in .module file wasn't generated
//    implementation("org.spike:client-app-v1-client:1.0-SNAPSHOT")
    implementation(files("build/javaClientV1/build/libs/client-app-v1-client-1.0-SNAPSHOT.jar"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

apply<PublishPlugin>()
