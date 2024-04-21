package com.example.socialspot.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialspot.DataClass.Message
import com.example.socialspot.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_receive_message,parent,false)
            return ReceiveViewHolder(view)

        }else{
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_send_message,parent, false)
            return SentViewHolder(view)
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass== SentViewHolder::class.java){
            //do the stuff for sent view holder


            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

        }else{
            //do the stuff for receive view holder
            val viewHolder= holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }
    }


    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

//    fun setMessageList(list: List<Message>) {
//        chatList.clear()
//        chatList.addAll(list)
//        notifyDataSetChanged()
//    }
//
//    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val userNameTextView: TextView = itemView.findViewById(R.id.messageTextView)
//
//        fun bind(chat: Message) {
//            // Bind chat data to UI elements
//            userNameTextView.text = chat.userName
//            // Add more bindings as needed
//        }
//    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txtsent_msg)

    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txtreceive_msg)
    }

}
