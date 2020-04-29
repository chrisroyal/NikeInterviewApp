package com.example.nikeinterviewapp.network

import com.example.nikeinterviewapp.domain.WordResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UrbanDictionaryAPI {
    @GET("/define")
    fun searchWordFromDictionary(
        @Query("term") searchTerm: String
    ): Observable<WordResponse>

}
