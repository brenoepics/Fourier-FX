import org.teavm.gradle.api.OptimizationLevel

plugins {
    java
    id("org.teavm") version "0.15.0"
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

// TeaVM consumes the compiled bytecode; keep it at Java 21 for compatibility.
tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

dependencies {
    implementation(project(":core"))
    implementation(teavm.libs.jso)
    implementation(teavm.libs.jsoApis)
}

teavm.wasmGC {
    mainClass = "io.github.brenoepics.fourier.web.Main"
    targetFileName = "fourier.wasm"
    obfuscated = true
    optimization = OptimizationLevel.AGGRESSIVE
}

// Static site bundle for GitHub Pages: webapp assets + the generated WASM module and its JS runtime.
val webDist by tasks.registering(Copy::class) {
    group = "build"
    description = "Assembles the static web app (HTML + WASM) into build/dist"
    dependsOn(tasks.named("generateWasmGC"), tasks.named("copyWasmGCRuntime"))
    from(layout.projectDirectory.dir("src/main/webapp"))
    from(layout.buildDirectory.dir("generated/teavm/wasm-gc")) {
        into("wasm-gc")
    }
    into(layout.buildDirectory.dir("dist"))
}

tasks.named("assemble") {
    dependsOn(webDist)
}
