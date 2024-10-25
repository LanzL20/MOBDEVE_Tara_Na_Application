package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingSignupBinding

class BoardingSignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityBoardingSignupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityBoardingSignupBtnSignup.setOnClickListener {
            val intent = Intent(this, BoardingGreetingActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityBoardingSignupTvLogin.setOnClickListener {
            val intent = Intent(this, BoardingLoginActivity::class.java)
            startActivity(intent)
        }

    }
}