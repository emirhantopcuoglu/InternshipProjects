package com.example.wordguessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var buttonAddWordPage: Button
    private lateinit var buttonStartGame: Button
    private lateinit var buttonScoreTable: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ana ekran buton tanimlamalari
        buttonAddWordPage = findViewById(R.id.buttonAddWordPage)
        buttonStartGame = findViewById(R.id.buttonStartGame)
        buttonScoreTable = findViewById(R.id.buttonScoreTable)

        //Kelime ekle sayfasina yonlendiren buton
        buttonAddWordPage.setOnClickListener {
            val intent = Intent(this, AddWordActivity::class.java)
            startActivity(intent)
        }
        //Oyunu baslatan buton
        buttonStartGame.setOnClickListener {
            val intent = Intent(this,GameActivity::class.java )
            startActivity(intent)
        }
        //Skor tablosuna yonlendiren buton
        buttonScoreTable.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
        }
    }
}