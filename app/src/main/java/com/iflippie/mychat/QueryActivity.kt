package com.iflippie.mychat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_query.*

class QueryActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private val totalResults = mutableListOf<User>()
    private val queryAdapter = QueryAdapter(totalResults)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        rvQuery.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvQuery.adapter = queryAdapter

        val userEmail = intent.getStringExtra(DefaultActivity.ADD_USER_EMAIL)
           val emailQuery = FirebaseDatabase.getInstance().getReference("/Users")
               .orderByChild("email")
               .equalTo(userEmail)

            emailQuery.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        totalResults.clear()
                        for (i in dataSnapshot.children){
                            val user = i.getValue(User::class.java)

                            user?.let {
                                totalResults.add(it)
                            }
                        }
                        println(totalResults.size)
                        println(totalResults)
                        queryAdapter.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(dataBaseError: DatabaseError) {
                    println(dataBaseError)
                }
            })

    }
}
