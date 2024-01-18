package com.example.silayi
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Tailor_list : AppCompatActivity() {

    private lateinit var tailorRecyclerView: RecyclerView
    private lateinit var tailorAdapter:tailor_adap
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tailor_list)

        tailorRecyclerView = findViewById(R.id.tailor_category_recycleView)
        tailorAdapter = tailor_adap()
        tailorRecyclerView.layoutManager = LinearLayoutManager(this)
        tailorRecyclerView.adapter = tailorAdapter

        // Initialize Firebase and fetch tailors
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        fetchTailors()
    }

    private fun fetchTailors() {
        val query: Query = databaseReference.orderByChild("isTailor").equalTo(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tailors = mutableListOf<tailor_adap.User>()

                for (tailorSnapshot in snapshot.children) {
                    val userId = tailorSnapshot.key.toString()

                    val email = tailorSnapshot.child("email").getValue(String::class.java) ?: ""
                    val isTailor = tailorSnapshot.child("isTailor").getValue(Boolean::class.java) ?: false
                    val phoneNumber = tailorSnapshot.child("phoneNumber").getValue(String::class.java) ?: ""
                    val username = tailorSnapshot.child("username").getValue(String::class.java) ?: ""

                    val tailor = tailor_adap.User(userId, username, phoneNumber, isTailor)
                    tailors.add(tailor)
                }

                tailorAdapter.setTailors(tailors)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}