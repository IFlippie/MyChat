package com.iflippie.mychat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            defaultScreen()
        }else{
            println("User is signed out")
        }
        btLogin.setOnClickListener { logIn() }
        btSignAct.setOnClickListener { toSignUp() }
    }

    private fun logIn(){
        val email = etLogmail.text.toString()
        val password = etLogpass.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    defaultScreen()
                    Toast.makeText(baseContext, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    println("signInWithEmail:failure ${task.exception}")
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(baseContext, "email or password is empty", Toast.LENGTH_SHORT).show()
        }
    }
    private fun defaultScreen() {
        val intent = Intent(this, DefaultActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun toSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}
