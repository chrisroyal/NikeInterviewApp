package com.example.nikeinterviewapp.ui.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.nikeinterviewapp.R
import com.example.nikeinterviewapp.domain.WordListItem
import com.example.nikeinterviewapp.domain.WordListView
import com.example.nikeinterviewapp.domain.WordResponse
import com.example.nikeinterviewapp.network.UrbanDictionaryAPI
import com.example.nikeinterviewapp.ui.main.ListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel : MainViewModel() {

    @Inject
    lateinit var dictionaryAPI: UrbanDictionaryAPI
    private lateinit var subscription: Disposable
    private val wordListView = MutableLiveData<WordListView>()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadWordLists(searchTerm = "") }
    val wordListAdapter: ListAdapter = ListAdapter()


    fun getWordListState(): MutableLiveData<WordListView> {
        return wordListView
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadWordLists(searchTerm: String) {
        subscription = dictionaryAPI
            .searchWordFromDictionary(searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveWordListStart() }
            .doOnTerminate { onRetrieveWordListFinish() }
            .subscribe(
                { result -> onRetrieveWordListSuccess(result) },
                { error -> onRetrieveWordListError(error) }
            )
    }

    fun sortWordListByThumbsUpOrDown(searchTerm: String, thumbsUpOrDown: String) {
        subscription = dictionaryAPI
            .searchWordFromDictionary(searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveWordListStart() }
            .doOnTerminate { onRetrieveWordListFinish() }
            .subscribe(
                { result -> onSortWordListSuccess(result, thumbsUpOrDown) },
                { error -> onRetrieveWordListError(error) }
            )
    }

    private fun onRetrieveWordListStart() {
        wordListView.postValue(WordListView.LOADING_STATE)
        loadingVisibility.value = View.VISIBLE

        if (WordListView.SUCCESS_STATE.data != null)
            wordListView.postValue(WordListView.SUCCESS_STATE)
        if (WordListView.ERROR_STATE.error != null)
            wordListView.postValue(WordListView.ERROR_STATE)

    }

    private fun onRetrieveWordListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveWordListSuccess(response: WordResponse) {
        val wordList: List<WordListItem?> = response.list
        wordListAdapter.updatePostList(wordList as List<WordListItem>)

        WordListView.SUCCESS_STATE.data = response
    }

    private fun onSortWordListSuccess(response: WordResponse, thumbsUpOrDown: String) {
        val wordList: List<WordListItem?> = response.list
        wordListAdapter.sortWordListByThumbsUpOrDown(
            wordList as List<WordListItem>,
            thumbsUpOrDown
        )

        WordListView.SUCCESS_STATE.data = response
    }

    private fun onRetrieveWordListError(error: Throwable) {
        WordListView.ERROR_STATE.error = error
        wordListView.postValue(WordListView.ERROR_STATE)
        errorMessage.value = R.string.post_error
    }
}