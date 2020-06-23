package com.iflippie.mychat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class QueryActivity : AppCompatActivity() {
    private val ref: FirebaseDatabase by lazy { FirebaseDatabase.getInstance()}
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private lateinit var emailQuery: Query
    private val totalResults = mutableListOf<User>()
    private val queryAdapter = QueryAdapter(totalResults)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        val userEmail = intent.getStringExtra(DefaultActivity.ADD_USER_EMAIL)
            emailQuery = ref.getReference("Users").orderByChild("email").equalTo(userEmail)

            emailQuery.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        totalResults.clear()
                        for (i in dataSnapshot.children){
                            val user = dataSnapshot.getValue(User::class.java)

                            user?.let {
                                totalResults.add(it)
                                queryAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                override fun onCancelled(dataBaseError: DatabaseError) {
                    println(dataBaseError)
                }
            })

    }
}
