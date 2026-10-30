// ============== MAIN CONFIG ==============
group = "progmeth_2026_1"
version = "1"
// =========================================

application {
    // Ex. "src/main/java/game/DeckManager.java" becomes "game.Deckmanager"
    mainClass.set("WelcomeWithHandler")
}

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // TestFX dependencies
    testImplementation("org.testfx:testfx-core:4.0.17")
    testImplementation("org.testfx:testfx-junit5:4.0.17")
    testImplementation("org.hamcrest:hamcrest:2.2")

    // JUnit dependencies
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyLocking {
    lockAllConfigurations()
}

javafx {
    version = "25.0.4"
    modules = listOf(
        "javafx.controls", "javafx.fxml", "javafx.graphics",
        "javafx.media", "javafx.web", "javafx.swing",
    )
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()

    // TestFX configuration for JavaFX 25
    systemProperty("testfx.robot", "glass")
    systemProperty("testfx.setup.timeout", "60000")
    systemProperty("testfx.launch.timeout", "60000")

    // this is to force the "test" task to run all tests every time
    // regardless of whether there's any changes,
    // comment out this line if you prefer it otherwise
    outputs.upToDateWhen { false }
}

tasks.jar {
    // Set duplicate handling strategy
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Include compiled classes (default behavior)
    from(sourceSets.main.get().output)

    // Include source code
    from(sourceSets.main.get().allSource)

    // Make it executable by setting the main class
    manifest {
        attributes("Main-Class" to application.mainClass.get())
    }

    archiveFileName = "${project.name}-${project.version}-with-sources.jar"
}
