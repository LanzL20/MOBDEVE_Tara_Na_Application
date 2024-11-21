package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileUserBinding
import kotlinx.coroutines.launch

class ProfileUserActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityProfileUserBinding.inflate(layoutInflater)
        val username= intent.getStringExtra(User.USERNAME_KEY)
        lifecycleScope.launch {
            val user= DBHelper.getUser(username!!)
            viewBinding.activityProfileUserName.text= user.name
            viewBinding.activityProfileUserUsername.text= "@"+user.username
        }
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
            val intent = Intent(this, ScheduleMainActivity::class.java)
            startActivity(intent)
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