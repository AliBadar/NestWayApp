package com.example.hackatonprjoect

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("url") ?: "https://google.com"

        setContent {
            WebScreen(url, this)
        }
    }

    // ✅ JS bridge class
    class WebAppInterface(private val activity: ComponentActivity) {

        @JavascriptInterface
        fun onMessage(json: String) {
            try {
                val jsonObject = org.json.JSONObject(json)
                val type = jsonObject.getString("type")
                val destination = jsonObject.getString("destination")

                activity.runOnUiThread {
                    Log.d("WebAppInterface", "Got: $type → $destination")
                    Toast.makeText(activity, "Got: $type → $destination", Toast.LENGTH_SHORT).show()

                    val intent = Intent(activity, MapActivity::class.java).apply {
                        putExtra("type", type)
                        putExtra("destination", destination)
                    }
                    activity.startActivity(intent)
                }
            } catch (e: Exception) {
                Log.e("WebAppInterface", "Failed to parse JSON", e)
            }
        }

    }
}

@Composable
fun WebScreen(url: String, activity: ComponentActivity) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true)

                }

                clearCache(true)
                clearHistory()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.allowContentAccess = true
                settings.allowFileAccess = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                webViewClient = WebViewClient()

                // ✅ Add JS bridge → web can call AndroidBridge.sendData()
                addJavascriptInterface(WebViewActivity.WebAppInterface(activity), "AndroidBridge")

                loadUrl(url)
            }
        }
    )
}
