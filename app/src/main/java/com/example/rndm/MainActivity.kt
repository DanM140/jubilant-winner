package com.example.rndm
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rndm.Activities.AddThoughtActivity
import com.example.rndm.Activities.CommentsActivity
import com.example.rndm.Activities.LoginActivity
import com.example.rndm.Adapters.ThoughtsAdapter
import com.example.rndm.Modal.Thought
import com.example.rndm.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {
    var selectedCategory = Funny
    lateinit var thoughtsAdapter: ThoughtsAdapter
    val thoughts= arrayListOf<Thought>()
    val thoughtsCollectionRef=FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
    private lateinit var binding: ActivityMainBinding
    lateinit var thoughtsListener:ListenerRegistration
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            val addthoughtintent = Intent(this, AddThoughtActivity::class.java)
            startActivity(addthoughtintent)
        }
        thoughtsAdapter = ThoughtsAdapter(thoughts){ thought ->  
           val CommentsActivity = Intent(this,CommentsActivity::class.java)
            CommentsActivity.putExtra(DOCUMENT_KEY,thought.documentId)
            startActivity(CommentsActivity)
        }
        binding.cont.thoughtListView.adapter = thoughtsAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.cont.thoughtListView.layoutManager = layoutManager
        auth = FirebaseAuth.getInstance()

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
      menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.getItem(0)
        if (auth.currentUser== null){
          //logged out state
            menuItem.title="Login"
        } else {
            //logged in
            menuItem.title="Logout"

        }
        return super.onPrepareOptionsMenu(menu)
    }
 fun updateUI(){
     if(auth.currentUser == null){
         binding.cont.MainCrazybtn.isEnabled = false
     binding.cont.MainSeriousbtn.isEnabled = false
     binding.cont.MainFunnybtn.isEnabled = false
     binding.cont.MainPopularbtn.isEnabled = false
         binding.fab.isEnabled=false
         thoughts.clear()
         thoughtsAdapter.notifyDataSetChanged()
     }else{
         binding.cont.MainCrazybtn.isEnabled = true
         binding.cont.MainSeriousbtn.isEnabled = true
         binding.cont.MainFunnybtn.isEnabled = true
         binding.cont.MainPopularbtn.isEnabled = true
         binding.fab.isEnabled=true
         setListener()
     }

 }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_login) {
            if (auth.currentUser == null) {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                auth.signOut()
                updateUI()
            }
            return true
        }
        return false
    }

fun setListener(){
    if(selectedCategory== Popular){
        //show popular documents
        thoughtsListener=thoughtsCollectionRef
            .orderBy(Num_Likes, Query.Direction.DESCENDING)
            .addSnapshotListener(this){documents,exception ->
                if (exception !=null){
                    Log.e("Exception", "Could not retrieve post:$exception")

                }
                if (documents != null){
                    //parse out the data
                    parseData(documents)
                }
            }

    }else{
        thoughtsListener=thoughtsCollectionRef
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .whereEqualTo(CATEGORY,selectedCategory)
            .addSnapshotListener(this){documents,exception ->
                if (exception !=null){
                    Log.e("Exception", "Could not retrieve post:$exception")

                }
                if (documents != null){
                    //parse out the data
parseData(documents)
                }
            }
    }



}
    fun parseData(documents:QuerySnapshot){
        thoughts.clear()

            for (document in documents) {
                val data = document.data
                val name = data[USERNAME] as? String
                val timeStamp=data[TIMESTAMP] as Timestamp
                val thoughtTxt = data[THOUGHT_TXT] as? String
                val numLikes = data[Num_Likes] as? Long
                val numComments = data[Num_Comments] as? Long
                val documentId = document.id
                val userId=data[USER_ID] as  String
                val newThought = Thought(
                    name,
                    timeStamp.toDate(),
                    thoughtTxt,
                    numLikes?.toInt(),
                    numComments?.toInt(),
                    documentId,
                    userId
                )
                thoughts.add(newThought)
            }

        thoughtsAdapter.notifyDataSetChanged()
    }
        fun mainFunnyClicked(view: View) {
            if (selectedCategory == Funny) {
                binding.cont.MainFunnybtn.isChecked = true
                return
            }
            binding.cont.MainSeriousbtn.isChecked = false
            binding.cont.MainCrazybtn.isChecked = false
            binding.cont.MainPopularbtn.isChecked = false
            selectedCategory = Funny
            thoughtsListener.remove()
            setListener()
        }

        fun mainSeriousClicked(view: View) {
            if (selectedCategory == Serious) {
                binding.cont.MainSeriousbtn.isChecked = true
                return
            }
            binding.cont.MainFunnybtn.isChecked = false
            binding.cont.MainCrazybtn.isChecked = false
            binding.cont.MainPopularbtn.isChecked = false
            selectedCategory = Serious
            thoughtsListener.remove()
            setListener()

        }

        fun mainCrazyClicked(view: View) {
            if (selectedCategory == Crazy) {
                binding.cont.MainCrazybtn.isChecked = true
                return
            }
            binding.cont.MainSeriousbtn.isChecked = false
            binding.cont.MainFunnybtn.isChecked = false
            binding.cont.MainPopularbtn.isChecked = false
            selectedCategory = Crazy
            thoughtsListener.remove()
            setListener()
        }

        fun mainPopularClicked(view: View) {
            if (selectedCategory == Popular) {
                binding.cont.MainPopularbtn.isChecked = true
                return
            }
            binding.cont.MainSeriousbtn.isChecked = false
            binding.cont.MainFunnybtn.isChecked = false
            binding.cont.MainCrazybtn.isChecked = false
            selectedCategory = Popular
            thoughtsListener.remove()
            setListener()
        }}