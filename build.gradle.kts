import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.17.4"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.2.0"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "2023.2.1"
    // Gradle Kover Plugin
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    // grammar Plugin
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    maven("https://repo1.maven.org/maven2/")
}

// Configure Gradle IntelliJ Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.set(emptyList())
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(projectDir.resolve(".qodana").canonicalPath)
    resultsPath.set(projectDir.resolve("build/reports/inspections").canonicalPath)
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover.xmlReport {
    onCheck.set(true)
}

sourceSets["main"].java.srcDirs("src/main/gen")

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    generateLexer {
        // source flex file
        sourceFile.set(File("src/main/grammars/wit.flex"))

        // target directory for lexer
        targetOutputDir.set(project.layout.projectDirectory.dir("src/main/gen/org/mvnsearch/plugins/wit/lang/lexer/"))

        // if set, plugin will remove a lexer output file before generating new one. Default: false
        purgeOldFiles.set(true)
    }

    generateParser {
        // source bnf file
        sourceFile.set(File("src/main/grammars/wit.bnf"))

        // optional, task-specific root for the generated files. Default: none
        targetRootOutputDir.set(project.layout.projectDirectory.dir("src/main/gen"))

        // path to a parser file, relative to the targetRoot
        pathToParser.set("/org/mvnsearch/plugins/wit/parser/WitParserGenerated.java")

        // path to a directory with generated psi files, relative to the targetRoot
        pathToPsiRoot.set("/org/mvnsearch/plugins/wit/lang/psi")

        // if set, the plugin will remove a parser output file and psi output directory before generating new ones. Default: false
        purgeOldFiles.set(true)
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
                file("README.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").let { markdownToHTML(it) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider {
            with(changelog) {
                renderItem(
                        getOrNull(properties("pluginVersion"))
                                ?: runCatching { getLatest() }.getOrElse { getUnreleased() },
                        Changelog.OutputType.HTML,
                )
            }
        })
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}
