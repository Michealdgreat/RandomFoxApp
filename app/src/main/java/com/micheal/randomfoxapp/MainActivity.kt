package com.micheal.randomfoxapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var adapter: ImageAdapter
    private val imageUrls = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        adapter = ImageAdapter(this, imageUrls)
        gridView.adapter = adapter

        // Fetch multiple fox images using coroutines
        fetchMultipleFoxImages()
    }

    private fun fetchMultipleFoxImages() {
        val numberOfImages = 10 // Change this number as needed

        CoroutineScope(Dispatchers.IO).launch {
            repeat(numberOfImages) {
                try {
                    val response = URL("https://randomfox.ca/floof/").readText()
                    val imageUrl = JSONObject(response).getString("image")
                    // Update adapter on the main thread
                    withContext(Dispatchers.Main) {
                        imageUrls.add(imageUrl)
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
