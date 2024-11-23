package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaOngoingBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalEndLakwatsaBinding

class LakwatsaOngoingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaOngoingBinding
    private lateinit var endLakwatsaModalBinding: ModalEndLakwatsaBinding
    private lateinit var endLakwatsaModal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLakwatsaOngoingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val intent = Intent(this, AlbumViewActivity::class.java)
            startActivity(intent)
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
        }
    }
}