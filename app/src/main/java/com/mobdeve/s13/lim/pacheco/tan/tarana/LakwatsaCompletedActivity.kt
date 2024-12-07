package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaCompletedBinding
import kotlinx.coroutines.launch

class LakwatsaCompletedActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivityLakwatsaCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLakwatsaCompletedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.optionsIcon.visibility = View.GONE
        lifecycleScope.launch {
            val lakwatsa = DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)
            if(lakwatsa.lakwatsaAdmin == UserSession.getUser().username){
                viewBinding.optionsIcon.visibility = View.VISIBLE
            }
            viewBinding.activityLakwtsaCompletedTitle.text = lakwatsa.lakwatsaTitle
            viewBinding.activityLakwtsaCompletedDate.text = lakwatsa.date
            if(lakwatsa.time.isNullOrBlank()){
                viewBinding.activityLakwtsaCompletedTime.text = ""
            } else{
                viewBinding.activityLakwtsaCompletedTime.text = lakwatsa.time
            }

            viewBinding.activityLakwatsaCompletedFriend1Iv.visibility = View.GONE
            viewBinding.activityLakwatsaCompletedFriend2Iv.visibility = View.GONE
            viewBinding.moreFriends.visibility = View.GONE
            if(lakwatsa.lakwatsaUsers.size == 1){
                viewBinding.activityLakwatsaCompletedFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaCompletedFriend1Iv.visibility = View.VISIBLE
            }
            else if (lakwatsa.lakwatsaUsers.size == 2){
                viewBinding.activityLakwatsaCompletedFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaCompletedFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaCompletedFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityLakwatsaCompletedFriend2Iv.visibility = View.VISIBLE
            }
            else {
                viewBinding.activityLakwatsaCompletedFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaCompletedFriend1Iv.visibility = View.VISIBLE
                viewBinding.activityLakwatsaCompletedFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                viewBinding.activityLakwatsaCompletedFriend2Iv.visibility = View.VISIBLE
                viewBinding.moreFriendsTv.setText("+" + (lakwatsa.lakwatsaUsers.size - 2).toString())
            }
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



        // TODO: Set the image to be the first of the album
        viewBinding.albumImgIv.setOnClickListener{
            val intent2 = Intent(this, AlbumAlbumActivity::class.java)
            intent2.putExtra(Lakwatsa.ID_KEY, intent.getStringExtra(Lakwatsa.ID_KEY))
            startActivity(intent2)
        }

        viewBinding.optionsIcon.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(this, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Edit details")

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    1 -> {
                        showEditDetailsDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showEditDetailsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_details, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.setOnShowListener {
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val margin = (24 * displayMetrics.density).toInt()

            dialog.window?.setLayout(
                screenWidth - 2 * margin,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val btnCancel = dialogView.findViewById<ImageButton>(R.id.modal_edit_details_btn_close)
        val btnSave = dialogView.findViewById<Button>(R.id.modal_edit_details_btn_save)
        val editName = dialogView.findViewById<EditText>(R.id.modal_schedule_edit_name_et)
        editName.setText(viewBinding.activityLakwtsaCompletedTitle.text)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            lifecycleScope.launch {
                val lakwatsa = DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)

                val newLakwatsa = Lakwatsa(lakwatsa.lakwatsaId,
                    lakwatsa.lakwatsaUsers,
                    lakwatsa.locationLatitude,
                    lakwatsa.locationLongitude,
                    editName.text.toString(),
                    lakwatsa.date,
                    lakwatsa.time,
                    lakwatsa.pollingList,
                    lakwatsa.album,
                    lakwatsa.status,
                    lakwatsa.lakwatsaAdmin)
                DBHelper.updateLakwatsa(newLakwatsa)
            }

            dialog.dismiss()
            viewBinding.activityLakwtsaCompletedTitle.setText(editName.text.toString())
        }

        dialog.show()
    }
}