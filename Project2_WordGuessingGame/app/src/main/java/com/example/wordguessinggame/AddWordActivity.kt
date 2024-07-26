package com.example.wordguessinggame

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.io.IOException

class AddWordActivity : AppCompatActivity(){
    private lateinit var newWord: EditText
    private lateinit var buttonAddWord: Button
    private lateinit var buttonBackToGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        newWord = findViewById(R.id.newWord)
        buttonAddWord = findViewById(R.id.buttonAddWord)
        buttonBackToGame = findViewById(R.id.buttonBackToGame)

        buttonAddWord.setOnClickListener {
            val newWord_ = newWord.text.toString()
            if ((newWord_.isNotEmpty())){
                addWordToJson(newWord_)
                Toast.makeText(this, "Kelime eklendi!", Toast.LENGTH_SHORT).show()
                newWord.text.clear()
            }else{
                Toast.makeText(this, "Lütfen bir kelime girin", Toast.LENGTH_SHORT).show()
            }
        }
        buttonBackToGame.setOnClickListener {
            finish()
        }
    }

    private fun addWordToJson(newWord: String){
        val jsonFile = File(filesDir, "kelimeler.json")

        val wordsWrapper: WordsWrapper = if (jsonFile.exists()) {
            val json = jsonFile.readText()
            val listType = object : TypeToken<WordsWrapper>() {}.type
            Gson().fromJson(json, listType)
        } else {
            WordsWrapper(mutableListOf())
        }
        wordsWrapper.kelimeler.add(newWord)
        try {
            val writer = FileWriter(jsonFile)
            val gson = Gson()
            gson.toJson(wordsWrapper, writer)
            writer.flush()
            writer.close()
        }catch (ex: IOException){
            ex.printStackTrace()
        }
    }
}