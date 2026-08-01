package com.example.symptomsync

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
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
import androidx.webkit.WebViewAssetLoader

class MainActivity : ComponentActivity() {

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
                        activity = this,
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
    activity: ComponentActivity,
    onWebViewCreated: (WebView) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    val assetLoader = remember {
        WebViewAssetLoader.Builder()
            .setDomain("appassets.androidplatform.net")
            .addPathHandler("/", WebViewAssetLoader.AssetsPathHandler(activity))
            .build()
    }

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
                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {
                            val url = request?.url ?: return null
                            val path = url.path ?: ""

                            // Standard assetLoader response
                            val response = assetLoader.shouldInterceptRequest(url)
                            if (response != null) return response

                            // Handle route paths without .html extension (e.g. /analyze, /diet, /settings, /feedback, /login)
                            try {
                                var assetPath = if (path.startsWith("/")) path.substring(1) else path
                                if (assetPath.isEmpty()) assetPath = "index.html"
                                if (!assetPath.contains(".")) {
                                    assetPath = "$assetPath.html"
                                }

                                val inputStream = context.assets.open(assetPath)
                                val mimeType = if (assetPath.endsWith(".html")) "text/html" 
                                    else if (assetPath.endsWith(".css")) "text/css"
                                    else if (assetPath.endsWith(".js")) "application/javascript"
                                    else "text/plain"
                                return WebResourceResponse(mimeType, "UTF-8", inputStream)
                            } catch (e: Exception) {
                                // Fallback to index.html for SPA client-side routing
                                try {
                                    val inputStream = context.assets.open("index.html")
                                    return WebResourceResponse("text/html", "UTF-8", inputStream)
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }
                            return super.shouldInterceptRequest(view, request)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }

                    loadUrl("https://appassets.androidplatform.net/index.html")
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
