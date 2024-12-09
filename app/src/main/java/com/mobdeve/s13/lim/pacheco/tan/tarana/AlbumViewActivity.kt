package com.mobdeve.s13.lim.pacheco.tan.tarana

import AdapterAlbumView
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityAlbumViewBinding
import kotlinx.coroutines.launch


class AlbumViewActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityAlbumViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("AlbumViewActivity", "oncreate")
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAlbumViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // User profile button
        var user = UserSession.getUser()
        viewBinding.profileUser1.setImageResource(user.getDrawableProfilePicture())

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

        Log.e("AlbumViewActivity", "onResume")
        lifecycleScope.launch {
            Log.e("AlbumViewActivity", "onResume")
            val data = UserSession.getUser().lakwatsaList
            if(viewBinding.albumRv.adapter == null) {
                val lakwatsaList = DBHelper.getAllLakwatsaFromList(data)
                val lakwatsaListFiltered = ArrayList<Lakwatsa>()
                Log.e("AlbumViewActivity", "lakwatsaList: $lakwatsaList")
                // remove all upcoming lakwatsas
                for (lakwatsa in lakwatsaList){
                    if(lakwatsa.status != Lakwatsa.LAKWATSA_UPCOMING){
                        lakwatsaListFiltered.add(lakwatsa)
                    }
                }
                val adapter = AdapterAlbumView(lakwatsaListFiltered)
                viewBinding.albumRv.adapter = adapter
                val layoutManager = GridLayoutManager(viewBinding.albumRv.context, 2)
                viewBinding.albumRv.layoutManager = layoutManager
            }else{
                val lakwatsaList = DBHelper.getAllLakwatsaFromList(data)
                val lakwatsaListFiltered = ArrayList<Lakwatsa>()
                // remove all upcoming lakwatsas
                for (lakwatsa in lakwatsaList){
                    if(lakwatsa.status != Lakwatsa.LAKWATSA_UPCOMING){
                        lakwatsaListFiltered.add(lakwatsa)
                    }
                }
                (viewBinding.albumRv.adapter as AdapterAlbumView).setData(lakwatsaListFiltered)
            }
        }
        if(UserSession.hasUnreadNotifications()){
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox_unread)
        }
        else{
            viewBinding.inboxIcon.setImageResource(R.drawable.ic_inbox)
        }
    }
}