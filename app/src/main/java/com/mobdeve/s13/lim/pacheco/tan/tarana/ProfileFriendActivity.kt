package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileFriendBinding
import kotlinx.coroutines.launch

class ProfileFriendActivity:AppCompatActivity() {

    companion object{
        const val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityProfileFriendBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // User profile button
        var user = UserSession.getUser()
        viewBinding.profileUser1.setImageResource(user.getDrawableProfilePicture())

        Log.e("ProfileFriendActivity", UserSession.getUser().friendRequestsSent.toString())
        Log.e("ProfileFriendActivity", UserSession.getUser().friendRequestsReceived.toString())
        Log.e("ProfileFriendActivity", intent.getStringExtra(USER_KEY).toString())

        if(UserSession.getUser().friendsList.contains(intent.getStringExtra(USER_KEY).toString())){
            viewBinding.followingText.text = "Following"
            viewBinding.friendScheduleBtn.visibility = View.VISIBLE
        } else if (UserSession.getUser().friendRequestsSent.contains(intent.getStringExtra(USER_KEY).toString())){
            viewBinding.followingText.text = "Request Sent"
            viewBinding.friendScheduleBtn.visibility = View.GONE
        } else if (UserSession.getUser().friendRequestsReceived.contains(intent.getStringExtra(USER_KEY).toString())){
            viewBinding.followingText.text = "Accept Request"
            viewBinding.friendScheduleBtn.visibility = View.GONE
        } else {
            viewBinding.followingText.text = "Follow"
            viewBinding.friendScheduleBtn.visibility = View.GONE
        }


        lifecycleScope.launch {
            val user = DBHelper.getUser(intent.getStringExtra(USER_KEY).toString())
            viewBinding.activityProfileUserName.text = user.name
            viewBinding.activityProfileUserUsername.text = user.username
            viewBinding.activityProfileUserNumFriends.text = user.friendsList.size.toString()
            val resourceId = viewBinding.root.context.getResources().getIdentifier("asset_profile" + user.profilePicture, "drawable", viewBinding.root.context.getPackageName());
            viewBinding.profileUser2.setImageResource(resourceId)

            viewBinding.friendScheduleBtn.setOnClickListener {
                val intent = Intent(this@ProfileFriendActivity, ScheduleMainActivity::class.java)
                intent.putExtra(USER_KEY, user.username)
                startActivity(intent)
            }
            viewBinding.followingBtn.setOnClickListener {

                if(viewBinding.followingText.text == "Follow"){
                    UserSession.getUser().friendRequestsSent.add(intent.getStringExtra(USER_KEY).toString())
                    user.friendRequestsReceived.add(UserSession.getUser().username)
                    DBHelper.updateUser(UserSession.getUser())
                    DBHelper.updateUser(user)
                    runOnUiThread{
                        viewBinding.followingText.text = "Request Sent"
                    }
                }
                else if (viewBinding.followingText.text == "Request Sent"){
                    UserSession.getUser().friendRequestsSent.remove(intent.getStringExtra(USER_KEY).toString())
                    user.friendRequestsReceived.remove(UserSession.getUser().username)
                    DBHelper.updateUser(UserSession.getUser())
                    DBHelper.updateUser(user)
                    runOnUiThread{
                        viewBinding.followingText.text = "Follow"
                    }
                } else if (viewBinding.followingText.text == "Accept Request"){
                    UserSession.getUser().friendRequestsReceived.remove(intent.getStringExtra(USER_KEY).toString())
                    UserSession.getUser().friendsList.add(intent.getStringExtra(USER_KEY).toString())
                    user.friendRequestsSent.remove(UserSession.getUser().username)
                    user.friendsList.add(UserSession.getUser().username)
                    DBHelper.updateUser(UserSession.getUser())
                    DBHelper.updateUser(user)
                    runOnUiThread{
                        viewBinding.followingText.text = "Following"
                    }
                } else if (viewBinding.followingText.text == "Following"){
                    UserSession.getUser().friendsList.remove(intent.getStringExtra(USER_KEY).toString())
                    user.friendsList.remove(UserSession.getUser().username)
                    DBHelper.updateUser(UserSession.getUser())
                    DBHelper.updateUser(user)
                    runOnUiThread{
                        viewBinding.followingText.text = "Follow"
                    }
                }
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