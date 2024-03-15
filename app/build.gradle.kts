plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

group = "io.github.brenoepics"
version = "1.0.0"
repositories {
    mavenCentral()
}

//Resolve the used operating system
var currentOS = org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem()
var platform = ""
if (currentOS.isMacOsX) {
    platform = "mac"
} else if (currentOS.isLinux) {
    platform = "linux"
} else if (currentOS.isWindows) {
    platform = "win"
}

val javaFXVersion = "21"
val appClassName = "io.github.brenoepics.fourier.App"
val appModuleName = "io.github.brenoepics.fourier"

val compiler = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_21.majorVersion))
}

dependencies {
    implementation("org.openjfx:javafx-base:${javaFXVersion}:${platform}")
    implementation("org.openjfx:javafx-controls:${javaFXVersion}:${platform}")
    implementation("org.openjfx:javafx-graphics:${javaFXVersion}:${platform}")
    implementation("org.openjfx:javafx-fxml:${javaFXVersion}:${platform}")
}

application {
    // Define the main class for the application.
    mainModule.set("io.github.brenoepics.fourier")
    mainClass.set(appClassName)
    if (platform.equals("mac")) {
        applicationDefaultJvmArgs = listOf("-Dsun.java2d.metal=true")
    }
}

java {
    modularity.inferModulePath.set(true)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    task<Copy>("copyDependencies") {
        from(configurations.runtimeClasspath)
        into("$buildDir/modules")
    }

    task<Exec>("package") {
        dependsOn(listOf("build", "copyDependencies"))
        val jdkHome = compiler.get().metadata.installationPath.asFile.absolutePath
        commandLine("${jdkHome}/bin/jpackage")
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
            "https://github.com/brenoepic/fourier-fx"
        )

        if (platform == "mac") {
            args += listOf(
                "--mac-package-name",
                "Fourier-FX",
                "--mac-package-identifier",
                "io.github.brenoepics.fourier",
                "--mac-app-category",
                "public.app-category.games",
            )
        } else if (platform == "linux") {
            args += listOf(
                "--linux-shortcut",
                "--linux-package-name", "fourier-fx",
                "--linux-rpm-license-type", "GPLv3",
            )
        } else if (platform == "win") {
            args += listOf(
                "--win-menu",
                "--win-menu-group", "Fourier-FX",
                "--win-shortcut-prompt",
                "--win-help-url", "https://github.com/brenoepic/fourier-fx",
                "--win-dir-chooser",
            )
        }

        args(args)
    }

}
