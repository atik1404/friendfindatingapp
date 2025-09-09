package com.friendfinapp.dating.ui.chatsearch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendfinapp.dating.databinding.ItemSearchSuggestionBinding
import com.friendfinapp.dating.ui.chatsearch.model.SuggestionResponseModel


class SearchSuggestionAdapter(val context: Context,private val listener: (String) -> Unit): RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder>() {
    private var suggestions: List<SuggestionResponseModel.Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchSuggestionBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = suggestions[position]

      holder.binding.tvText.text=product.username.toString()

        holder.itemView.setOnClickListener {
            listener.invoke(suggestions[holder.adapterPosition].username.toString())
        }
    }

    override fun getItemCount(): Int = suggestions.size

    fun addData(suggestions: List<SuggestionResponseModel.Data>) {
        this.suggestions = suggestions
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemSearchSuggestionBinding): RecyclerView.ViewHolder(binding.root)
}