package com.kurox.qwerty

import io.github.rosemoe.sora.widget.CodeEditor

object EditorManager {

    fun setup(editor: CodeEditor) {

        editor.setText(
            """
<!DOCTYPE html>
<html>
<head>
<title>Kuro IDE</title>
</head>

<body>

<h1>Hello World</h1>

</body>
</html>
            """.trimIndent()
        )
    }
}