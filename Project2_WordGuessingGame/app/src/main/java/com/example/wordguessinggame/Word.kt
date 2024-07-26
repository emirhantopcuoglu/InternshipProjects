package com.example.wordguessinggame

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class WordsWrapper(val kelimeler: MutableList<String>)

//JSON formatindaki bir dizi kelimeyi alan ve listeye donusturen fonksiyon
fun parseJSONToWords(jsonString: String): List<String> {
    val gson = Gson() //Gson kütüphanesinden bir nesne olustur
    val wordListType = object : TypeToken<WordsWrapper>() {}.type //JSON verisini dogru turde parse edebilmek icin bir TypeToken olustur
    val wordsWrapper: WordsWrapper = gson.fromJson(jsonString, wordListType) //string WordsWrapper turune donusturulur
    return wordsWrapper.kelimeler
}
