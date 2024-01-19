package com.example.silayi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Clothes : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes)
        val addSizeButton = findViewById<AppCompatButton>(R.id.add_a_size)
        val addToCartButton = findViewById<AppCompatButton>(R.id.add_to_cart)
        val clothesNameTextView = findViewById<TextView>(R.id.clothesNameTextView)
        val productKeywordTextView = findViewById<TextView>(R.id.productKeywordTextView)
        val priceTextView = findViewById<TextView>(R.id.price_tag)

        val intent = intent
        val clothesName = intent.getStringExtra("clotheName")
        val productKeyword = intent.getStringExtra("clotheDescription")
        val price = intent.getIntExtra("clothePrice", 0)

        clothesNameTextView.text = clothesName
        productKeywordTextView.text = productKeyword
        priceTextView.text=price.toString()

        addSizeButton.setOnClickListener {
            startActivity(Intent(this, Measurement::class.java))
            Toast.makeText(this, "Add  Size Button Clicked", Toast.LENGTH_SHORT).show()
        }

        addToCartButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {

            }
            if (currentUser != null) {
                val userId = currentUser.uid
                val productName = clothesName
                val quantity = 1
                val totalPrice = quantity * price

                val databaseReference = FirebaseDatabase.getInstance().getReference("carts").child(userId)
                if (productName != null) {

                    databaseReference.child(productName).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val existingQuantity = dataSnapshot.child("quantity").getValue(Long::class.java) ?: 0
                                val newQuantity = existingQuantity.toInt() + quantity
                                val existingprice = dataSnapshot.child("totalPrice").getValue(Long::class.java) ?: 0
                                val newprice = existingprice.toInt() + totalPrice

                                // Update quantity
                                databaseReference.child(productName).child("quantity").setValue(newQuantity)
                                    .addOnCompleteListener { quantityUpdateTask ->
                                        if (quantityUpdateTask.isSuccessful) {
                                            Toast.makeText(this@Clothes, "Quantity updated successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@Clothes, "Failed to update quantity", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                databaseReference.child(productName).child("totalPrice").setValue(newprice)
                                    .addOnCompleteListener { priceUpdateTask ->
                                        if (priceUpdateTask.isSuccessful) {
                                            Toast.makeText(this@Clothes, "Total price updated successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@Clothes, "Failed to update total price", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this@Clothes, databaseReference.toString(), Toast.LENGTH_SHORT).show()
                                databaseReference.child(productName).child("quantity").setValue(quantity)
                                    .addOnCompleteListener { quantityUpdateTask ->
                                        if (quantityUpdateTask.isSuccessful) {
                                            Toast.makeText(this@Clothes, "Quantity added successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@Clothes, "Failed to add quantity", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                databaseReference.child(productName).child("totalPrice").setValue(totalPrice)
                                    .addOnCompleteListener { priceUpdateTask ->
                                        if (priceUpdateTask.isSuccessful) {
                                            Toast.makeText(this@Clothes, "Total price added successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@Clothes, "Failed to add total price", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }

                            val homeIntent = Intent(this@Clothes, HomeScreen::class.java)
                            homeIntent.putExtra("openCartFragment", true)
                            startActivity(homeIntent)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(this@Clothes, "Error accessing database", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(this, "Please log in to add items to the cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
