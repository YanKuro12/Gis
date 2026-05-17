package com.kurox.qwerty

import com.chaquo.python.Python

object PythonRunner {

    fun run(code: String): String {
        return try {
            if (!Python.isStarted()) {
                "Python is not initialized"
            } else {
                val py = Python.getInstance()
                val module = py.getModule("__main__")
                val result = module.callAttr("exec", code).toString()
                "Python Output:\n$result"
            }
        } catch (e: Exception) {
            "Python Error: ${e.message}"
        }
    }
}
