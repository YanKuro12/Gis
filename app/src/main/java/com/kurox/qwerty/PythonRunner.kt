package com.kurox.qwerty

fun run(code: String): String {
    return try {
        val py = Python.getInstance()
        val result = py.getModule("__main__").callAttr("exec", code)
        "Python Execution Result:\n$result"
    } catch (e: Exception) {
        "Python Error: ${e.message}"
    }
}