package com.iflippie.mychat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iflippie.mychat.model.ChatRoom
import com.iflippie.mychat.model.User
import kotlinx.android.synthetic.main.item_query.view.*

class QueryAdapter(private val users: List<User>) : RecyclerView.Adapter<QueryAdapter.ViewHolder>() {
    private lateinit var ref: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var friendRef: DatabaseReference
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private var currentUserName: String = auth.currentUser?.email.toString().substring(0,7)

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
                itemView.setOnClickListener {
                    val userID = auth.currentUser?.uid
                    val friendID = result.userId

                   val currentUserQuery = FirebaseDatabase.getInstance().getReference("Users").equalTo(userID)
                    currentUserQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (i in dataSnapshot.children) {
                                    val user = i.getValue(User::class.java)
                                    if (user != null) {
                                        currentUserName = user.name.toString()
                                        println("userName is $currentUserName")
                                    }else {println("it failed")}
                                }
                            }else { println("the currentUser doesn't exist")}
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            println(databaseError)
                        }
                    })

                    ref = FirebaseDatabase.getInstance().getReference("ChatRooms")
                    userRef = FirebaseDatabase.getInstance().getReference("/Users/$userID").child("rooms")
                    friendRef = FirebaseDatabase.getInstance().getReference("/Users/$friendID").child("rooms")
                    val refKey = ref.push().key
                    val newChat = ChatRoom(
                        uid = refKey,
                        p1 = userID,
                        p1Email = auth.currentUser?.email,
                        p1Name = currentUserName,
                        p2 = result.userId,
                        p2Email = result.email,
                        p2Name = result.name
                    )
                    if(refKey != null) {
                        ref.child(refKey).setValue(newChat).addOnCompleteListener {
                            Toast.makeText(itemView.context, "Data Saved Successfully",
                                Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(itemView.context, "Data not saved, please try again",
                                Toast.LENGTH_SHORT).show()
                        }
                        userRef.setValue(refKey).addOnCompleteListener {
                            println("roomId saved in currentUser")
                        }.addOnFailureListener {
                            println("roomId failed in currentUser")
                        }
                        friendRef.setValue(refKey).addOnCompleteListener {
                            println("roomId saved in otherUser")
                        }.addOnFailureListener {
                            println("roomId failed in otherUser")
                        }
                    }
                    val intent = Intent(itemView.context, DefaultActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}