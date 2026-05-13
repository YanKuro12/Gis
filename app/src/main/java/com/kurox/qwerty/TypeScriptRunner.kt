package com.kurox.qwerty

object TypeScriptRunner {

    fun transpile(code: String): String {

        return """
// TypeScript Output

$code
        """.trimIndent()
    }
}