package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
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
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaOngoingBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalEndLakwatsaBinding
import kotlinx.coroutines.launch

class LakwatsaOngoingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaOngoingBinding
    private lateinit var endLakwatsaModalBinding: ModalEndLakwatsaBinding
    private lateinit var endLakwatsaModal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLakwatsaOngoingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.optionsIcon.visibility = View.GONE
        lifecycleScope.launch {
            val lakwatsa = DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)
            if(lakwatsa.lakwatsaAdmin == UserSession.getUser().username){
                binding.optionsIcon.visibility = View.VISIBLE
            }
            binding.activityTitleTv.text = lakwatsa.lakwatsaTitle
            binding.activityLakwtsaUpcomingDate.text = lakwatsa.date
            if(lakwatsa.time.isNullOrBlank()){
                binding.activityLakwtsaUpcomingTime.text = ""
            } else{
                binding.activityLakwtsaUpcomingTime.text = lakwatsa.time
            }

            binding.activityLakwatsaOngoingFriend1Iv.visibility = View.GONE
            binding.activityLakwatsaOngoingFriend2Iv.visibility = View.GONE
            binding.moreFriends.visibility = View.GONE
            if(lakwatsa.lakwatsaUsers.size == 1){
                binding.activityLakwatsaOngoingFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                binding.activityLakwatsaOngoingFriend1Iv.visibility = View.VISIBLE
            }
            else if (lakwatsa.lakwatsaUsers.size == 2){
                binding.activityLakwatsaOngoingFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                binding.activityLakwatsaOngoingFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                binding.activityLakwatsaOngoingFriend1Iv.visibility = View.VISIBLE
                binding.activityLakwatsaOngoingFriend2Iv.visibility = View.VISIBLE
            } else {
                binding.activityLakwatsaOngoingFriend1Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[0]).profilePicture, "drawable", packageName))
                binding.activityLakwatsaOngoingFriend2Iv.setImageResource(resources.getIdentifier("asset_profile" + DBHelper.getUser(lakwatsa.lakwatsaUsers[1]).profilePicture, "drawable", packageName))
                binding.activityLakwatsaOngoingFriend1Iv.visibility = View.VISIBLE
                binding.activityLakwatsaOngoingFriend2Iv.visibility = View.VISIBLE
                binding.moreFriendsTv.text = "+${lakwatsa.lakwatsaUsers.size - 2}"
                binding.moreFriends.visibility = View.VISIBLE

            }
            binding.endBtn.setOnClickListener{
                val newLakwatsa = Lakwatsa(
                    lakwatsa.lakwatsaId,
                    lakwatsa.lakwatsaUsers,
                    lakwatsa.locationLatitude,
                    lakwatsa.locationLongitude,
                    binding.activityTitleTv.text.toString(),
                    lakwatsa.date,
                    lakwatsa.time,
                    lakwatsa.pollingList,
                    lakwatsa.album,
                    Lakwatsa.LAKWATSA_COMPLETED,
                    lakwatsa.lakwatsaAdmin
                )
                DBHelper.updateLakwatsa(newLakwatsa)
                val intent2 = Intent(this@LakwatsaOngoingActivity, LakwatsaCompletedActivity::class.java)
                intent2.putExtra(Lakwatsa.ID_KEY, intent.getStringExtra(Lakwatsa.ID_KEY))
                startActivity(intent2)
                finish()
            }
        }

        endLakwatsaModal = Dialog(this)
        endLakwatsaModalBinding = ModalEndLakwatsaBinding.inflate(layoutInflater)
        endLakwatsaModal.setContentView(endLakwatsaModalBinding.root)
        endLakwatsaModal.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        endLakwatsaModal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.endBtn.setOnClickListener {
            endLakwatsaModal.show()
        }

        endLakwatsaModalBinding.modalEndLakwatsaBtnClose.setOnClickListener {
            endLakwatsaModal.dismiss()
        }

        // TODO: END LAKWATSA

        // NAVIGATION BUTTONS

        binding.albumBtn.setOnClickListener{
            val intent2 = Intent(this, AlbumAlbumActivity::class.java)
            intent2.putExtra(Lakwatsa.ID_KEY, intent.getStringExtra(Lakwatsa.ID_KEY))
            startActivity(intent2)
        }

        binding.inboxIcon.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }
        binding.settingsIcon.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        binding.profileUser1.setOnClickListener{
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }
        binding.activityLakwatsaOngoingMapIv.setOnClickListener{
            val intent = Intent(this, LakwatsaLocationActivity::class.java)
            startActivity(intent)
        }

        binding.optionsIcon.setOnClickListener { view ->
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
        editName.setText(binding.activityTitleTv.text)

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
            binding.activityTitleTv.text = editName.text.toString()
        }

        dialog.show()
    }
}