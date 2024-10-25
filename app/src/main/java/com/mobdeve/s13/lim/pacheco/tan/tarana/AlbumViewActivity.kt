package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityAlbumViewBinding

class AlbumViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityAlbumViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // TODO: Add data passing from recycler view to album activity
        viewBinding.albumItem1.setOnClickListener{
            val intent = Intent(this, AlbumAlbumActivity::class.java)
            startActivity(intent)
        }
        viewBinding.albumItem2.setOnClickListener{
            val intent = Intent(this, AlbumAlbumActivity::class.java)
            startActivity(intent)
        }
        viewBinding.albumItem3.setOnClickListener{
            val intent = Intent(this, AlbumAlbumActivity::class.java)
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