package com.iflippie.mychat.old

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iflippie.mychat.User
import com.iflippie.mychat.R
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter (private val messages: List<User>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: User) {
            itemView.run {
                //messageTextView.text = message.text.toString()
                //messengerTextView.text = message.name.toString()
            }
        }
    }

}