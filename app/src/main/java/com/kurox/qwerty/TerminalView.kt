package com.kurox.qwerty

object TerminalView {

    fun execute(command: String): String {

        return when(command) {

            "help" -> {
                """
help
clear
run
build
                """.trimIndent()
            }

            else -> {
                "Unknown command: $command"
            }
        }
    }
}