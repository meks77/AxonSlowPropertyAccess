plugins {
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = uri("https://oss.sonatype.org/content/repositories/snapshots/"))
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    // the bom 4.10.1-SNAPSHOT uses version 4.10.0. Therefor the bom couldn't be used
//    implementation(enforcedPlatform("org.axonframework:axon-bom:4.10.1-SNAPSHOT"))
    implementation("org.axonframework:axon-configuration:4.10.1-SNAPSHOT")
    implementation("org.axonframework:axon-messaging:4.10.1-SNAPSHOT")
//    this implemention contains a cache with the information how to access a property of the model. It's not available in maven central
//    If you would like to try it out, you have to build it localy from https://github.com/meks77/AxonFramework/tree/cputime-decrease-on-property-access
//    implementation("org.axonframework:axon-messaging:4.10.1-SNAPSHOT-meks77")
    implementation("org.axonframework:axon-modelling:4.10.1-SNAPSHOT")
    implementation("org.axonframework:axon-eventsourcing:4.10.1-SNAPSHOT")
    implementation("org.axonframework:axon-server-connector:4.10.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner")
    implementation("com.fasterxml.jackson.module:jackson-module-blackbird")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.axonframework:axon-test:4.10.1-SNAPSHOT")
}

group = "org.acme"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
