package com.example.nikeinterviewapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeinterviewapp.R
import com.example.nikeinterviewapp.domain.WordListItem
import com.example.nikeinterviewapp.ui.viewmodel.UrbanDictionaryViewModel
import java.util.*
import kotlin.Comparator

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private lateinit var wordList: List<WordListItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWordBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.word_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wordList[position])
    }

    override fun getItemCount(): Int {
        return if (::wordList.isInitialized) wordList.size else 0
    }

    fun updatePostList(wordList: List<WordListItem>) {
        this.wordList = wordList
        notifyDataSetChanged()
    }

    fun sortWordListByThumbsUpOrDown(wordList: List<WordListItem>, thumbsUpOrDown: String) {
        this.wordList = wordList
        when (thumbsUpOrDown) {
            R.string.thumbs_up.toString() -> Collections.sort(
                this.wordList,
                thumbsUpOrderComparator
            )
            R.string.thumbs_down.toString() -> Collections.sort(
                this.wordList,
                thumbsDownOrderComparator
            )
        }
        notifyDataSetChanged()
    }

    private var thumbsUpOrderComparator: Comparator<WordListItem> =
        Comparator { wordListItem1, wordListItem2 -> if (wordListItem1.thumbs_up!! > wordListItem2.thumbs_up!!) -1 else if (wordListItem1.thumbs_up === wordListItem2.thumbs_up) 0 else 1 }

    private var thumbsDownOrderComparator: Comparator<WordListItem> =
        Comparator { wordListItem1, wordListItem2 -> if (wordListItem1.thumbs_down!! > wordListItem2.thumbs_down!!) -1 else if (wordListItem1.thumbs_down === wordListItem2.thumbs_down) 0 else 1 }

    class ViewHolder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = UrbanDictionaryViewModel()
        fun bind(wordItem: WordListItem) {
            viewModel.bind(wordItem)
            binding.viewModel = viewModel
        }
    }
}