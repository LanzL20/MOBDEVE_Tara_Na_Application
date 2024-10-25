package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingLoginBinding

class BoardingLoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityBoardingLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityBoardingLoginBtnLogin.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityBoardingLoginTvSignUp.setOnClickListener {
            val intent = Intent(this, BoardingSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}