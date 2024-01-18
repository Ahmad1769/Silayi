package com.example.silayi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActitvity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var findTailorButton: AppCompatButton
    private lateinit var applyCouponCodeEditText: EditText
    private lateinit var applyButton: AppCompatButton
    private lateinit var subtotalText: TextView
    private lateinit var subtotalAmountText: TextView
    private lateinit var taxTotalText: TextView
    private lateinit var taxAmountText: TextView
    private lateinit var totalText: TextView
    private lateinit var totalAmountText: TextView
    private lateinit var proceedToCheckoutButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_actitvity)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        cartRecyclerView = findViewById(R.id.cart_recycleView)
        findTailorButton = findViewById(R.id.findtailor)
        applyCouponCodeEditText = findViewById(R.id.applycouponcode)
        applyButton = findViewById(R.id.button4)
        subtotalText = findViewById(R.id.subtotal)
        subtotalAmountText = findViewById(R.id.subtotalamount)
        taxTotalText = findViewById(R.id.taxtotal)
        taxAmountText = findViewById(R.id.taxamount)
        totalText = findViewById(R.id.total)
        totalAmountText = findViewById(R.id.totalamount)
        proceedToCheckoutButton = findViewById(R.id.proceedtocheckout)

        // Set up RecyclerView (assuming you have an adapter and data to display)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        // cartRecyclerView.adapter = YourAdapter()

        // Set click listeners or any other necessary setup

        // Example of click listener for the proceed to checkout button
        proceedToCheckoutButton.setOnClickListener {
            // Implement your proceed to checkout button logic here
        }

        // Example of click listener for the find tailor button
        findTailorButton.setOnClickListener {
            // Implement your find tailor button logic here
        }

        // Example of click listener for the apply button
        applyButton.setOnClickListener {
            // Implement your apply button logic here
        }

        // Handle BottomNavigationView item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                //home
                R.id.home -> {
                    intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                    true
                }
                //category list
                R.id.category -> {
                    // Handle click on item 2
                    // Add your navigation logic or start the corresponding activity
                    true
                }
                //size
                R.id.eye -> {
                    // Handle click on item 2
                    // Add your navigation logic or start the corresponding activity
                    true
                }
                //cart
                R.id.cart -> {
                    intent = Intent(this, CartActitvity::class.java)
                    startActivity(intent)
                    true
                }
                //account
                R.id.person -> {
                    // Handle click on item 2
                    // Add your navigation logic or start the corresponding activity
                    true
                }

                // Add more cases for other items if needed
                else -> false
            }
        }
    }
}