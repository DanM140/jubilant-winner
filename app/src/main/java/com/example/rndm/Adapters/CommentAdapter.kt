package com.example.rndm.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rndm.Modal.Comment
import com.example.rndm.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
class CommentAdapter(val comments:ArrayList<Comment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=
            LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindComment(comments[position])
    }

    override fun getItemCount(): Int {
        return comments.count()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username= itemView?.findViewById<TextView>(R.id.commentListUsername)
        val timeStamp=itemView?.findViewById<TextView>(R.id.commentListTimestamp)
        val commentTxt= itemView?.findViewById<TextView>(R.id.commentListCommentTxt)

        fun bindComment(comment: Comment){
            username?.text=comment.username
            commentTxt?.text=comment.commentTxt
            val dateFormatter= SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            timeStamp?.text=  dateFormatter.format(comment.timeStamp)

        }
    }
}