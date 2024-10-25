package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileUserBinding

class ProfileUserActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.lakwatsaBtn.setOnClickListener {
            val intent = Intent(this, LakwatsaListActivity::class.java)
            startActivity(intent)
        }
        viewBinding.addFriendBtn.setOnClickListener {
            val intent = Intent(this, FriendsAddActivity::class.java)
            startActivity(intent)
        }
        viewBinding.albumBtn.setOnClickListener {
            val intent = Intent(this, AlbumViewActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityProfileFriendsLl.setOnClickListener {
            val intent = Intent(this, FriendsViewActivity::class.java)
            startActivity(intent)
        }
        viewBinding.friendsIcon.setOnClickListener {
            val intent = Intent(this, FriendsViewActivity::class.java)
            startActivity(intent)
        }
        viewBinding.scheduleBtn.setOnClickListener {
            //TODO:("Add ScheduleActivity")
            //val intent = Intent(this, ScheduleActivity::class.java)
            //startActivity(intent)
        }

        // NAVIGATION BUTTONS

        viewBinding.inboxIcon.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }

        viewBinding.settingsIcon.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
    }
}