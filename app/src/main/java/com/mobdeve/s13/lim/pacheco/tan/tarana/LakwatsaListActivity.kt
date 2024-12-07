package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.Distribution.BucketOptions.Linear
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaListBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalDeleteItemBinding
import kotlinx.coroutines.launch

class LakwatsaListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaListBinding
    private lateinit var deleteItemModalBinding: ModalDeleteItemBinding
    private lateinit var deleteItemModal: Dialog

    suspend fun refreshData(){
        binding.upcomingLl.visibility = View.GONE
        binding.ongoingLl.visibility = View.GONE
        binding.completedLl.visibility = View.GONE

        val lakwatsaList = DBHelper.getAllLakwatsaFromList(UserSession.getUser().lakwatsaList)
        var lakwatsaListUpcoming = ArrayList<Lakwatsa>()
        var lakwatsaListOngoing = ArrayList<Lakwatsa>()
        var lakwatsaListCompleted = ArrayList<Lakwatsa>()

        for(lakwatsa in lakwatsaList){
            if(lakwatsa.status == Lakwatsa.LAKWATSA_UPCOMING){
                lakwatsaListUpcoming.add(lakwatsa)
            } else if (lakwatsa.status == Lakwatsa.LAKWATSA_ONGOING){
                lakwatsaListOngoing.add(lakwatsa)
            } else if (lakwatsa.status == Lakwatsa.LAKWATSA_COMPLETED){
                lakwatsaListCompleted.add(lakwatsa)
            }
        }

        if(lakwatsaListUpcoming.size > 0){
            binding.upcomingLl.visibility = View.VISIBLE
        }
        if(lakwatsaListOngoing.size > 0){
            binding.ongoingLl.visibility = View.VISIBLE
        }
        if(lakwatsaListCompleted.size > 0){
            binding.completedLl.visibility = View.VISIBLE
        }

        binding.activityLakwatsaListRv1.adapter = AdapterLakwatsaList(lakwatsaListUpcoming)
        binding.activityLakwatsaListRv1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.activityLakwatsaListRv2.adapter = AdapterLakwatsaList(lakwatsaListOngoing)
        binding.activityLakwatsaListRv2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.activityLakwatsaListRv3.adapter = AdapterLakwatsaList(lakwatsaListCompleted)
        binding.activityLakwatsaListRv3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLakwatsaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.upcomingLl.visibility = View.GONE
        binding.ongoingLl.visibility = View.GONE
        binding.completedLl.visibility = View.GONE

        lifecycleScope.launch {
            refreshData()
        }

        deleteItemModal = Dialog(this)
        deleteItemModalBinding = ModalDeleteItemBinding.inflate(layoutInflater)
        deleteItemModal.setContentView(deleteItemModalBinding.root)
        deleteItemModal.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        deleteItemModal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        // TODO: Click listener for the delete button
        deleteItemModalBinding.modalDeleteItemBtnClose.setOnClickListener {
            deleteItemModal.dismiss()
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            refreshData()
        }
    }
}