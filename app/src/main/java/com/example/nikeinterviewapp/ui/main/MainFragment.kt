package com.example.nikeinterviewapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.nikeinterviewapp.databinding.ActivityWordsListBinding
import com.example.nikeinterviewapp.ui.viewmodel.SearchViewModel
import androidx.appcompat.widget.SearchView
import com.example.nikeinterviewapp.R

class MainFragment : Fragment() {
    private lateinit var binding: ActivityWordsListBinding
    private lateinit var viewModel: SearchViewModel
    private var errorSnackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.wordlist_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureBindingAndViewModelValues()
    }

    private fun configureBindingAndViewModelValues() {
//        Used Android Data Binding to access elements of XML
        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.wordlist_activity)
        binding.wordsList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.switchLikesVsDisLikes.setOnCheckedChangeListener { _, isChecked ->
            configureSortingByThumbsUpOrDown(isChecked)
        }
        configureSearchViewForDynamicStringSearch()

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
    }

    private fun configureSortingByThumbsUpOrDown(isChecked: Boolean) {
//    Assumed switch isChecked to SortBy Thumbs Up Count and vice versa
        if (isChecked) {
            viewModel.sortWordListByThumbsUpOrDown(
                binding.search.query.toString(),
                R.string.thumbs_up.toString()
            )
        } else {
            viewModel.sortWordListByThumbsUpOrDown(
                binding.search.query.toString(),
                R.string.thumbs_down.toString()
            )
        }
    }

    private fun configureSearchViewForDynamicStringSearch() {
        binding.search.isActivated = true
        binding.search.queryHint = getString(R.string.query_hint)
        binding.search.onActionViewExpanded()
        binding.search.isIconified = false
        binding.search.clearFocus()

        binding.search.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.loadWordLists(query)
                return false
            }

            override fun onQueryTextChange(searchTerm: String): Boolean {
                return false
            }
        })
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}