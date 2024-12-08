package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaCreateBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalInviteFriendsBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class LakwatsaCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaCreateBinding
    private lateinit var inviteFriendsModalBinding: ModalInviteFriendsBinding
    private lateinit var inviteFriendsModal: Dialog
    private var friendsToInviteList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLakwatsaCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup invite friends modal
        inviteFriendsModalBinding = ModalInviteFriendsBinding.inflate(layoutInflater)
        inviteFriendsModal = Dialog(this).apply {
            setContentView(inviteFriendsModalBinding.root)
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.90).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        binding.activityLakwatsaCreateEtFriends.setOnClickListener {
            inviteFriendsModal.show()
        }

        inviteFriendsModalBinding.modalInviteFriendsBtnClose.setOnClickListener {
            inviteFriendsModal.dismiss()
        }

        setupRecyclerView()

        // NAVIGATION BUTTONS
        binding.inboxIcon.setOnClickListener {
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }
        binding.settingsIcon.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        binding.profileUser1.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }
        binding.activityLakwatsaCreateEtDatetime.setOnClickListener{
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                var month = month + 1
                var date = "$year/$month/$dayOfMonth"
                binding.activityLakwatsaCreateEtDatetime.setText(date)
            }, year, month, day)
            datePickerDialog.show()
        }
        binding.activityLakwatsaCreateBtn.setOnClickListener {
            lifecycleScope.launch {
                val users = ArrayList<User>()
                val usernames = ArrayList<String>()
                users.add(UserSession.getUser())
                usernames.add(UserSession.getUser().username)
                for(friends in friendsToInviteList){
                    val user = DBHelper.getUser(friends)
                    users.add(user)
                    usernames.add(user.username)
                }
                val lakwatsa = Lakwatsa(
                    usernames,
                    binding.activityLakwatsaCreateEtTitle.text.toString(),
                    binding.activityLakwatsaCreateEtDatetime.text.toString(),
                    UserSession.getUser().username
                )
                val lakwatsaId = DBHelper.addLakwatsa(lakwatsa)
                for (user in users) {
                    user.addLakwatsa(lakwatsaId)
                    DBHelper.updateUser(user)
                }
                finish()
            }
        }
    }

    // HARDCODED CONTENT
    private fun setupRecyclerView() {
        lifecycleScope.launch {
            val friendsList = DBHelper.getUsers(UserSession.getUser().friendsList)
            val items = ArrayList<InviteFriendItem>()
            for(friend in friendsList){
                if (friendsToInviteList.contains(friend.username)) {
                    items.add(InviteFriendItem(friend.getDrawableProfilePicture(), friend.name, "@" + friend.username, true))
                } else {
                    items.add(InviteFriendItem(friend.getDrawableProfilePicture(), friend.name, "@" + friend.username, false))
                }
            }
            inviteFriendsModalBinding.inviteFriendsRv.layoutManager = LinearLayoutManager(binding.root.context)
            val adapter = AdapterInviteFriends(items, binding.root.context)
            inviteFriendsModalBinding.inviteFriendsRv.adapter = adapter

            inviteFriendsModalBinding.activityFriendsAddBtn.setOnClickListener {
                adapter.filterItems(inviteFriendsModalBinding.modalInviteFriendsEt.text.toString())
            }

            inviteFriendsModalBinding.modalInviteFriendsBtnInvite.setOnClickListener{
                friendsToInviteList.clear()
                for (item in items) {
                    if (item.isChecked) {
                        friendsToInviteList.add(item.username.substring(1))
                    }
                }
                inviteFriendsModal.dismiss()
                binding.activityLakwatsaCreateEtFriends.setText(friendsToInviteList.joinToString(", "))
            }
        }
    }
}
