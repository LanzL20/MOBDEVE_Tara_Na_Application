package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileInboxBinding
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.reflect.typeOf

class ProfileInboxActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityProfileInboxBinding
    private lateinit var adapterInbox: AdapterInbox
    var user=UserSession.getUser()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileInboxBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        UserSession.setUnreadNotifications(false)
        lifecycleScope.launch {
            DBHelper.setAllNotificationsRead(user)
        }


        //val sampleData = fetchNotifications()
        Log.d("ProfileInboxActivity", user.notificationList::class.simpleName.toString())
        Log.d("ProfileInboxActivity", user.notificationList[0]::class.java.toString())
        adapterInbox = AdapterInbox(user.notificationList)
        viewBinding.rvNotifications.layoutManager = LinearLayoutManager(this)
        viewBinding.rvNotifications.adapter = adapterInbox

        // var data = ArrayList<Notification>()
        // viewBinding.rvNotifications.adapter = AdapterInbox(data)

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

        // TODO: Maybe do finish() after returning to the user profile and clear screen stack
        /** viewBinding.profileUser1.setOnClickListener{
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        } **/

        Log.d("AdapterInbox", "Item count: ${adapterInbox.itemCount}")
    }

    override fun onResume() {
        super.onResume()
        if(user.notificationList.size!=adapterInbox.itemCount){
            adapterInbox.notifyDataSetChanged()
        }
        if(UserSession.hasUnreadNotifications()){
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox_unread)
        }
    }
    /*private fun fetchNotifications(): ArrayList<Notification> {
        val notifications = ArrayList<Notification>()

        notifications.add(
            object : Notification(
                "Marissa Villaceran sent you a friend request!",
                Date(),
                isRead = false,
                isFriendRequest = true
            ) {}
        )

        notifications.add(
            object : Notification(
                "Tyler Tan accepted your friend request!",
                Date(),
                isRead = true,
                isFriendRequest = false
            ) {}
        )

        return notifications
    }*/
}