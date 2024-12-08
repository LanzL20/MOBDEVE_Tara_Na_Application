package com.mobdeve.s13.lim.pacheco.tan.tarana

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

class LakwatsaCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLakwatsaCreateBinding
    private lateinit var inviteFriendsModalBinding: ModalInviteFriendsBinding
    private lateinit var inviteFriendsModal: Dialog

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
        binding.activityLakwatsaCreateBtn.setOnClickListener {
            // TODO: Very temp code for testing
            val users = ArrayList<User>()
            val usernames = ArrayList<String>()
            users.add(UserSession.getUser())
            usernames.add(UserSession.getUser().username)
            // TODO: ADD ALL ADDED FRIENDS TO THE LIST
            // TODO: Date be a proper date modal
            val lakwatsa = Lakwatsa(
                usernames,
                binding.activityLakwatsaCreateEtTitle.text.toString(),
                binding.activityLakwatsaCreateEtDatetime.text.toString(),
                UserSession.getUser().username
            )
            lifecycleScope.launch {
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
        val friendsList = arrayListOf(
            InviteFriendItem(R.drawable.asset_profile2, "Ashley Tsang", "@ashley_yvonne2003", false),
            InviteFriendItem(R.drawable.asset_profile3, "Cedric Alejo", "@alejo_ced", false),
            InviteFriendItem(R.drawable.asset_profile1, "Tyler Tan", "@TyTan88", false)
        )

        Log.d("RecyclerView Setup", "Found ${friendsList.size} items")
        inviteFriendsModalBinding.inviteFriendsRv.layoutManager = LinearLayoutManager(this)

        val adapter = AdapterInviteFriends(friendsList, this)
        inviteFriendsModalBinding.inviteFriendsRv.adapter = adapter
    }
}
