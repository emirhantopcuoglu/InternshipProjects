package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var weatherApi: WeatherApi
    private lateinit var cityNameTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var windSpeedTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var weatherIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrofit Kurulumu
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)

        // UI Ogeleri
        cityNameTextView = findViewById(R.id.cityName)
        temperatureTextView = findViewById(R.id.temperature)
        humidityTextView = findViewById(R.id.humidity)
        windSpeedTextView = findViewById(R.id.windSpeed)
        descriptionTextView = findViewById(R.id.description)
        weatherIcon = findViewById(R.id.weatherIcon)


        val searchButton = findViewById<Button>(R.id.searchButton)
        val cityNameInput = findViewById<EditText>(R.id.cityNameInput)

        searchButton.setOnClickListener {
            val cityName = cityNameInput.text.toString()
            if (cityName.isNotEmpty()) {
                fetchWeatherData(cityName)
            } else {
                Toast.makeText(this, "Lütfen bir şehir adı girin", Toast.LENGTH_SHORT).show()
            }
        }

        // Son Aranan Şehir Verilerini Yükle
        val lastCity = getLastSearchedCity()
        if (lastCity != null) {
            fetchWeatherData(lastCity)
        }
    }

    private fun fetchWeatherData(city: String) {
        val apiKey = ""
        val call = weatherApi.getWeather(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        displayWeatherData(weatherResponse)
                        saveLastSearchedCity(city)
                    }
                } else {
                    Log.e(
                        "WeatherApp",
                        "API Response not successful: ${response.errorBody()?.string()}"
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "Hava durumu bilgileri alınamadı. Lütfen tekrar deneyin.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherApp", "API Request failed", t)
                Toast.makeText(
                    this@MainActivity,
                    "İnternet bağlantınızı kontrol edin ve tekrar deneyin.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun displayWeatherData(weather: WeatherResponse) {
        cityNameTextView.text = weather.name
        val tempInCelsius = weather.main.temp - 273.15
        temperatureTextView.text = "Sıcaklık: %.2f°C".format(tempInCelsius)
        humidityTextView.text = "Nem: ${weather.main.humidity}%"
        windSpeedTextView.text = "Rüzgar Hızı: ${weather.wind.speed} m/s"
        descriptionTextView.text = weather.weather[0].description

        val iconResId = getWeatherIcon(weather.weather[0].icon)
        weatherIcon.setImageResource(iconResId)
    }


    private fun saveLastSearchedCity(city: String) {
        val sharedPreferences = getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("last_city", city).apply()
    }

    private fun getLastSearchedCity(): String? {
        val sharedPreferences = getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        return sharedPreferences.getString("last_city", null)
    }

    private fun getWeatherIcon(icon: String): Int {
        return when (icon) {
            "01d" -> R.drawable.sun
            "02d" -> R.drawable.partly_cloudy
            "03d" -> R.drawable.cloudy
            "04d" -> R.drawable.cloudy
            "09d" -> R.drawable.raining
            "10d" -> R.drawable.raining
            "11d" -> R.drawable.storm
            "13d" -> R.drawable.snow
            "50d" -> R.drawable.mist
            else -> R.drawable.sun
        }
    }
}

