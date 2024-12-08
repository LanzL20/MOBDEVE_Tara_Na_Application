package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileUserBinding
import kotlinx.coroutines.launch

class ProfileUserActivity: AppCompatActivity() {

    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    private val FINE_PERMISSION_CODE=1

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
        viewBinding.profileUser1.setImageResource(user.getDrawableProfilePicture())

        // add a permission check for location
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)


        if (!areLocationPermissionsGranted()) {
            requestLocationPermissions()
        } else {
            startLocationService()
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

    private fun areLocationPermissionsGranted(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, permissions, FINE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.d("MainActivity", "Location permissions granted")
                startLocationService()
            } else {
                Log.d("MainActivity", "Location permissions denied")

            }
        }
    }

    private fun startLocationService() {
        val locationIntent = Intent(this, LocationService::class.java)
        if (!LocationService.isRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("MainActivity", "Foregrounding Starting Service")
                startForegroundService(locationIntent)
            } else {
                Log.d("MainActivity", "Starting Service")
                startService(locationIntent)
            }
        }
    }
}