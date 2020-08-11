package com.example.smartkitbeta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartkitbeta.MainActivity
import com.example.smartkitbeta.R
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logInBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
    }
}
