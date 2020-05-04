package com.example.nikeinterviewapp.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import com.example.nikeinterviewapp.domain.WordListItem;
import com.example.nikeinterviewapp.domain.WordListView;
import com.example.nikeinterviewapp.domain.WordResponse;
import com.example.nikeinterviewapp.network.UrbanDictionaryAPI;
import com.example.nikeinterviewapp.ui.viewmodel.SearchViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SearchViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UrbanDictionaryAPI apiClient;

    @Mock
    Observer<WordListView> observer;

    private List<WordListItem> wordsListItem ;
    private SearchViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        wordsListItem = new ArrayList<>();
        viewModel = new SearchViewModel();
        viewModel.getWordListState().observeForever(observer);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void testNull() {
        when(apiClient.searchWordFromDictionary("test")).thenReturn(null);
        assertNotNull(viewModel.getWordListState());
        assertTrue(viewModel.getWordListState().hasObservers());
    }

    @Test
    public void testFetchDataSuccess() {
        // Mock API response
        when(apiClient.searchWordFromDictionary("test")).thenReturn(Observable.just(new WordResponse(wordsListItem)));
        viewModel.loadWordLists("test");
        verify(observer).onChanged(WordListView.getLOADING_STATE());
        verify(observer).onChanged(WordListView.getSUCCESS_STATE());

    }

    @Test
    public void testFetchDataError() {
        when(apiClient.searchWordFromDictionary("test")).thenReturn(Observable.<WordResponse>error(new Throwable("\"Api Error")));
        viewModel.loadWordLists("test");
        verify(observer).onChanged(WordListView.getLOADING_STATE());
        verify(observer).onChanged(WordListView.getERROR_STATE());
    }

    @After
    public void tearDown() {
        apiClient = null;
        viewModel = null;
    }
}
