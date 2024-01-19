package com.example.silayi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        if (intent.getBooleanExtra("openCartFragment", false)) {
            replaceFragment(Cart_Fragment())
        } else {
            val initialFragment = Home_fragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, initialFragment).commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(Home_fragment())
                    true
                }
                R.id.category -> {
                    replaceFragment(CategoryFragment())
                    true
                }
                R.id.eye -> {
                    startActivity(Intent(this,Measurement::class.java))
                    true
                }
                R.id.cart -> {
                    replaceFragment(Cart_Fragment())
                    true
                }
//                R.id.person -> {
//                    true
//                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
