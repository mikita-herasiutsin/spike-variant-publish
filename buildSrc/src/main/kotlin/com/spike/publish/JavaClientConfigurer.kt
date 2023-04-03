package com.spike.publish

import java.io.File
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import com.spike.publish.Utils.createSourceSet
import com.spike.publish.Utils.generateComponentForPublication

class JavaClientConfigurer(
    private val project: Project,
    private val apiSpec: File,
) {
    private val apiVersion = 1

    fun configure() {
        val name = "javaClientV$apiVersion"
        val generatedDir = File("${project.buildDir}/$name")
        val artifactName = "${project.name}-v$apiVersion-client"
        val packageName = "${project.rootProject.group.toString()}.${project.name}.v$apiVersion.client".replace("-", "")

        val sourceSet = createSourceSet(project, generatedDir, name, "java")

        project.dependencies {
            sourceSet.compileOnlyConfigurationName("com.google.code.findbugs:jsr305:3.0.2")

            sourceSet.implementationConfigurationName("com.google.code.findbugs:jsr305:3.0.2")
            sourceSet.implementationConfigurationName("org.springframework:spring-web:5.3.24")
            sourceSet.implementationConfigurationName("org.springframework:spring-context:5.3.24")
            sourceSet.implementationConfigurationName("com.fasterxml.jackson.core:jackson-core:2.14.1")
            sourceSet.implementationConfigurationName("com.fasterxml.jackson.core:jackson-annotations:2.14.1")
            sourceSet.implementationConfigurationName("com.fasterxml.jackson.core:jackson-databind:2.14.1")
            sourceSet.implementationConfigurationName("com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:2.14.1")
            sourceSet.implementationConfigurationName("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.1")
            sourceSet.implementationConfigurationName("jakarta.annotation:jakarta.annotation-api:1.3.5")
        }

        val generateTask = project.tasks.register("generate-$name", GenerateTask::class) {
            generatorName.set("java")
            inputSpec.set(project.projectDir.path + "/src/main/resources/${apiSpec.name}")
            enablePostProcessFile.set(true)
            skipOverwrite.set(false)
            outputs.cacheIf { true }
            apiPackage.set(packageName)
            invokerPackage.set("$packageName.invoker")
            modelPackage.set("$packageName.model")
            configOptions.set(
                mapOf(
                    "java8" to "true",
                    "dateLibrary" to "java8",
                    "serializationLibrary" to "jackson",
                    "library" to "resttemplate",
                    "useBeanValidation" to "false",
                    "enableBuilderSupport" to "true",
                    "openApiNullable" to "false"
                )
            )
            globalProperties.set(mapOf("modelDocs" to "true"))
            outputDir.set(generatedDir.absolutePath)
            id.set(artifactName)
        }

        generateComponentForPublication(project, sourceSet, generateTask, name, generatedDir, artifactName)
    }
}
