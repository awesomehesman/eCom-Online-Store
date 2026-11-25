import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
import java.security.MessageDigest
import java.util.HexFormat

plugins {
    java
    id("org.springframework.boot") version "3.5.16"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.5.16"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.bouncycastle:bcprov-jdk18on:1.86")
    implementation("org.springframework.session:spring-session-jdbc")
    implementation("org.flywaydb:flyway-core")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.5.1")
}

dependencyLocking {
    lockAllConfigurations()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("integration")
    }
}

val integrationTest by tasks.registering(Test::class) {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform {
        includeTags("integration")
    }
    shouldRunAfter(tasks.named("test"))
    reports {
        junitXml.outputLocation = layout.buildDirectory.dir("test-results/integrationTest")
        html.outputLocation = layout.buildDirectory.dir("reports/tests/integrationTest")
    }
}

tasks.named("check") {
    dependsOn(integrationTest)
}

tasks.register<JavaExec>("argon2Benchmark") {
    description = "Runs the isolated DEC-0006 Argon2id evidence benchmark."
    group = "verification"
    dependsOn(tasks.named("testClasses"))
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("com.enterprise.fashion.ecommerce.identity.benchmark.Argon2Benchmark")
    javaLauncher.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    )
    jvmArgs("-Xms512m", "-Xmx2g")
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.register<JavaExec>("inspectBei02GradleEvidence") {
    group = "verification"
    description = "Reconciles the executable against actual Gradle production artifact evidence."
    val executable = tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar")
    dependsOn(executable, tasks.named("testClasses"))
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("com.enterprise.fashion.ecommerce.releasecompliance.Bei02Verification")
    javaLauncher.set(javaToolchains.launcherFor { languageVersion = JavaLanguageVersion.of(21) })
    doFirst {
        check(gradle.startParameter.isOffline && gradle.startParameter.dependencyVerificationMode.name == "STRICT") {
            "BEI-02 requires offline execution and strict dependency verification"
        }
        val source = project.buildscript.configurations.getByName("classpath").incoming.artifacts.artifacts
            .single { it.id.componentIdentifier.displayName == "org.springframework.boot:spring-boot-loader-tools:3.5.16"
                    && it.file.name == "spring-boot-loader-tools-3.5.16.jar" }
        val evidence = mutableListOf(executable.get().archiveFile.get().asFile.absolutePath,
            source.file.absolutePath, file("gradle.lockfile").absolutePath,
            file("gradle/verification-metadata.xml").absolutePath)
        val runtime = configurations.getByName("productionRuntimeClasspath")
        val artifacts = runtime.incoming.artifacts.artifacts.groupBy { it.id.componentIdentifier }
        val components = runtime.incoming.resolutionResult.allComponents
            .filter { it.id is org.gradle.api.artifacts.component.ModuleComponentIdentifier }
            .sortedBy { it.id.displayName }
        println("Resolved external components: ${components.size}")
        println("Selected external artifacts: ${components.sumOf { artifacts[it.id].orEmpty().size }}")
        for (component in components) {
            val variants = component.variants.joinToString(",") { variant ->
                variant.attributes.getAttribute(org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE)
                    ?.name ?: "unspecified"
            }
            val selected = artifacts[component.id].orEmpty().sortedBy { it.file.name }
            check(selected.size <= 1) { "Unsupported multiple selected artifacts for ${component.id.displayName}" }
            evidence.add(component.id.displayName)
            evidence.add(variants)
            evidence.add(selected.singleOrNull()?.file?.name ?: "-")
            var selectedDigest = "-"
            println("${component.id.displayName}\t${variants}")
            for (artifact in selected) {
                val digest = MessageDigest.getInstance("SHA-256")
                artifact.file.inputStream().use { input ->
                    val buffer = ByteArray(8192)
                    while (true) {
                        val count = input.read(buffer)
                        if (count < 0) break
                        digest.update(buffer, 0, count)
                    }
                }
                selectedDigest = HexFormat.of().formatHex(digest.digest())
                println("  ${artifact.file.name}\t$selectedDigest")
            }
            evidence.add(selectedDigest)
        }
        setArgs(evidence)
    }
}
