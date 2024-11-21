package com.mobdeve.s13.lim.pacheco.tan.tarana

import AdapterAlbumView
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityAlbumViewBinding
import kotlinx.coroutines.launch


class AlbumViewActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityAlbumViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAlbumViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // ALBUM RECYCLER VIEW TODO: CHANGE TO ONLY THE USERS LAKWATSAS
        lifecycleScope.launch {
            val data = DBHelper.getAllLakwatsa()
            val adapter = AdapterAlbumView(data)
            viewBinding.albumRv.adapter = adapter
            val layoutManager = GridLayoutManager(viewBinding.albumRv.context, 2)
            viewBinding.albumRv.layoutManager = layoutManager
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

    override fun onResume(){
        super.onResume()
        lifecycleScope.launch {
            val data = DBHelper.getAllLakwatsa()
            (viewBinding.albumRv.adapter as AdapterAlbumView).setData(data)
        }
    }
}