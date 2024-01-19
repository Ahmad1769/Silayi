package com.example.silayi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MeasurementFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val currentUser = Firebase.auth.currentUser
    private val currentUserid = currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            auth = Firebase.auth
            databaseReference = FirebaseDatabase.getInstance().reference.child("measurements")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_measurement, container, false)

        val submitSizeButton = view.findViewById<AppCompatButton>(R.id.submitsize)

        submitSizeButton.setOnClickListener {
            Toast.makeText(requireContext(), "username", Toast.LENGTH_SHORT).show()
            saveMeasurements(view)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData(view)
    }
        private fun saveMeasurements(view: View) {
        val measurementsMap = mutableMapOf<String, Pair<String, String>>()
        Toast.makeText(requireContext(), currentUserid, Toast.LENGTH_SHORT).show()
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
            val measurementValue1 = view.findViewById<EditText>(editTextIds[i]).text.toString()
            val measurementValue2 = view.findViewById<EditText>(editTextIds[i + 1]).text.toString()

            if (measurementValue1.isNotEmpty() && measurementValue2.isNotEmpty()) {
                measurementsMap["pair${(i / 2) + 1}"] = Pair(measurementValue1, measurementValue2)
            } else {
                Toast.makeText(requireContext(), "Please fill all measurement fields", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (currentUserid != null) {
            databaseReference.child(currentUserid).setValue(measurementsMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Measurements saved successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to save measurements", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun populateData(view: View) {
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

                                view.findViewById<EditText>(editTextIds[i]).setText(measurementValue1)
                                view.findViewById<EditText>(editTextIds[i + 1]).setText(measurementValue2)
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch measurements data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
