package com.example.nikeinterviewapp.domain

class WordListView private constructor(
    data: WordResponse?,
    currentState: Int,
    error: Throwable?
) : BaseView<WordResponse>() {

    init {
        this.data = data
        this.error = error
        this.currentState = currentState
    }

    companion object {
        //Used for SearchViewModel Unit Testing

        @JvmStatic
        var ERROR_STATE =
            WordListView(null, State.FAILED.value, Throwable())
        @JvmStatic
        var LOADING_STATE =
            WordListView(null, State.LOADING.value, null)
        @JvmStatic
        var SUCCESS_STATE =
            WordListView(WordResponse(emptyList()), State.SUCCESS.value, null)
    }
}