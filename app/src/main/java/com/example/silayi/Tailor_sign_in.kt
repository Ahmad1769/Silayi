package com.example.silayi

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Tailor_sign_in : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: AppCompatButton
    private lateinit var googleButton: AppCompatButton
    private lateinit var facebookButton: AppCompatButton
    private lateinit var forgetPasswordText: TextView
    private lateinit var auth: FirebaseAuth

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tailor_sign_in)

        auth = Firebase.auth

        emailEditText = findViewById(R.id.email1)
        passwordEditText = findViewById(R.id.editText131)
        signInButton = findViewById(R.id.signup_btn1)
        googleButton = findViewById(R.id.googlebtn1)
        facebookButton = findViewById(R.id.facebookbtn1)
        forgetPasswordText = findViewById(R.id.forgetPassword1)
        val backArrowImage: ImageView = findViewById(R.id.backArrowImage1)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            signInWithEmailAndPassword(email, password)
        }

        forgetPasswordText.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Password reset email sent",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Failed to send password reset email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show()
            }
        }

        googleButton.setOnClickListener {
//            signInWithGoogle()
            Toast.makeText(this, "Google sign-in logic not implemented", Toast.LENGTH_SHORT)
                .show()

        }

        facebookButton.setOnClickListener {
            Toast.makeText(this, "Facebook sign-in logic not implemented", Toast.LENGTH_SHORT)
                .show()
        }

        backArrowImage.setOnClickListener {
            val userIntent = Intent(this, MainActivity::class.java)
            startActivity(userIntent)
        }

        val registerLink: TextView = findViewById(R.id.registerLink1)
        registerLink.setOnClickListener {
            val registerIntent = Intent(this, tailor_registration::class.java)
            startActivity(registerIntent)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    checkIsTailerTag(user?.uid)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
    private fun checkIsTailerTag(userId: String?) {
        if (userId != null) {
            val userReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val isTailer = snapshot.child("isTailor").getValue(Boolean::class.java)

                        if (isTailer == true) {
                            onSignInSuccess()
                        } else {
                            Toast.makeText(baseContext, "You are not a tailer. Please use the user screen.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }    override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, "Error checking 'istailer' tag.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun onSignInSuccess() {
        val intent = Intent(this, OrderListActivity::class.java)
//        Toast.makeText(this,"Hello going to intent",Toast.LENGTH_SHORT).show()
//        startActivity(intent)
        finish()
    }

//    private fun signInWithGoogle() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(this, gso)
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    // Handle the result of Google sign-in
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // Authenticate with Firebase using Google credentials
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI or navigate to the next screen
//                    onSignInSuccess()
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//    }
}
