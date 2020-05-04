package com.example.nikeinterviewapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nikeinterviewapp.dagger.component.AppComponent
import com.example.nikeinterviewapp.dagger.component.DaggerAppComponent
import com.example.nikeinterviewapp.dagger.module.NetworkModule

abstract class MainViewModel : ViewModel() {

    private val injector: AppComponent= DaggerAppComponent
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Inject the required dependencies using for Dagger
     */
    private fun inject() {
        when (this) {
            is SearchViewModel -> injector.inject(this)
        }
    }
}