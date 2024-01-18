package com.example.silayi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var userButton :androidx.appcompat.widget.AppCompatButton = findViewById(R.id.user)
        var serviceButton:androidx.appcompat.widget.AppCompatButton = findViewById(R.id.service)

        userButton.setOnClickListener {
            Toast.makeText(this,"Users",Toast.LENGTH_SHORT).show()
            val userIntent = Intent(this, User_SignIn::class.java)
            startActivity(userIntent)
        }

        serviceButton.setOnClickListener {
            Toast.makeText(this,"Service",Toast.LENGTH_SHORT).show()
            val serviceIntent = Intent(this, Tailor_sign_in::class.java)
            startActivity(serviceIntent)
        }
    }
}

