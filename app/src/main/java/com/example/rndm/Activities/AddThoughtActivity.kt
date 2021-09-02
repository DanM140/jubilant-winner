package com.example.rndm.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.rndm.*
import com.example.rndm.databinding.ActivityAddThoughtBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddThoughtActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddThoughtBinding
    var selectedCategory= Funny
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddThoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun addpostclicked(view: View){
//add post to firestore
        val data:MutableMap<String,Any> = HashMap()
        data.put(CATEGORY,selectedCategory)
        data.put(Num_Comments,0)
        data.put(Num_Likes,0)
        data.put(THOUGHT_TXT,binding.addThoughtText.text.toString())
data.put(TIMESTAMP,FieldValue.serverTimestamp())
        data.put(USERNAME, FirebaseAuth.getInstance().currentUser?.displayName.toString())
        data.put(USER_ID,FirebaseAuth.getInstance().currentUser?.uid.toString())
        FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
            .add(data)
            .addOnSuccessListener {
                finish()
            }
            .addOnFailureListener {exception ->

                Log.e("Exception","Could not add post:$exception")

            }

    }
    fun addfunnyclicked(view: View){
       if (selectedCategory== Funny){
           binding.addfunnybtn.isChecked=true
           return
       }
        binding.addseriousbtn.isChecked=false
        binding.addcrazybtn.isChecked=false
        selectedCategory= Funny
    }
    fun addseriousClicked(view: View){
        if (selectedCategory== Serious){
            binding.addseriousbtn.isChecked=true
            return
        }
        binding.addfunnybtn.isChecked=false
        binding.addcrazybtn.isChecked=false
        selectedCategory= Serious

    }
    fun addcrazyclicked(view: View){
        if (selectedCategory== Crazy){
            binding.addcrazybtn.isChecked=true
            return
        }
        binding.addseriousbtn.isChecked=false
        binding.addfunnybtn.isChecked=false
        selectedCategory= Crazy
    }
}