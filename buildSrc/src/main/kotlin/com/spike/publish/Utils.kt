package com.spike.publish

import java.io.File
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.support.serviceOf
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

object Utils {
    fun createSourceSet(project: Project, generatedDir: File, name: String, language: String): SourceSet {
        val sourceSets = project.the<SourceSetContainer>()

        return sourceSets.create(name) {
            java.srcDir(generatedDir.resolve("${"src/main"}/$language"))
            java.destinationDirectory.set(generatedDir.resolve("build/classes/$language"))
        }
    }

    fun generateComponentForPublication(
        project: Project,
        sourceSet: SourceSet,
        generateTask: TaskProvider<GenerateTask>,
        name: String,
        generatedDir: File,
        artifactName: String
    ) {
        val jarTask = project.tasks.register<Jar>(sourceSet.jarTaskName) {
            dependsOn(generateTask)

            destinationDirectory.set(generatedDir.resolve("build/libs"))
            archiveBaseName.set(artifactName)

            archiveExtension.set("jar")

            from(sourceSet.output)
        }

//      FIXME works without .module file
        project.tasks.withType(GenerateModuleMetadata::class.java) {
            enabled = false
//            enabled = true
        }

        project.tasks.named(sourceSet.compileJavaTaskName) { dependsOn(generateTask) }

        val component = project.gradle.serviceOf<SoftwareComponentFactory>().adhoc(name)

        val implementationConfiguration = project.configurations[sourceSet.implementationConfigurationName]

        implementationConfiguration.attributes {
            attribute(Attribute.of("org.gradle.category", String::class.java), "library")
            attribute(Attribute.of("org.gradle.dependency.bundling", String::class.java), "external")
            attribute(Attribute.of("org.gradle.jvm.version", Integer::class.java), Integer(17))
            attribute(Attribute.of("org.gradle.libraryelements", String::class.java), "jar")
            attribute(Attribute.of("org.gradle.usage", String::class.java), "java-runtime")
        }

        component.addVariantsFromConfiguration(implementationConfiguration) {
            mapToMavenScope("runtime")
            mapToOptional()
        }

        project.extensions.configure(PublishingExtension::class) {
            publications {
                create<MavenPublication>(name) {
                    from(component)
                    artifactId = artifactName
                    artifact(jarTask)
                }
            }
        }
        project.tasks.named("generatePomFileFor${name.replaceFirst("j", "J")}Publication") { dependsOn(jarTask) }
        project.tasks.named("generateMetadataFileFor${name.replaceFirst("j", "J")}Publication") { dependsOn(jarTask) }
    }
}
