package com.kurox.qwerty

import android.webkit.WebView

object WebRuntime {

    fun run(webView: WebView, code: String) {

        webView.loadDataWithBaseURL(
            null,
            code,
            "text/html",
            "UTF-8",
            null
        )
    }
}