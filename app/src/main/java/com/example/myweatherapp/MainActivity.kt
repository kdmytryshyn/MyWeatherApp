package com.example.myweatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "Austin, TX"
    val API: String = "dab3af44de7d24ae7ff86549334e45bd"
    val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
//                val url = WEATHER_URL + '?q='
                response = URL("$WEATHER_URL?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception){
                response= null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))

                val tempC = main.getString("temp") + " C"
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                //populate text views
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.temp_c).text = tempC
//                findViewById<TextView>(R.id.address).text = address
//                findViewById<TextView>(R.id.address).text = address

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            }
            catch (e: java.lang.Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
}