package com.example.smartkitbeta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.smartkitbeta.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this, LogInActivity::class.java))
            //finish this activity
            finish()
        },4000)
    }
}
