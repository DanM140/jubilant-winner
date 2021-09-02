package com.example.rndm.Modal

import com.google.firebase.Timestamp
import java.util.*

data class Thought(
    val username: String?, val timeStamp: Date,
    val thoughtText: String?, val numLikes: Int?, val numComments: Int?, val documentId: String,val userId:String
)