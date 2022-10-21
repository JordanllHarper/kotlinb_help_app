package uk.ac.chester.kotlinhelp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import uk.ac.chester.kotlinhelp.databinding.ActivityMainBinding
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var prevUrl: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        toggleButtonsState(false)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        prevUrl = sharedPref.getString("prevUrl", "")

        binding.previousButton.visibility = View.VISIBLE





        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                toggleButtonsState(binding.etSearch.text.length > 1)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        //initialising buttons
        binding.googleButton.setOnClickListener {
            loadWebActivity("https://google.co.uk/search?q=$encodedSearchTerm")
        }
        binding.stackOverflowButton.setOnClickListener {
            loadWebActivity("https://stackoverflow.com/search?q=$encodedSearchTerm")
        }
        binding.kotlinDocsButton.setOnClickListener {
            loadWebActivity("https://kotlinlang.org/docs/home.html?q=$encodedSearchTerm&s=full")
        }
        binding.androidDevButton.setOnClickListener {
            loadWebActivity("https://developer.android.com/s/?q=$encodedSearchTerm")
        }
        binding.previousButton.setOnClickListener {
            loadWebActivity(prevUrl ?: "")
        }


    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val url = result.data?.getStringExtra("url")
            if (url != null) {
                prevUrl = url
                binding.previousButton.visibility = View.VISIBLE
            } else {
                binding.previousButton.visibility = View.INVISIBLE
            }
        }

    private fun loadWebActivity(url: String) {
        val intent = Intent(this, WebSearchActivity::class.java)
        intent.putExtra("url", url)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("prevUrl", url)
            apply()
        }
        resultLauncher.launch(intent)
    }

    private fun toggleButtonsState(enabled: Boolean) {
        binding.googleButton.isEnabled = enabled
        binding.kotlinDocsButton.isEnabled = enabled
        binding.stackOverflowButton.isEnabled = enabled
        binding.androidDevButton.isEnabled = enabled
    }


    private val encodedSearchTerm: String
        get() = URLEncoder.encode(binding.etSearch.text.toString())
}