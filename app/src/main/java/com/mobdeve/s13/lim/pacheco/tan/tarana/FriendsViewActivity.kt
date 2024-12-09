package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityFriendsViewBinding
import kotlinx.coroutines.launch

class FriendsViewActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityFriendsViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFriendsViewBinding.inflate(layoutInflater)
        Log.d("FriendsViewActivity", "onCreate")
        setContentView(viewBinding.root)

        // User profile button
        var user = UserSession.getUser()
        viewBinding.profileUser1.setImageResource(user.getDrawableProfilePicture())

        viewBinding.activityFriendsViewAddBtn.setOnClickListener {
            val intent = Intent(this, FriendsAddActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val friendList = UserSession.getUser().friendsList
            val friends = DBHelper.getUsers(friendList)
            val adapter = AdapterFriendsAdd(friends)
            viewBinding.activityFriendsViewRv.adapter = adapter
            viewBinding.activityFriendsViewRv.layoutManager = LinearLayoutManager(this@FriendsViewActivity, LinearLayoutManager.VERTICAL, false)
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

    override fun onResume() {
        super.onResume()
        if(UserSession.hasUnreadNotifications()){
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox_unread)
        }
        else{
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox)
        }
    }
}