package com.micheal.randomfoxapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.colormind.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
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

        // Fetch fox images
        FetchFoxImagesTask().execute()
    }

    private inner class FetchFoxImagesTask : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg voids: Void): String {
            var response = ""
            try {
                val url = URL("https://randomfox.ca/floof/")
                val urlConnection = url.openConnection() as HttpURLConnection
                val inputStream: InputStream = urlConnection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    response += line
                }
                bufferedReader.close()
                inputStream.close()
                urlConnection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(response: String) {
            super.onPostExecute(response)
            try {
                val jsonObject = JSONObject(response)
                val imageUrl = jsonObject.getString("image")
                imageUrls.add(imageUrl)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
