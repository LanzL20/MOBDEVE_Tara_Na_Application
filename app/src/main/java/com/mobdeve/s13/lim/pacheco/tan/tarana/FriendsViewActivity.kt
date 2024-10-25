package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityFriendsViewBinding

class FriendsViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityFriendsViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityFriendsViewAddBtn.setOnClickListener {

            val intent = Intent(this, FriendsAddActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityFriendsViewFriend1Ll.setOnClickListener {
            Log.d("FriendsViewActivity", "Details clicked")
            val intent = Intent(this, ProfileFriendActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityFriendsViewFriend2Ll.setOnClickListener {
            Log.d("FriendsViewActivity", "Details clicked")
            val intent = Intent(this, ProfileFriendActivity::class.java)
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
        viewBinding.profileUser1.setOnClickListener{
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }
    }
}