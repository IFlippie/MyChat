package com.iflippie.mychat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btSignUp.setOnClickListener { signUp() }

    }

    private fun signUp(){
        val email = etmail.text.toString()
        val password = etpass.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    println("createUserWithEmail:success")
                    val userID = auth.currentUser?.uid
                    ref = FirebaseDatabase.getInstance().getReference("/Users/$userID")
                    val user = User(
                        userId = userID,
                        email = email,
                        password = password,
                        rooms = listOf()
                    )
                    ref.setValue(user)

                    loginScreen()

                } else {
                    // If sign in fails, display a message to the user.
                    println("createUserWithEmail:failure ${task.exception}")
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
            }
        }else {
            Toast.makeText(baseContext, "email or password is empty failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun defaultScreen() {
        val intent = Intent(this, defaultActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}