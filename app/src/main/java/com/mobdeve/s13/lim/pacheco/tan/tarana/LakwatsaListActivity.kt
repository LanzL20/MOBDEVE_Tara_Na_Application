package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding

class LakwatsaListActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityLakwatsaListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // TODO: Convert hardcoded data to actual data and pass it to the adapter
        // TODO: I have a feeling that we can create an abstract lakwatsa class/layout file thats able to become either upcoming, ongoing, or completed
        //  hard coded data for now
        viewBinding.upcomingItem1.setOnClickListener {
            val intent = Intent(this, LakwatsaUpcomingActivity::class.java)
            startActivity(intent)
        }
        viewBinding.ongoingItem1.setOnClickListener {
            val intent = Intent(this, LakwatsaOngoingActivity::class.java)
            startActivity(intent)
        }
        viewBinding.completedItem1.setOnClickListener {
            val intent = Intent(this, LakwatsaCompletedActivity::class.java)
            startActivity(intent)
        }
        viewBinding.completedItem2.setOnClickListener {
            val intent = Intent(this, LakwatsaCompletedActivity::class.java)
            startActivity(intent)
        }
        viewBinding.addLakwatsaIcon.setOnClickListener {
            val intent = Intent(this, LakwatsaCreateActivity::class.java)
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