package com.friendfinapp.dating.ForwardMessaging


import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ChatUserListBinding
import com.friendfinapp.dating.databinding.ForwardChatItemsBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import com.friendfinapp.dating.ui.othersprofile.model.MessageViewModel
import java.util.Locale

class ForwardUserListAdapter(var context: Context, var listener: (String, String, String) -> Unit) :
    RecyclerView.Adapter<ForwardUserListAdapter.ViewHolder>() {
    private var originalChatUserList: MutableList<ChatResponseModel.Data> = ArrayList() // Store original list



    var chatUserList: MutableList<ChatResponseModel.Data> = ArrayList()

    class ViewHolder(itemView: ForwardChatItemsBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }
    fun filterList(query: String) {
        chatUserList = if (query.isEmpty()) {
            originalChatUserList
        } else {
            originalChatUserList.filter {
                it.toUsername.toString().lowercase(Locale.getDefault()).contains(query.lowercase(
                    Locale.getDefault()))
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForwardChatItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.username.text = chatUserList[position].toUsername.toString()



        if (chatUserList[position].userimage != null) {
            Log.d("TAG", "onBindViewHolder: " + chatUserList[position].userimage)
            //   var imageBytes = chatUserList[position].userimage?.toByteArray()
            Glide.with(context)
                .load(Constants.BaseUrl + chatUserList[position].userimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo)
                .into(holder.binding.userProfileImage)
        } else {
            Glide.with(context)
                .load(R.drawable.ic_user)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo)
                .into(holder.binding.userProfileImage)

        }



        holder.itemView.setOnLongClickListener {

            

            if (!ForwadCompaionList.userForwardList.contains(chatUserList[position])) {
//                Toast.makeText(context, " was not exist so add", Toast.LENGTH_SHORT).show()
                if(ForwadCompaionList.userForwardList.size==5 ){

                    Toast.makeText(context, "You can only share with up to 5 chats", Toast.LENGTH_SHORT).show()
                }
            else{
                ForwadCompaionList.userForwardList.add(chatUserList[position])
                holder.binding.tick.visibility = View .VISIBLE

                }
            } else {
                ForwadCompaionList.userForwardList.remove(chatUserList[position])
//                Toast.makeText(context, " was exist so remove", Toast.LENGTH_SHORT).show()
                holder.binding.tick.visibility = View .INVISIBLE

            }
            true
        }

        holder.binding.tick.visibility =
            if (ForwadCompaionList.userForwardList.contains(chatUserList[position]))
                View.VISIBLE
            else
                View.INVISIBLE
    }
    
    

    override fun getItemCount(): Int {
        return chatUserList.size
    }


//    @RequiresApi(Build.VERSION_CODES.N)
//    fun addData(chatItem: List<ChatResponseModel.Data>) {
//
//        val deleteAdminCheckList: MutableList<ChatResponseModel.Data> = ArrayList()
//
//        deleteAdminCheckList.addAll(chatItem)
//        deleteAdminCheckList.removeIf {
//            it.toUsername.toString() == ("admin")
//        }
//        chatUserList.addAll(deleteAdminCheckList)
//        notifyItemInserted(chatUserList.size - deleteAdminCheckList.size)
//    }


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


//
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun addDataFirstTime(chatItem: List<ChatResponseModel.Data>) {
//
//        Log.d("TAG", "addDataFirstTime: " + chatItem)
//        chatUserList.clear()
//
//        val deleteAdminCheckList: MutableList<ChatResponseModel.Data> = ArrayList()
//
//        deleteAdminCheckList.addAll(chatItem)
//        deleteAdminCheckList.removeIf {
//            it.toUsername.toString() == ("admin")
//        }
//        chatUserList.addAll(deleteAdminCheckList)
//        notifyDataSetChanged()
//    }
}
