package com.kurox.qwerty

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import io.github.rosemoe.sora.widget.CodeEditor

class MainActivity : AppCompatActivity() {

    private lateinit var editor: CodeEditor
    private lateinit var runButton: Button
    private lateinit var output: TextView
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        editor = CodeEditor(this).apply {
            setText("print('Hello Kuro IDE')")
        }

        runButton = Button(this).apply {
            text = "RUN"
        }

        output = TextView(this).apply {
            textSize = 14f
            setPadding(20, 20, 20, 20)
        }

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
        }

        layout.addView(
            editor,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        layout.addView(runButton)

        layout.addView(
            output,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
            )
        )

        layout.addView(
            webView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                500
            )
        )

        setContentView(layout)

        runButton.setOnClickListener {

            val code = editor.text.toString()

            try {

                when {

                    code.contains("<html") -> {
                        webView.loadDataWithBaseURL(
                            null,
                            code,
                            "text/html",
                            "UTF-8",
                            null
                        )

                        output.text = "HTML Executed"
                    }

                    code.trim().startsWith("print(") -> {

                        val py = Python.getInstance()
                        val module = py.getModule("builtins")

                        output.text = "Python:\n$code"
                    }

                    code.contains("console.log") -> {

                        val html = """
                            <html>
                            <body>
                            <script>
                            $code
                            </script>
                            </body>
                            </html>
                        """.trimIndent()

                        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)

                        output.text = "JavaScript Executed"
                    }

                    else -> {
                        output.text = "Unsupported language"
                    }
                }

            } catch (e: Exception) {
                output.text = "Error: ${e.message}"
            }
        }
    }
}