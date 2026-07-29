plugins {
    `java-library`
}

group = "io.github.brenoepics"
version = "2.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

// Keep the bytecode at Java 21 so the TeaVM WebAssembly compiler can consume it.
tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}
