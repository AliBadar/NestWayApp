package com.example.hackatonprjoect.visioglobe.ui.compose

import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceInfo

@Composable
fun WebView(
    modifier: Modifier = Modifier,
    placeInfo: PlaceInfo = PlaceInfo()
) {

    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            //loadUrl(mUrl)
            val lHTMLHead = ("<head><link rel='stylesheet' type='text/css' href='visioglobe.css'/>"
                    + "<style type=\"text/css\">body { font-size: 16.0px; zoom: 90%;}"
                    + "</style></head>")
            val lHTMLBody = "<body dir='auto'>" + placeInfo.description + "</body>"
            val lHTML = "<html>$lHTMLHead$lHTMLBody</html>"
            val lBaseURL = "file:///android_asset/"
            loadDataWithBaseURL(lBaseURL, lHTML, "text/html", "UTF8", null)
            isScrollbarFadingEnabled = true
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            setBackgroundColor(Color.TRANSPARENT)
            setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        }
    }, update = {
        val lHTMLHead = ("<head><link rel='stylesheet' type='text/css' href='visioglobe.css'/>"
                + "<style type=\"text/css\">body { font-size: 16.0px; zoom: 90%;}"
                + "</style></head>")
        val lHTMLBody = "<body dir='auto'>" + placeInfo.description + "</body>"
        val lHTML = "<html>$lHTMLHead$lHTMLBody</html>"
        val lBaseURL = "file:///android_asset/"
        it.loadDataWithBaseURL(lBaseURL, lHTML, "text/html", "UTF8", null)
        it.setBackgroundColor(Color.TRANSPARENT)
        it.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
    }, modifier = modifier)
}