package uk.ac.chester.kotlinhelp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebViewClient
import uk.ac.chester.kotlinhelp.databinding.ActivityWebSearchBinding

class WebSearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebSearchBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val url = savedInstanceState?.getString("url") ?: intent.getStringExtra("url")


        binding.webView.webViewClient = WebViewClient() //prevents opening in browser app
        binding.webView.settings.javaScriptEnabled = true //required for kotlin and android sites
        binding.webView.loadUrl(url!!)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", binding.webView.url)
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).putExtra("url", binding.webView.url)
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }
}