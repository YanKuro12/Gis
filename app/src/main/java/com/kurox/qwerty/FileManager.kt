package com.kurox.qwerty

import java.io.File

object FileManager {

    fun saveFile(path: String, content: String) {

        File(path).writeText(content)
    }

    fun readFile(path: String): String {

        return File(path).readText()
    }
}