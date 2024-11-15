package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingWelcomeBinding

class BoardingWelcomeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding= ActivityBoardingWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityBoardingWelcomeBtnSignUp.setOnClickListener {
            Log.d("MainActivity", "Sign Up Button Clicked")
            // force a crash
            throw RuntimeException("Test Crash")
            val intent = Intent(this, BoardingSignUpActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityBoardingWelcomeTvLogin.setOnClickListener {
            val intent = Intent(this, BoardingLoginActivity::class.java)
            startActivity(intent)
        }
    }
}