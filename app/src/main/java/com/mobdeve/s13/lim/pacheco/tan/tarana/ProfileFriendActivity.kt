package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileFriendBinding

class ProfileFriendActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityProfileFriendBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.profileUser1.setOnClickListener{
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }
    }

}