package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalDeleteItemBinding

class LakwatsaListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaListBinding
    private lateinit var deleteItemModalBinding: ModalDeleteItemBinding
    private lateinit var deleteItemModal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLakwatsaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deleteItemModal = Dialog(this)
        deleteItemModalBinding = ModalDeleteItemBinding.inflate(layoutInflater)
        deleteItemModal.setContentView(deleteItemModalBinding.root)
        deleteItemModal.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        deleteItemModal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.trashIcon.setOnClickListener {
            deleteItemModal.show()
        }

        // TODO: Click listener for the delete button

        deleteItemModalBinding.modalDeleteItemBtnClose.setOnClickListener {
            deleteItemModal.dismiss()
        }

        // TODO: Convert hardcoded data to actual data and pass it to the adapter
        // TODO: I have a feeling that we can create an abstract lakwatsa class/layout file thats able to become either upcoming, ongoing, or completed
        //  hard coded data for now
        binding.upcomingItem1.setOnClickListener {
            val intent = Intent(this, LakwatsaUpcomingActivity::class.java)
            startActivity(intent)
        }
        binding.ongoingItem1.setOnClickListener {
            val intent = Intent(this, LakwatsaOngoingActivity::class.java)
            startActivity(intent)
        }
        binding.completedItem1.setOnClickListener {
            val intent = Intent(this, LakwatsaCompletedActivity::class.java)
            startActivity(intent)
        }
        binding.completedItem2.setOnClickListener {
            val intent = Intent(this, LakwatsaCompletedActivity::class.java)
            startActivity(intent)
        }
        binding.addLakwatsaIcon.setOnClickListener {
            val intent = Intent(this, LakwatsaCreateActivity::class.java)
            startActivity(intent)
        }

        // NAVIGATION BUTTONS

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
    }
}