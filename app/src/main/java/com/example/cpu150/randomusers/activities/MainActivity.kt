package com.example.cpu150.randomusers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Go to home activity
        startActivity(Intent(this, HomePageActivity::class.java))

        // Close activity
        finish ()
    }
}
