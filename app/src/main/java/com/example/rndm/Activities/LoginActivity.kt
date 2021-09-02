package com.example.rndm.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.rndm.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
    }
    fun loginLoginClicked(view:View){
//perform login
        val email = binding.loginEmailText.text.toString()
        val pass=binding.loginPasswordTxt.text.toString()
        auth.signInWithEmailAndPassword(email,pass)
            .addOnSuccessListener {
                finish()
            }
            .addOnFailureListener { exception->
                Log.e("Exception","Could not  sign in user:${exception.localizedMessage}")
            }


    }
    fun loginCreatedClicked(view: View){
//segue to the create user activity
      val createIntent=Intent(this, CreateUserActivity::class.java)
    startActivity(createIntent)
    }


}