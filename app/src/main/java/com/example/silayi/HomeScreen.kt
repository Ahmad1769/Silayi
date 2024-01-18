package com.example.silayi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recyclerView4: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        // Initialize your views
        recyclerView1 = findViewById(R.id.reCycleView)
        recyclerView2 = findViewById(R.id.tailor_recyclerView)
        recyclerView3 = findViewById(R.id.clothes_recyclerView)
        recyclerView4 = findViewById(R.id.recommended_recyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Setup RecyclerView Adapters (You need to create adapters for each RecyclerView)
        val adapterTailor = TailorAdapter(this)
//        val adapter2 = YourAdapter2()
//        val adapter3 = YourAdapter3()
//        val adapter4 = YourAdapter4()
//
//        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView1.adapter = adapterTailor
//
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = adapterTailor

        recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.adapter = adapterTailor

        recyclerView4.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView4.adapter = adapterTailor

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
