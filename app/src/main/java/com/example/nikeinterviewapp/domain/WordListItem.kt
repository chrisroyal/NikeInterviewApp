package com.example.nikeinterviewapp.domain

data class WordListItem(
    val definition: String? = null,
    val permalink: String? = null,
    val thumbs_up: Int? = null,
    val soundUrls: List<Any?>? = null,
    val author: String? = null,

    val word: String? = null,
    val defid: Int? = null,
    val current_vote: String? = null,
    val writtenOn: String? = null,
    val example: String? = null,
    val thumbs_down: Int? = null
)