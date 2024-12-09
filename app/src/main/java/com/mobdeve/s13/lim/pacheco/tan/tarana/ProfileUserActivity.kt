package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileUserBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalChangeProfileBinding
import kotlinx.coroutines.launch

class ProfileUserActivity: AppCompatActivity() {

    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    private val FINE_PERMISSION_CODE=1

    private lateinit var viewBinding: ActivityProfileUserBinding
    private lateinit var changeProfileModalBinding: ModalChangeProfileBinding
    private lateinit var changeProfileModal: Dialog

    private var selectedProfilePictureRes: Int = 1

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

        // Change profile picture modal
        changeProfileModalBinding = ModalChangeProfileBinding.inflate(layoutInflater)
        changeProfileModal = Dialog(this).apply {
            setContentView(changeProfileModalBinding.root)
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.90).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        setSelectedBorder(changeProfileModalBinding.male1)

        viewBinding.cameraIcon.setOnClickListener {
            changeProfileModal.show()
        }

        changeProfileModalBinding.modalChangeProfileBtnClose.setOnClickListener {
            changeProfileModal.dismiss()
        }

        // Updating profile picture

        changeProfileModalBinding.male1.setOnClickListener {
            changeProfileModalBinding.profileUser.setImageResource(R.drawable.asset_profile1)
            resetBorders()
            setSelectedBorder(changeProfileModalBinding.male1)
            selectedProfilePictureRes = 1
        }

        changeProfileModalBinding.female1.setOnClickListener {
            changeProfileModalBinding.profileUser.setImageResource(R.drawable.asset_profile2)
            resetBorders()
            setSelectedBorder(changeProfileModalBinding.female1)
            selectedProfilePictureRes = 2
        }

        changeProfileModalBinding.male2.setOnClickListener {
            changeProfileModalBinding.profileUser.setImageResource(R.drawable.asset_profile3)
            resetBorders()
            setSelectedBorder(changeProfileModalBinding.male2)
            selectedProfilePictureRes = 3
        }

        changeProfileModalBinding.female2.setOnClickListener {
            changeProfileModalBinding.profileUser.setImageResource(R.drawable.asset_profile4)
            resetBorders()
            setSelectedBorder(changeProfileModalBinding.female2)
            selectedProfilePictureRes = 4
        }

        // Save new profile picture
        changeProfileModalBinding.modalChangeProfileBtnSave.setOnClickListener {
            val user = UserSession.getUser()
            user.updateProfilePicture(selectedProfilePictureRes)
            viewBinding.profileUser1.setImageResource(user.getDrawableProfilePicture())
            changeProfileModal.dismiss()
            DBHelper.updateUser(user)
        }

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

        if(UserSession.hasUnreadNotifications()){
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox_unread)
        }
        else{
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox)
        }
        viewBinding.profileUser1.setImageResource(UserSession.getUser().getDrawableProfilePicture())
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

    private fun resetBorders() {
        changeProfileModalBinding.male1.background = null
        changeProfileModalBinding.female1.background = null
        changeProfileModalBinding.male2.background = null
        changeProfileModalBinding.female2.background = null
    }

    private fun setSelectedBorder(view: View) {
        val borderDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.TRANSPARENT)
            setStroke(5, Color.BLACK)
        }
        view.background = borderDrawable
    }

}