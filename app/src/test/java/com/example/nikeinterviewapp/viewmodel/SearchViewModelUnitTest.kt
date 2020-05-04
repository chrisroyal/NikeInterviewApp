package com.example.nikeinterviewapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.nikeinterviewapp.domain.WordListItem
import com.example.nikeinterviewapp.domain.WordListView
import com.example.nikeinterviewapp.domain.WordListView.Companion.ERROR_STATE
import com.example.nikeinterviewapp.domain.WordListView.Companion.LOADING_STATE
import com.example.nikeinterviewapp.domain.WordListView.Companion.SUCCESS_STATE
import com.example.nikeinterviewapp.domain.WordResponse
import com.example.nikeinterviewapp.network.UrbanDictionaryAPI
import com.example.nikeinterviewapp.ui.viewmodel.SearchViewModel
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*


@RunWith(JUnit4::class)
class SearchViewModelUnitTest {
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    var apiClient: UrbanDictionaryAPI? = null

    @Mock
    var observer: Observer<WordListView>? = null
    private var wordsListItem: List<WordListItem>? = null
    private var viewModel: SearchViewModel? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        wordsListItem = ArrayList()
        viewModel = SearchViewModel()
        viewModel!!.getWordListState().observeForever(observer!!)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testNull() {
        Mockito.`when`(apiClient!!.searchWordFromDictionary("test"))
            .thenReturn(null)
        Assert.assertNotNull(viewModel!!.getWordListState())
        Assert.assertTrue(viewModel!!.getWordListState().hasObservers())
    }

    @Test
    fun testFetchDataSuccess() {
        // Mock API response
        Mockito.`when`(apiClient!!.searchWordFromDictionary("test"))
            .thenReturn(
                Observable.just(
                    WordResponse(wordsListItem)
                )
            )
        viewModel!!.loadWordLists("test")
        Mockito.verify(observer)!!.onChanged(
            LOADING_STATE
        )
        Mockito.verify(observer)!!.onChanged(
            SUCCESS_STATE
        )
    }

    @Test
    fun testFetchDataError() {
        Mockito.`when`(apiClient!!.searchWordFromDictionary("test"))
            .thenReturn(Observable.error(Throwable("\"Api Error")))
        viewModel!!.loadWordLists("test")
        Mockito.verify(observer)!!.onChanged(
            LOADING_STATE
        )
        Mockito.verify(observer)!!.onChanged(
            ERROR_STATE
        )
    }

    @After
    fun tearDown() {
        apiClient = null
        viewModel = null
    }
}