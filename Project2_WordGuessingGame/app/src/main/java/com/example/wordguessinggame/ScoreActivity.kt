package com.example.wordguessinggame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreActivity : AppCompatActivity(){
    private lateinit var scoreView: TextView
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        scoreView = findViewById(R.id.score)
        buttonBack = findViewById(R.id.buttonBack)

        val score = intent.getIntExtra("score", getSavedScore())
        scoreView.text = score.toString()

        saveScore(score)

        buttonBack.setOnClickListener {
            finish() //Aktiviteyi sonlandir
        }

    }
    //Bu fonksiyon, verilen skoru (score) SharedPreferences icine kaydeder
    private fun saveScore(score: Int){
        val sharedPref = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt("HIGH_SCORE", score)
            apply()
        }
    }
    //Bu fonksiyon,SharedPreferences dosyasindan "score" anahtari ile saklanmis olan skoru alır ve döner. Eger boyle bir skor bulunamazsa, 0 doner.
    private fun getSavedScore(): Int {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ScorePrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("score", 0)  // Varsayılan değeri 0 olarak belirle
    }
}