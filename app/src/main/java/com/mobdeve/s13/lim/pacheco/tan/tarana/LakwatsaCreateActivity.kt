package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaCreateBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Date

class LakwatsaCreateActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityLakwatsaCreateBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

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
        viewBinding.activityLakwatsaCreateBtn.setOnClickListener{
            val intent = Intent(this, LakwatsaListActivity::class.java)
            // TODO: Very temp code for testing
            val users = ArrayList<User>()
            users.add(UserSession.getUser())
            // TODO: ADD ALL ADDED FRIENDS TO THE LIST
            val lakwatsa = Lakwatsa(users, viewBinding.activityDatetimeEtLocation.text.toString(),
                viewBinding.activityLakwatsaCreateEtTitle.text.toString(),
                LocalDateTime.now(),
                HashMap<LocalDateTime, Int>(), ArrayList<String>())
            lifecycleScope.launch {
                val lakwatsaId = DBHelper.addLakwatsa(lakwatsa)
                for(user in users){
                    user.addLakwatsa(lakwatsaId)
                    DBHelper.updateUser(user)
                }
                startActivity(intent)
            }

        }
    }
}