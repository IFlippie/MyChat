package com.iflippie.mychat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_query.view.*

class QueryAdapter(private val users: List<User>) : RecyclerView.Adapter<QueryAdapter.ViewHolder>() {
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
                //setOnClickListener { onClick.invoke(friend) }
                itemView.setOnClickListener(){
                    Toast.makeText(itemView.context, "Click worked.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}