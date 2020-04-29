package com.example.nikeinterviewapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.nikeinterviewapp.domain.WordListItem

class UrbanDictionaryViewModel : MainViewModel() {

    private val wordTitle = MutableLiveData<String>()
    private val wordDef = MutableLiveData<String>()
    private val thumbsUpCount = MutableLiveData<Int>()
    private val thumbsDownCount = MutableLiveData<Int>()

    fun bind(word: WordListItem){
        wordTitle.value = word.word
        wordDef.value = word.definition
        thumbsUpCount.value = word.thumbs_up
        thumbsDownCount.value = word.thumbs_down
    }

    fun getWordTitle(): MutableLiveData<String> {
        return wordTitle
    }

    fun getWordDefinition(): MutableLiveData<String> {
        return wordDef
    }

    fun getThumbsUpCount(): MutableLiveData<Int> {
        return thumbsUpCount
    }
    fun getThumbsDownCount(): MutableLiveData<Int> {
        return thumbsDownCount
    }
}