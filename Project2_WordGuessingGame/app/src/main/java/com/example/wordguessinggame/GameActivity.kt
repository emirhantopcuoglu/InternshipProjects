package com.example.wordguessinggame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class GameActivity : AppCompatActivity() {
    private lateinit var hiddenWordView: TextView
    private lateinit var keyboard: GridLayout
    private lateinit var guessWord: EditText
    private lateinit var buttonGuess: Button
    private lateinit var hiddenWord: String
    private lateinit var displayedWord: CharArray
    private lateinit var buttonBackToMain: Button
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Buton tanimlamalari
        hiddenWordView = findViewById(R.id.hiddenWord)
        keyboard = findViewById(R.id.keyboard)
        guessWord = findViewById(R.id.guessWord)
        buttonGuess = findViewById(R.id.buttonGuess)
        buttonBackToMain = findViewById(R.id.buttonBackToMain)

        //readJsonFromAsset fonk ile okunan json dosyasi, json degiskenine atanir
        val json = readJsonFromAsset("kelimeler.json")
        if(json != null){
            val words = parseJSONToWords(json) //kelime listesi words'de saklanir
            hiddenWord = getRandomWord(words) //getRandomWord fonk ile random bir kelime secilir
            displayedWord = CharArray(hiddenWord.length) {'_'} //gizli kelimenin uzunlugu kadar "_' konulur
            hiddenWordView.text = String(displayedWord) //gizli kelime ekranda gosterilir
        } else {
            Toast.makeText(this, "JSON dosyası okunamadı", Toast.LENGTH_LONG).show()
        }
        keyboard() //klavye cagrilir

        //Tahmin butonu
        buttonGuess.setOnClickListener {
            val guess = guessWord.text.toString()
            if(guess.equals(hiddenWord, true)){
                score += 100
                saveScoreToPreferences(score)
                showScore()
            }else{
                guessWord.error = "Yanlış tahmin!"
                score -= 50
                saveScoreToPreferences(score)
            }
        }
        //Ana menu butonu
        buttonBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //json dosyasindan kelime okuyan fonksiyon
    fun readJsonFromAsset(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    //Random kelime secme fonksiyonu
    fun getRandomWord(wordList: List<String>): String {
        return wordList.random()
    }
    //Harf tahmini icin klavye fonksiyonu
    private fun keyboard() {
        val alphabet =  listOf(
            'a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h', 'ı', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'ö', 'p', 'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'
        )
        for (char in alphabet) {
            val button = Button(this).apply {
                text = char.toString()
                setOnClickListener {
                    onLetterGuess(char, this)
                }
            }
            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(3, 3, 3, 3)
            }
            button.layoutParams = params
            keyboard.addView(button)
        }
    }

    private fun onLetterGuess(letter: Char, button: Button) {
        var isGuessTrue = false //Kullanicinin tahmininin dogru olup olmadigini belirten bir degisken
        for (i in hiddenWord.indices) {
            if (hiddenWord[i].toLowerCase() == letter) {
                displayedWord[i] = hiddenWord[i]
                isGuessTrue = true
            }
        }
        hiddenWordView.text = String(displayedWord)
        if (!isGuessTrue) {
            button.isEnabled = false
        }
    }

    private fun saveScoreToPreferences(score: Int) { // "ScorePrefs" Skorlari saklamak icin kullanilacak SharedPreferences dosyasi
        //Context.MODE_PRIVATE ile SharedPreferences dosyasi yalnizca bu uygulama tarafından okunabilir ve yazilabilir
        val sharedPreferences: SharedPreferences = getSharedPreferences("ScorePrefs", Context.MODE_PRIVATE) //SharedPreferences nesnesi alinir
        val editor: SharedPreferences.Editor = sharedPreferences.edit() //SharedPreferences nesnesinden bir Editor nesnesi alinir. Bu nesne, SharedPreferences uzerinde degisiklik yapmamizi saglar.
        editor.putInt("score", score) //Skor kaydedilir
        editor.apply() //degisiklikler kaydedilir
    }

    private fun showScore() {
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("score", score)
        startActivity(intent)
    }

}