package com.example.rndm.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rndm.Modal.Thought
import com.example.rndm.Num_Likes
import com.example.rndm.R
import com.example.rndm.THOUGHTS_REF
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThoughtsAdapter(val thoughts:ArrayList<Thought>, val itemClick:(Thought)-> Unit): RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent?.context).inflate(R.layout.thought_list_view,parent,false)
        return ViewHolder(view,itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindThought(thoughts[position])
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }
    inner class ViewHolder(itemView: View,val itemClick: (Thought) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val username= itemView?.findViewById<TextView>(R.id.listViewUsername)
        val timeStamp=itemView?.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtTxt= itemView?.findViewById<TextView>(R.id.listViewThoughtTxt)
        val numLikes= itemView?.findViewById<TextView>(R.id.listViewNumLikes)
        val likesImage= itemView?.findViewById<ImageView>(R.id.listViewLikesImage)
val numComments =itemView?.findViewById<TextView>(R.id.numCommentslbl)
        fun bindThought(thought: Thought){
            username?.text=thought.username
            thoughtTxt?.text=thought.thoughtText
            numLikes?.text=thought.numLikes.toString()
            numComments?.text=thought.numComments.toString()
            val dateFormatter=SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            timeStamp?.text=  dateFormatter.format(thought.timeStamp)
            itemView.setOnClickListener { itemClick(thought) }
            likesImage?.setOnClickListener {
                FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thought.documentId)
                    .update(Num_Likes, thought.numLikes?.plus(1))
            }

        }
    }
}