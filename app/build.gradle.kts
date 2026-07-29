plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "io.github.brenoepics"
version = "2.0.0"

repositories {
    mavenCentral()
}

// Resolve the used operating system (for jpackage installer flags)
val currentOS = org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem()
val platform = when {
    currentOS.isMacOsX -> "mac"
    currentOS.isLinux -> "linux"
    currentOS.isWindows -> "win"
    else -> ""
}

val appClassName = "io.github.brenoepics.fourier.App"
val appModuleName = "io.github.brenoepics.fourier"

dependencies {
    implementation(project(":core"))
}

javafx {
    version = "26.0.2"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainModule = appModuleName
    mainClass = appClassName
    if (platform == "mac") {
        applicationDefaultJvmArgs = listOf("-Dsun.java2d.metal=true")
    }
}

java {
    modularity.inferModulePath = true
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val copyDependencies by tasks.registering(Copy::class) {
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("modules"))
}

tasks.register<Exec>("package") {
    dependsOn(tasks.build, copyDependencies)
    val jdkHome = javaToolchains.compilerFor {
        languageVersion = JavaLanguageVersion.of(25)
    }.map { it.metadata.installationPath.asFile.absolutePath }
    val buildDir = layout.buildDirectory.get().asFile

    executable = "${jdkHome.get()}/bin/jpackage"
    val args = mutableListOf(
        "-n",
        "fourier-fx",
        "-p",
        "$buildDir/modules" + File.pathSeparator + "$buildDir/libs",
        "-d",
        "$buildDir/installer",
        "-m",
        "${appModuleName}/${appClassName}",
        "--copyright",
        "Copyright (c) 2024 Breno A.",
        "--description",
        "Fourier-FX is a simple application to visualize the Fourier Transform.",
        "--vendor",
        "Breno A.",
        "--app-version",
        project.version.toString(),
        "--about-url",
        "https://github.com/brenoepics/Fourier-FX"
    )

    when (platform) {
        "mac" -> args += listOf(
            "--mac-package-name",
            "Fourier-FX",
            "--mac-package-identifier",
            "io.github.brenoepics.fourier",
            "--mac-app-category",
            "public.app-category.games",
        )
        "linux" -> args += listOf(
            "--linux-shortcut",
            "--linux-package-name", "fourier-fx",
            "--linux-rpm-license-type", "MIT",
        )
        "win" -> args += listOf(
            "--win-menu",
            "--win-menu-group", "Fourier-FX",
            "--win-shortcut-prompt",
            "--win-help-url", "https://github.com/brenoepics/Fourier-FX",
            "--win-dir-chooser",
        )
    }

    args(args)
}
