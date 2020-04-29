package com.example.nikeinterviewapp.dagger.component


import com.example.nikeinterviewapp.dagger.module.NetworkModule
import com.example.nikeinterviewapp.ui.viewmodel.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface AppComponent {


    /**
     * Injects required dependencies into the specified SearchViewModel.
     * @param searchViewModel SearchWordInDictionaryViewModel in which to inject the dependencies
     */
    fun inject(searchViewModel: SearchViewModel)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent
        fun networkModule(networkModule: NetworkModule): Builder
    }

}