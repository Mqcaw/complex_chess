import sun.jvmstat.monitor.MonitoredVmUtil.mainClass

plugins {
    id("java")
    application
}

application {
    mainClass = "me.jdfunk.app.Main"
}

group = "me.jdfunk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(project(":api"))
    runtimeOnly(project(":core"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to application.mainClass.get(),
            "Class-Path" to configurations.runtimeClasspath.get()
                .files
                .joinToString(" ") { it.name }
        )
    }
}

tasks.named<JavaExec>("run") {
    systemProperty(
        "app.config.dir",
        "${rootProject.projectDir}/resources-external"
    )
}