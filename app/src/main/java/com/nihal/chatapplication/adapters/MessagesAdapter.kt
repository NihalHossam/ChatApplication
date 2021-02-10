package com.nihal.chatapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.nihal.chatapplication.R
import com.nihal.chatapplication.model.Chat


class MessagesAdapter(): RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {


    private var sender =  FirebaseAuth.getInstance()

    private  var chatList =  ArrayList<Chat>()

    private val MESSAGE_TYPE_RIGHT = 1  //sender
    private val MESSAGE_TYPE_LEFT = 0   //receiver

    override fun getItemViewType(position: Int): Int {
        if(chatList[position].sender.equals(sender.currentUser?.uid) ){
            return MESSAGE_TYPE_RIGHT
        }else{
            return MESSAGE_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.MessagesViewHolder {
        //sender
        if(viewType == MESSAGE_TYPE_RIGHT){
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.chat_message_right_item, parent, false)
            return MessagesViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.chat_message_left_item, parent, false)
            return MessagesViewHolder(view)
        }
    }

    /**
     * Gets a list to set it to the RecyclerView Adapter.
     * @param list The list to be submitted.
     */
    fun submitList(list: List<Chat>?) {
        if (list != null) {
            chatList = list as ArrayList<Chat>
        }
    }

    /**
     * Gets the total number of items in this adapter.
     * @return an integer that represents the number of items in adapter.
     */
    override fun getItemCount(): Int {
        return chatList.size
    }

    /**
     * Associates a viewholder with data.
     * @param holder The ViewHolder that represents the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MessagesAdapter.MessagesViewHolder, position: Int) {
        val currentChat = chatList[position]
        holder.showMessage.text = currentChat.message

        if (position == chatList.size - 1) {
            if (currentChat.isSeen) {
                holder.seenText.text = "Seen"
            } else {
                holder.seenText.text = "Delivered"
            }
        } else {
            holder.seenText.visibility = View.GONE
        }
    }

    /**
     * Holds specifications for the views in the RecyclerView item row.
     */
    inner class MessagesViewHolder(view: View) : RecyclerView.ViewHolder(view.rootView) {
        var showMessage: TextView = view.findViewById(R.id.showMessage)
        var seenText: TextView = view.findViewById(R.id.textSeen)
    }


}