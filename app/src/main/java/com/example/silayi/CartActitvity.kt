package com.example.silayi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActitvity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_actitvity)



        bottomNavigationView = findViewById(R.id.bottomNavigationView)
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