// gradle/jacoco.gradle.kts

// Menggunakan apply plugin, bukan blok plugins {} jika ini adalah file skrip terpisah
apply(plugin = "jacoco")

configure<JacocoPluginExtension> {
    // Gunakan versi JaCoCo yang stabil
    toolVersion = "0.8.11"
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*") // Diperlukan untuk kompatibilitas Java 11/17+
    }
}

project.afterEvaluate {
    // Kita menargetkan build variant 'debug'
    val testTaskName = "testDebugUnitTest"
    val reportTaskName = "jacocoTestReport"

    tasks.register<JacocoReport>(reportTaskName) {
        dependsOn(testTaskName)

        group = "Reporting"
        description = "Generate JaCoCo coverage reports for the debug build."

        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        // Daftar file/kelas yang HARUS diabaikan dari kalkulasi coverage
        val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            // Mengabaikan dependensi DI / Hilt
            "**/*_MembersInjector.class",
            "**/Dagger*Component*.class",
            "**/Dagger*Subcomponent*.class",
            "**/*_Factory.class",
            "**/*_Provide*Factory.class",
            "**/*HiltModule_*.class",
            "**/*_HiltModules*.class",
            "**/Hilt_*.*",
            "**/*_GeneratedInjector.class",
            // Mengabaikan Jetpack Compose compiler generated classes
            "**/*ComposableSingletons*.*",
            "**/*ComposableInvoker*.*",
            "**/*\$Lambda\$*.*"
        )

        // Lokasi file class hasil kompilasi Kotlin & Java
        val javaClasses =
            fileTree("${project.layout.buildDirectory.get()}/intermediates/javac/debug/classes").exclude(
                fileFilter
            )
        val kotlinClasses =
            fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/debug").exclude(
                fileFilter
            )

        classDirectories.setFrom(files(javaClasses, kotlinClasses))

        // Lokasi kode sumber aplikasi Anda
        sourceDirectories.setFrom(
            files(
                "${project.projectDir}/src/main/java",
                "${project.projectDir}/src/main/kotlin"
            )
        )

        // Mengambil data eksekusi (.exec) yang dihasilkan setelah tes berjalan
        executionData.setFrom(
            files(
                "${project.layout.buildDirectory.get()}/outputs/unit_test_code_coverage/debugUnitTest/${testTaskName}.exec"
            )
        )
    }
}