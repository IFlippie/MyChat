package com.iflippie.mychat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_query.view.*

class QueryAdapter(private val users: List<User>) : RecyclerView.Adapter<QueryAdapter.ViewHolder>() {
    private lateinit var ref: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_query, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(result: User) {
            itemView.run {
                queryEmail.text = result.email.toString()
                queryName.text = result.name.toString()
                itemView.setOnClickListener(){
                    val userID = auth.currentUser?.uid
                    ref = FirebaseDatabase.getInstance().getReference("ChatRooms")
                    userRef = FirebaseDatabase.getInstance().getReference("Users").child("rooms")
                    val refKey = ref.push().key
                    val newChat = ChatRoom(
                            uid = refKey,
                            p1 = userID,
                            p2 = result.userId,
                            messagesP1 = listOf(),
                            messagesP2 = listOf()
                    )
                    if(refKey != null) {
                        ref.child(refKey).setValue(newChat).addOnCompleteListener {
                            Toast.makeText(itemView.context, "Data Saved Succesfully",
                                Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(itemView.context, "Data not saved, please try again",
                                Toast.LENGTH_SHORT).show()
                        }
                        userRef.setValue(refKey)
                    }
                    val intent = Intent(itemView.context, DefaultActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}