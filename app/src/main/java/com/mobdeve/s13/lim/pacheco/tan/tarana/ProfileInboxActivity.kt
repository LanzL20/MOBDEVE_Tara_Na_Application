package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileInboxBinding
import java.util.Date

class ProfileInboxActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityProfileInboxBinding
    private lateinit var adapterInbox: AdapterInbox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileInboxBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sampleData = fetchNotifications()

        adapterInbox = AdapterInbox(sampleData)
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

    private fun fetchNotifications(): ArrayList<Notification> {
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
    }
}