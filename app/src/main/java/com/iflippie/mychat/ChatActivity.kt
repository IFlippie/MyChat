package com.iflippie.mychat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iflippie.mychat.model.Messages
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    private val totalMessages = mutableListOf<Messages>()
    private val chatAdapter = ChatAdapter(totalMessages)
    private val ref: FirebaseDatabase by lazy { FirebaseDatabase.getInstance()}
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private lateinit var messagesQuery : Query
    private var usernameString: String? = null
    private var uidString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val extras = intent.extras
        usernameString = extras?.getString(RoomAdapter.EXTRA_NAME)
        uidString = extras?.getString(RoomAdapter.EXTRA_UID)
        rvMessages.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMessages.adapter = chatAdapter
        messagesQuery = ref.getReference("ChatRooms/${uidString}/messagesList")

        messagesQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalMessages.clear()
                    for (i in dataSnapshot.children) {
                        val messages = i.getValue(Messages::class.java)
                        messages?.let {
                            totalMessages.add(it)
                            chatAdapter.notifyDataSetChanged()
                        }
                        println("INFO $usernameString $uidString")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println(databaseError)
            }
        })

        sendButton.setOnClickListener{
            sendMessage()
        }
    }

    private fun sendMessage(){
        val theText = etMessages.text.toString()
        if(theText.isEmpty()){
            Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show()
        } else {
            val sendRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("ChatRooms").child(uidString!!).child("messagesList")

            val refKey = sendRef.push().key
            val message = Messages(
                uid = refKey,
                email = auth.currentUser?.email,
                name = usernameString,
                tekst = theText
            )
                if(refKey != null){
                    sendRef.child(refKey).setValue(message).addOnCompleteListener {
                        Toast.makeText(this, "Data Saved Successfully",
                            Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Data not saved, please try again",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
