package com.example.silayi

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val backArrowImage: ImageView = findViewById(R.id.backArrowImage)
        val signInLink: TextView = findViewById(R.id.signInLink)
        val signUpButton: AppCompatButton = findViewById(R.id.signup_btn)
        val googleButton: AppCompatButton = findViewById(R.id.googlebtn)
        val tailorCheckBox: CheckBox = findViewById(R.id.checkBox)

        backArrowImage.setOnClickListener {
            onBackPressed()
        }

        signInLink.setOnClickListener {
            startActivity(Intent(this, User_SignIn::class.java))
        }

        signUpButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.editText).text.toString()
            val email = findViewById<EditText>(R.id.editText11).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.editText12).text.toString()
            val password = findViewById<EditText>(R.id.editText13).text.toString()

            // Additional logic to determine if the user is a tailor
            val isTailor = tailorCheckBox.isChecked

            // Call your function to sign up with Firebase
            signUpWithEmailAndPassword(email, password,username,phoneNumber,isTailor)
        }

        googleButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signUpWithEmailAndPassword(email: String, password: String, username: String, phoneNumber: String, isTailor: Boolean) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    val user = auth.currentUser
                    val userId = user?.uid

                    // Store user details in Firebase Realtime Database
                    userId?.let {
                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(it)
                        val userData = HashMap<String, Any>()
                        userData["username"] = username
                        userData["email"] = email
                        userData["phoneNumber"] = phoneNumber
                        userData["isTailor"] = isTailor

                        userRef.setValue(userData).addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,Measurement::class.java))
                            } else {
                                Toast.makeText(this, "Database registration failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(baseContext, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = GoogleSignIn.getClient(this, getGoogleSignInOptions()).signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun getGoogleSignInOptions() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    Toast.makeText(this, "Google Sign In successful", Toast.LENGTH_SHORT).show()
                    // Additional logic to store user data or navigate to the next screen
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Google Sign In failed. Please try again.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
