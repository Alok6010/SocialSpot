package com.example.socialspot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialspot.DataClass.Message

class ChatListAdapter(private var chatList: List<Message>) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    // Function to update the chat list
    fun updateChatList(newChatList: List<Message>) {
        chatList = newChatList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatList[position]
        holder.messageTextView.text = chatMessage.message
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}
