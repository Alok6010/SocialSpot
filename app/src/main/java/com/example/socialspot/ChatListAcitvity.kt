package com.example.socialspot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialspot.DataClass.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatListAcitvity : AppCompatActivity() {

    private lateinit var chatListRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_list_acitvity)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        chatListRecyclerView = findViewById(R.id.chatListRecyclerView)
        chatListAdapter = ChatListAdapter(ArrayList())
        chatListRecyclerView.adapter = chatListAdapter
        chatListRecyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("chats")

        fetchChatList()
    }

    private fun fetchChatList() {
        val currentUserUid = auth.currentUser?.uid ?: return

        database.child(currentUserUid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chats = ArrayList<Message>()
                for (childSnapshot in snapshot.children) {
                    val chatMessage = childSnapshot.getValue(Message::class.java)
                    chatMessage?.let {
                        chats.add(it)
                    }
                }
                chatListAdapter.updateChatList(chats)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

}