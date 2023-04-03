package com.spike.publish

import org.gradle.api.Plugin
import org.gradle.api.Project

open class PublishPlugin : Plugin<Project> {

    override fun apply(clientProject: Project) {
        clientProject.pluginManager.apply("maven-publish")

        val apiSpecFiles = clientProject.layout.projectDirectory.dir("src/main/resources")
            .asFileTree
            .filter { it.isFile }

        clientProject.afterEvaluate {
            apiSpecFiles.forEach { apiSpec ->
                JavaClientConfigurer(project, apiSpec).configure()
            }
        }
    }

    companion object : PublishPlugin() {
    }
}
