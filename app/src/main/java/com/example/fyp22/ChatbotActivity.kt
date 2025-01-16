package com.example.fyp22

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ChatbotActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot)

        // Initialize the WebView and ProgressBar
        val webView: WebView = findViewById(R.id.webView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Enable JavaScript and other settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true  // Enable DOM storage if required
        webView.settings.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK)
        webView.settings.setSupportZoom(false)
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false

        // Set up WebViewClient to handle page loading within the WebView
        webView.webViewClient = object : WebViewClient() {

            // Handle errors while loading the URL
            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("WebView", "HTTP Error loading URL: ${errorResponse?.statusCode}")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                //Log.e("WebView", "Error loading URL: ${error?.description}")
            }

            // Handle page navigation within WebView (i.e., do not open links in browser)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        // Set up WebChromeClient to handle the loading progress
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
            }
        }

        // Load the URL (Voiceflow chatbot prototype)
        val url = "https://creator.voiceflow.com/prototype/66f42770011c56b5632835b7"
        webView.loadUrl(url)

        // Handle the back button press for WebView
        webView.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (webView.canGoBack()) {
                    webView.goBack()  // Go back within WebView
                    return@setOnKeyListener true
                }
            }
            false
        }
    }

    // Handle the back button for WebView (alternative method)
    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webView)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
