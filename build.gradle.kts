plugins {
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation(platform("org.axonframework:axon-bom:4.10.0"))
    implementation("org.axonframework:axon-configuration")
    implementation("org.axonframework:axon-messaging")
//    this implemention contains a cache with the information how to access a property of the model. It's not available in maven central
//    If you would like to try it out, you have to build it localy from https://github.com/meks77/AxonFramework/tree/cputime-decrease-on-property-access
//    implementation("org.axonframework:axon-messaging:4.10.1-SNAPSHOT-meks77")
    implementation("org.axonframework:axon-modelling")
    implementation("org.axonframework:axon-eventsourcing")
    implementation("org.axonframework:axon-server-connector")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.axonframework:axon-test")
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
