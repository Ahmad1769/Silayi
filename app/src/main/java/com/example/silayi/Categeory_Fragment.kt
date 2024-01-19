package com.example.silayi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class CategoryFragment : Fragment() {

    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userNumber: TextView
    private lateinit var clothesCategory: TextView
    private lateinit var tailorCategory: TextView
    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categeory_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        userImage = view.findViewById(R.id.userImage)
        userName = view.findViewById(R.id.userName)
        userNumber = view.findViewById(R.id.userNumber)
        clothesCategory = view.findViewById(R.id.clothesCategory)
        tailorCategory = view.findViewById(R.id.tailorCategory)
        logoutButton = view.findViewById(R.id.logoutButton)


        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        userId?.let {
            val userReference = databaseReference.child("users").child(it)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.child("username").getValue().toString()
                    val usernum = snapshot.child("phoneNumber").getValue().toString()
                    userName.text = username
                    userNumber.text = usernum
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }
}