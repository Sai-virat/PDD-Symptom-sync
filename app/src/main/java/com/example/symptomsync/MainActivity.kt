package com.example.symptomsync

import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {

    // Endpoints in order of preference:
    // 1. http://localhost:8000 (ADB USB reverse port)
    // 2. http://172.23.18.125:8000 (Wi-Fi IP)
    // 3. http://10.0.2.2:8000 (Android Emulator)
    // 4. file:///android_asset/index.html (Offline Assets)
    private val urlsToTry = listOf(
        "http://localhost:8000",
        "http://172.23.18.125:8000",
        "http://10.0.2.2:8000",
        "file:///android_asset/index.html"
    )

    private var webViewRef: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F172A)
                ) {
                    SymptomSyncWebViewScreen(
                        urls = urlsToTry,
                        onWebViewCreated = { webViewRef = it }
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webViewRef?.canGoBack() == true) {
            webViewRef?.goBack()
        } else {
            super.onBackPressed()
        }
    }
}

@Composable
fun SymptomSyncWebViewScreen(
    urls: List<String>,
    onWebViewCreated: (WebView) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var currentUrlIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    onWebViewCreated(this)
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.databaseEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.allowUniversalAccessFromFileURLs = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            // On network error, automatically try the next URL in fallback list
                            if (currentUrlIndex < urls.size - 1) {
                                currentUrlIndex++
                                view?.loadUrl(urls[currentUrlIndex])
                            }
                        }
                    }

                    loadUrl(urls[0])
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFFF97316)
            )
        }
    }
}
