package com.example.silayi

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var addNewLocationButton: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var easypaisaButton: AppCompatButton
    private lateinit var jazzcashButton: AppCompatButton
    private lateinit var visaButton: AppCompatButton
    private lateinit var totalAmountText: TextView
    private lateinit var subTotalText: TextView
    private lateinit var confirmButton: AppCompatButton
    private lateinit var cancelButton: AppCompatButton

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        locationEditText = findViewById(R.id.editTextTextPersonName)
        addNewLocationButton = findViewById(R.id.addNewLocation)
        recyclerView = findViewById(R.id.checkout_recycle_view)
        easypaisaButton = findViewById(R.id.easypaisa)
        jazzcashButton = findViewById(R.id.jazzcash)
        visaButton = findViewById(R.id.visa)
        totalAmountText = findViewById(R.id.totalamount)
        subTotalText = findViewById(R.id.subamount)
        confirmButton = findViewById(R.id.confirm)
        cancelButton = findViewById(R.id.cancel)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("carts").child(userId)
            fetchCartItems()
            confirmButton.setOnClickListener {
                val address = locationEditText.text.toString().trim()
                if (address.isEmpty()) {
                    Toast.makeText(this, "Please enter a delivery address", Toast.LENGTH_SHORT).show()
                } else {
                    confirmOrder(address)
                }
            }

            cancelButton.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun fetchCartItems() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems: MutableList<Cartitem> = mutableListOf()

                for (itemSnapshot in snapshot.children) {
                    val productName = itemSnapshot.key.toString()
                    val quantity = itemSnapshot.child("quantity").getValue(Int::class.java) ?: 0
                    val totalPrice = itemSnapshot.child("totalPrice").getValue(Int::class.java) ?: 0

                    val cartItem = Cartitem(productName, quantity, totalPrice)
                    cartItems.add(cartItem)
                }
                recyclerView.adapter = CartAdapter(cartItems)
                 subTotalText.text = calculateSubtotal(cartItems).toString()
                 totalAmountText.text = calculateTotal(cartItems).toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CheckoutActivity, "Error accessing database", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun calculateSubtotal(cartItems: List<Cartitem>): Int {
        return cartItems.sumByDouble { it.totalPrice.toDouble() }.toInt()
    }

    private fun calculateTotal(cartItems: List<Cartitem>): Int {
        return calculateSubtotal(cartItems) // Add tax or any other charges here
    }


    private fun confirmOrder(address: String) {
        databaseReference.removeValue()
        Toast.makeText(this, "Order confirmed! Delivery address: $address", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,HomeScreen::class.java))
    }
}
