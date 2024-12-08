package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityFriendsAddBinding
import kotlinx.coroutines.launch

class FriendsAddActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityFriendsAddBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // User profile button
        var user = UserSession.getUser()
        viewBinding.profileUser1.setImageResource(user.getDrawableProfilePicture())

        val friendList = ArrayList<User>()
        viewBinding.activityFriendsAddRv.adapter = AdapterFriendsAdd(friendList)
        viewBinding.activityFriendsAddRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        viewBinding.activityFriendsAddBtn.setOnClickListener(){
            lifecycleScope.launch {
                val friendsList = DBHelper.getUsersByUsernameOrPhoneNumber(viewBinding.activityFriendsAddEtUsername.text.toString())
                friendsList.removeIf {
                    it.username == UserSession.getUser().username || UserSession.getUser().friendsList.contains(it.username)
                }
                (viewBinding.activityFriendsAddRv.adapter as AdapterFriendsAdd).setData(friendsList)
            }
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