package com.example.silayi

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Measurement : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val currentUser = Firebase.auth.currentUser
    private val currentUserid = currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement)

        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().reference.child("measurements")
        val submitSizeButton = findViewById<AppCompatButton>(R.id.submitsize)
        populateData()
        submitSizeButton.setOnClickListener {
            Toast.makeText(this,"username",Toast.LENGTH_SHORT).show()
            saveMeasurements()
            finish()
        }
    }

    private fun saveMeasurements() {
            val measurementsMap = mutableMapOf<String, Pair<String, String>>()
            Toast.makeText(this,currentUserid,Toast.LENGTH_SHORT).show()
            val editTextIds = arrayOf(
                R.id.shoulder11, R.id.shoulder12,
                R.id.shoulder21, R.id.shoulder22,
                R.id.shoulder31, R.id.shoulder32,
                R.id.shoulder41, R.id.shoulder42,
                R.id.shoulder51, R.id.shoulder52,
                R.id.shoulder61, R.id.shoulder62,
                R.id.shoulder71, R.id.shoulder72
            )

            for (i in 0 until editTextIds.size step 2) {
                val measurementValue1 = findViewById<EditText>(editTextIds[i]).text.toString()
                val measurementValue2 = findViewById<EditText>(editTextIds[i + 1]).text.toString()

                if (measurementValue1.isNotEmpty() && measurementValue2.isNotEmpty()) {
                    measurementsMap["pair${(i / 2) + 1}"] = Pair(measurementValue1, measurementValue2)
                } else {
                    // Show error message or handle empty input
                    Toast.makeText(this, "Please fill all measurement fields", Toast.LENGTH_SHORT).show()
                    return
                }
            }

        if (currentUserid != null) {
            databaseReference.child(currentUserid).setValue(measurementsMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Measurements saved successfully", Toast.LENGTH_SHORT).show()

                        // Return to the home activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to save measurements", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun populateData() {
        // Check if the user is logged in
        currentUserid?.let { uid ->
            databaseReference.child(uid).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val measurementsSnapshot = task.result
                    if (measurementsSnapshot.exists()) {
                        val measurementsMap = measurementsSnapshot.value as Map<*, *>
                        val editTextIds = arrayOf(
                            R.id.shoulder11, R.id.shoulder12,
                            R.id.shoulder21, R.id.shoulder22,
                            R.id.shoulder31, R.id.shoulder32,
                            R.id.shoulder41, R.id.shoulder42,
                            R.id.shoulder51, R.id.shoulder52,
                            R.id.shoulder61, R.id.shoulder62,
                            R.id.shoulder71, R.id.shoulder72
                        )

                        for (i in 0 until editTextIds.size step 2) {
                            val pairKey = "pair${(i / 2) + 1}"
                            if (measurementsMap.containsKey(pairKey)) {
                                val pair = measurementsMap[pairKey] as Map<*, *>
                                val measurementValue1 = pair["first"].toString()
                                val measurementValue2 = pair["second"].toString()

                                findViewById<EditText>(editTextIds[i]).setText(measurementValue1)
                                findViewById<EditText>(editTextIds[i + 1]).setText(measurementValue2)
                            }
                        }
                    }
                } else {
                    // Handle the error
                    Toast.makeText(this, "Failed to fetch measurements data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
