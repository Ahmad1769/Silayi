package com.example.silayi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class Clothes : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes)

        val addSizeButton = findViewById<AppCompatButton>(R.id.add_a_size)
        val addToCartButton = findViewById<AppCompatButton>(R.id.add_to_cart)

        addSizeButton.setOnClickListener {
            startActivity(Intent(this, Measurement::class.java))
            Toast.makeText(this, "Add A Size Button Clicked", Toast.LENGTH_SHORT).show()
        }

        addToCartButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
