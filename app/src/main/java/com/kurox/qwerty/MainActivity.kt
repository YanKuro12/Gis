package com.kurox.qwerty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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

        // Initialize Python
        initializePython()

        // Create main layout
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        // Create CodeEditor
        try {
            editor = CodeEditor(this).apply {
                setText("print('Hello Kuro IDE')")
            }
        } catch (e: Exception) {
            Log.e("Editor", "Error creating editor: ${e.message}")
        }

        // Create RUN Button
        runButton = Button(this).apply {
            text = "RUN CODE"
        }

        // Create Output TextView
        output = TextView(this).apply {
            textSize = 14f
            setPadding(20, 20, 20, 20)
            text = "Output will appear here..."
        }

        // Create WebView
        webView = WebView(this).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                mixedContentMode = WebView.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {
                    Log.d("WebView", "JS Console: $message")
                    super.onConsoleMessage(message, lineNumber, sourceID)
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: android.webkit.WebResourceRequest?,
                    error: android.webkit.WebResourceError?
                ) {
                    output.text = "❌ WebView Error: ${error?.description}"
                    Log.e("WebView", "Error: ${error?.description}")
                    super.onReceivedError(view, request, error)
                }
            }
        }

        // Add Editor to layout
        layout.addView(
            editor,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400  // Fixed: was 0 (invalid)
            )
        )

        // Add Run Button
        layout.addView(
            runButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                80
            )
        )

        // Add Output TextView
        layout.addView(
            output,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200
            )
        )

        // Add WebView
        layout.addView(
            webView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
            )
        )

        // Set content view
        setContentView(layout)

        // Set Run Button Click Listener
        runButton.setOnClickListener {
            executeCode()
        }
    }

    private fun initializePython() {
        try {
            if (!Python.isStarted()) {
                try {
                    Python.start(AndroidPlatform(this))
                    Log.d("Python", "Python initialized successfully")
                } catch (e: Exception) {
                    Log.e("Python", "Failed to start Python: ${e.message}", e)
                    output.text = "❌ Python Init Error: ${e.message}"
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error during Python initialization: ${e.message}", e)
        }
    }

    private fun executeCode() {
        try {
            val code = editor.text.toString().trim()

            if (code.isEmpty()) {
                output.text = "⚠️ Error: Code is empty"
                return
            }

            when {
                // HTML Code
                code.contains("<html") || code.contains("<HTML") -> {
                    try {
                        webView.loadDataWithBaseURL(
                            null,
                            code,
                            "text/html",
                            "UTF-8",
                            null
                        )
                        output.text = "✅ HTML executed successfully"
                        Log.d("HTML", "HTML loaded")
                    } catch (e: Exception) {
                        output.text = "❌ HTML Error: ${e.message}"
                        Log.e("HTML", "Error: ${e.message}", e)
                    }
                }

                // Python Code
                code.trim().startsWith("print(") || code.contains("import ") -> {
                    executePython(code)
                }

                // JavaScript Code
                code.contains("console.log") || code.contains("function") || code.contains("var ") || code.contains("let ") -> {
                    executeJavaScript(code)
                }

                else -> {
                    output.text = "⚠️ Unsupported language. Use:\n- Python (print)\n- HTML (<html>)\n- JavaScript (console.log)"
                }
            }

        } catch (e: Exception) {
            output.text = "❌ Error: ${e.message}\n${e.cause}"
            Log.e("Execute", "Error: ${e.message}", e)
        }
    }

    private fun executePython(code: String) {
        try {
            if (!Python.isStarted()) {
                output.text = "❌ Python is not initialized"
                return
            }

            val py = Python.getInstance()
            val module = py.getModule("__main__")

            try {
                val result = module.callAttr("exec", code).toString()
                output.text = "✅ Python Output:\n$result"
                Log.d("Python", "Execution result: $result")
            } catch (e: Exception) {
                output.text = "❌ Python Error:\n${e.message}"
                Log.e("Python", "Execution error: ${e.message}", e)
            }
        } catch (e: Exception) {
            output.text = "❌ Python Error: ${e.message}"
            Log.e("Python", "Error: ${e.message}", e)
        }
    }

    private fun executeJavaScript(code: String) {
        try {
            val htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>JavaScript Output</title>
                    <style>
                        body { font-family: monospace; padding: 20px; background: #f0f0f0; }
                        #output { background: white; padding: 15px; border-radius: 5px; }
                    </style>
                </head>
                <body>
                    <h2>JavaScript Execution Result:</h2>
                    <div id="output"></div>
                    <script>
                        try {
                            let consoleLog = [];
                            const originalLog = console.log;
                            console.log = function(...args) {
                                consoleLog.push(args.join(' '));
                                originalLog.apply(console, args);
                            };
                            
                            $code
                            
                            document.getElementById('output').innerHTML = '<pre>' + consoleLog.join('\n') + '</pre>';
                        } catch(err) {
                            document.getElementById('output').innerHTML = '<pre style="color: red;">Error: ' + err.message + '</pre>';
                        }
                    </script>
                </body>
                </html>
            """.trimIndent()

            webView.loadDataWithBaseURL(
                null,
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
            output.text = "✅ JavaScript executed successfully"
            Log.d("JavaScript", "JS executed")
        } catch (e: Exception) {
            output.text = "❌ JavaScript Error: ${e.message}"
            Log.e("JavaScript", "Error: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        try {
            if (::webView.isInitialized) {
                webView.stopLoading()
                webView.clearCache(true)
                webView.destroy()
                Log.d("MainActivity", "WebView destroyed")
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error destroying WebView: ${e.message}", e)
        }
        super.onDestroy()
    }
}