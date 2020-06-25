package com.iflippie.mychat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.iflippie.mychat.model.ChatRoom
import kotlinx.android.synthetic.main.item_room.view.*

class RoomAdapter (private val rooms: List<ChatRoom>) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(room: ChatRoom) {
            itemView.run {
                val userID = auth.currentUser?.uid
                val intent = Intent(itemView.context, ChatActivity::class.java)
                val extras = Bundle()
                if(room.p1 == userID){
                    roomEmail.text = room.p2Email.toString()
                    roomName.text = room.p2Name.toString()
                    extras.putString(EXTRA_NAME, room.p1Name.toString())
                } else{
                    roomEmail.text = room.p1Email.toString()
                    roomName.text = room.p1Name.toString()
                    extras.putString(EXTRA_NAME, room.p2Name.toString())
                }
                itemView.setOnClickListener{
                    extras.putString(EXTRA_UID, room.uid.toString())
                    intent.putExtras(extras)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_UID = "EXTRA_UID"
    }

}