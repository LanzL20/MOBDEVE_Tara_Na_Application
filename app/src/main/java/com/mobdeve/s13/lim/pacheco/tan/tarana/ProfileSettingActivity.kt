package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileSettingsBinding

class ProfileSettingActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // NAVIGATION BUTTONS

        viewBinding.inboxIcon.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }
        viewBinding.settingsIcon.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

        viewBinding.logOutBtn.setOnClickListener {
            val intent = Intent(this, BoardingWelcomeActivity::class.java)
            Firebase.auth.signOut()
            UserSession.clearUser()
            startActivity(intent)
        }
    }
}