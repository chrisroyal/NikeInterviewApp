package com.example.nikeinterviewapp.domain

open class BaseView<WordResponse> {

    var data: WordResponse? = null
    var error: Throwable? = null
    var currentState: Int = 0

    enum class State constructor(var value: Int) {
        LOADING(0), SUCCESS(1), FAILED(-1)
    }
}