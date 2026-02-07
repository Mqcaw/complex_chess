plugins {
    id("java")
}

group = "me.jdfunk"
version = "1.0-SNAPSHOT"

val distDir = layout.projectDirectory.dir("dist")

subprojects {
    plugins.withType<JavaPlugin> {
        tasks.withType<Jar>().configureEach {
            destinationDirectory.set(distDir)
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}