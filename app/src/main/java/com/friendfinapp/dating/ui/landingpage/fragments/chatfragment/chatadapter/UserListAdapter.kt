package com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.chatadapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ChatUserListBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import java.util.*
import kotlin.collections.ArrayList
class UserListAdapter(var context: Context, var listener: (String, String, String) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var chatUserList: MutableList<ChatResponseModel.Data> = ArrayList()
    private var originalChatUserList: MutableList<ChatResponseModel.Data> = ArrayList() // Store original list

    class ViewHolder(itemView: ChatUserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatUserListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = chatUserList[position]
        holder.binding.username.text = user.toUsername.toString()
        val imageUrl = if (user.userimage != null) Constants.BaseUrl + user.userimage else R.drawable.ic_user
        Glide.with(context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.logo)
            .into(holder.binding.userProfileImage)

        holder.itemView.setOnClickListener {
            listener.invoke(user.toUsername.toString().trim(), user.notificationToken.toString().trim(), user.userimage.toString())
        }
    }

    override fun getItemCount(): Int = chatUserList.size

    @RequiresApi(Build.VERSION_CODES.N)
    fun addData(chatItem: List<ChatResponseModel.Data>) {
        val filteredList = chatItem.filter { it.toUsername.toString() != "admin" }
        originalChatUserList.addAll(filteredList) // Store original list
        chatUserList.addAll(filteredList)
        notifyItemInserted(chatUserList.size - filteredList.size)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addDataFirstTime(chatItem: List<ChatResponseModel.Data>) {
        chatUserList.clear()
        originalChatUserList.clear()

        val filteredList = chatItem.filter { it.toUsername.toString() != "admin" }
        originalChatUserList.addAll(filteredList)
        chatUserList.addAll(filteredList)

        notifyDataSetChanged()
    }

    fun filterList(query: String) {
        chatUserList = if (query.isEmpty()) {
            originalChatUserList
        } else {
            originalChatUserList.filter {
                it.toUsername.toString().lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
