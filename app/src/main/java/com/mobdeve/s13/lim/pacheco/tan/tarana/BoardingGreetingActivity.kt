package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingGreetingBinding

class BoardingGreetingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityBoardingGreetingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val username= intent.getStringExtra(User.USERNAME_KEY)
        viewBinding.activityBoardingGreetingMainCl.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            intent.putExtra(User.USERNAME_KEY, username)
            startActivity(intent)
        }
    }
}