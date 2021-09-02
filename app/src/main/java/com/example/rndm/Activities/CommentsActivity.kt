package com.example.rndm.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rndm.*
import com.example.rndm.Adapters.CommentAdapter
import com.example.rndm.Modal.Comment
import com.example.rndm.Modal.Thought
import com.example.rndm.databinding.ActivityCommentsBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    lateinit var thoughtDocumentId: String
    lateinit var commentAdapter:CommentAdapter
    val comments = arrayListOf<Comment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        thoughtDocumentId= intent.getStringExtra(DOCUMENT_KEY).toString()
commentAdapter = CommentAdapter(comments)
        binding.commentListView.adapter=commentAdapter
        val layoutManager=LinearLayoutManager(this)
        binding.commentListView.layoutManager=layoutManager
        FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId).collection(
            COMMENTS_REF)
            .orderBy(TIMESTAMP,Query.Direction.DESCENDING)
            .addSnapshotListener{ snapshot,exception->
          if (exception != null){
              Log.e("Exception:","Could not retrieve comments ${exception.localizedMessage}")
          }
            if (snapshot != null){
                comments.clear()
                for (document in snapshot) {
                    val data = document.data
                    val name = data[USERNAME] as? String
                    val timeStamp=data[TIMESTAMP] as Timestamp
                    val commentTxt = data[COMMENT_Txt] as? String
                    val documentId=document.id
                    val userId = data[USER_ID] as String
                    val newComment= Comment(
                        name,
                        timeStamp.toDate(),
                        commentTxt,documentId,userId
                    )
                    comments.add(newComment)
                }
                commentAdapter.notifyDataSetChanged()
            }
        }
    }
    fun addCommentClicked(view: View){
val commentTxt=binding.enterCommentText.text.toString()
        val thoughtRef=FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)
    FirebaseFirestore.getInstance().runTransaction { transaction ->
val thought = transaction.get(thoughtRef)
        val numComments= thought.getLong(Num_Comments)?.plus(1)
    transaction.update(thoughtRef, Num_Comments,numComments)
    val newCommentRef =FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
        .document(thoughtDocumentId).collection(COMMENTS_REF).document()
        val data = HashMap<String,Any>()
        data.put(COMMENT_Txt,commentTxt)
        data.put(TIMESTAMP,FieldValue.serverTimestamp())
        data.put(USERNAME, FirebaseAuth.getInstance().currentUser?.displayName.toString())
        data.put(USER_ID,FirebaseAuth.getInstance().currentUser?.uid.toString())
    transaction.set(newCommentRef,data)
    }
        .addOnSuccessListener {
          binding.enterCommentText.setText("")
        }
        .addOnFailureListener { exception ->
            Log.e("Exception:","Could not add comment ${exception.localizedMessage}")
        }
    }






}
