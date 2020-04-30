package com.example.nikeinterviewapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.nikeinterviewapp.domain.WordListItem
import com.example.nikeinterviewapp.domain.WordListView
import com.example.nikeinterviewapp.domain.WordResponse
import com.example.nikeinterviewapp.network.UrbanDictionaryAPI
import com.example.nikeinterviewapp.ui.viewmodel.SearchViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.concurrent.Callable

@RunWith(JUnit4::class)
class UrbanDictionaryViewModelTest {
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    var apiClient: UrbanDictionaryAPI? = null

    @Mock
    var observer: Observer<WordListView>? = null
    private var wordListItem: List<WordListItem>? = null
    private var viewModel: SearchViewModel? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        wordListItem = ArrayList<WordListItem>()
        viewModel = SearchViewModel()
        observer?.let { viewModel!!.getWordListState().observeForever(it) }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(object :
            Function<Callable<Scheduler?>?, Scheduler> {
            override fun apply(scheduler: Callable<Scheduler?>?): Scheduler {
                return Schedulers.trampoline()
            }
        })
    }

    @Test
    fun testNull() {
        Mockito.`when`<Any?>(apiClient?.searchWordFromDictionary("test")).thenReturn(null)
        Assert.assertNotNull(viewModel?.getWordListState())
        Assert.assertTrue(viewModel?.getWordListState()!!.hasObservers())
    }

    @Test
    fun testFetchDataSuccess() {
        // Mock API response
        Mockito.`when`<Any>(apiClient.searchWordFromDictionary("test"))
            .thenReturn(Observable.just<WordResponse>(WordResponse(wordListItem)))
        viewModel?.loadWordLists("test")
        Mockito.verify<Observer<WordListView>?>(observer)
            .onChanged(WordListView.getLOADING_STATE())
        Mockito.verify<Observer<WordListView>?>(observer)
            .onChanged(WordListView.getSUCCESS_STATE())
    }

    @Test
    fun testFetchDataError() {
        Mockito.`when`<Any>(apiClient.searchWordFromDictionary("test"))
            .thenReturn(Observable.error<WordResponse>(Throwable("\"Api Error")))
        viewModel?.loadWordLists("test")
        Mockito.verify<Observer<WordListView>?>(observer)
            .onChanged(WordListView.getLOADING_STATE())
        Mockito.verify<Observer<WordListView>?>(observer)
            .onChanged(WordListView.getERROR_STATE())
    }

    @After
    fun tearDown() {
        apiClient = null
        viewModel = null
    }
}