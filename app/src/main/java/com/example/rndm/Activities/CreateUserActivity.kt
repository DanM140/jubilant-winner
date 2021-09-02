package com.example.rndm.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.rndm.DATE_CREATED
import com.example.rndm.USERNAME
import com.example.rndm.USERS_REF
import com.example.rndm.databinding.ActivityCreateUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CreateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateUserBinding
    lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

    }
    fun createcreatecliked(view: View){
        val email=binding.createEmailtxt.text.toString()
        val pass=binding.createPasswordtxt.text.toString()
        val username=binding.createUsernametxt.text.toString()
auth.createUserWithEmailAndPassword(email,pass)
    .addOnSuccessListener { result->
       //user created
        val changeRequest=UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        result.user?.updateProfile(changeRequest)
            ?.addOnFailureListener { exception->
                Log.e("Exception","Could not update display name:${exception.localizedMessage}")
            }
        val data:MutableMap<String,Any> = HashMap()
        data.put(USERNAME,username)
        data.put(DATE_CREATED, FieldValue.serverTimestamp())
        FirebaseFirestore.getInstance().collection(USERS_REF).document(result.user!!.uid)
            .set(data)
            .addOnSuccessListener { finish() }
            .addOnFailureListener { exception->
                Log.e("Exception","Could not add user document:${exception.localizedMessage}")
            }
    }
    .addOnFailureListener { exception->
        Log.e("Exception","Could not create user:${exception.localizedMessage}")
    }
    }
    fun createcancelclicked(view:View){

    }

}