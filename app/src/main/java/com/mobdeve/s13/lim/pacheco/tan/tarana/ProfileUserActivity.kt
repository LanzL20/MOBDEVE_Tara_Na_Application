package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileUserBinding
import kotlinx.coroutines.launch

class ProfileUserActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivityProfileUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileUserBinding.inflate(layoutInflater)
        Log.d("MainActivity", "Welcome Activity Created")
        var user = UserSession.getUser()
        DBHelper.activateUserListener()
        Log.d("MainActivity", "Welcome Activity Created123")
        viewBinding.activityProfileUserName.text= user.name
        viewBinding.activityProfileUserUsername.text= "@"+user.username
        viewBinding.activityProfileUserNumFriends.text= user.friendsList.size.toString()

        val LocationIntent= Intent(this, LocationService::class.java)
        if(!LocationService.isRunning){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(LocationIntent)
            }
            else{
                startService(LocationIntent)
            }
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

    override fun onResume(){
        super.onResume()
        viewBinding.activityProfileUserName.text= UserSession.getUser().name
        viewBinding.activityProfileUserUsername.text= "@"+UserSession.getUser().username
        viewBinding.activityProfileUserNumFriends.text= UserSession.getUser().friendsList.size.toString()
    }
}