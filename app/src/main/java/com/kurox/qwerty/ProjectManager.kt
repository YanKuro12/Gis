package com.kurox.qwerty

import java.io.File

object ProjectManager {

    fun createProject(path: String, name: String): File {

        val project = File(path, name)

        if (!project.exists()) {
            project.mkdirs()
        }

        return project
    }
}