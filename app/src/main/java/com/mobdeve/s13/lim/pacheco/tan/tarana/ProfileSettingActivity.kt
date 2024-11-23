package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileSettingsBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalDeleteAccBinding

class ProfileSettingActivity:AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingsBinding
    private lateinit var deleteAccModalBinding: ModalDeleteAccBinding
    private lateinit var deleteAccModal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deleteAccModal = Dialog(this)
        deleteAccModalBinding = ModalDeleteAccBinding.inflate(layoutInflater)
        deleteAccModal.setContentView(deleteAccModalBinding.root)
        deleteAccModal.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        deleteAccModal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.deleteAccBtn.setOnClickListener {
            deleteAccModal.show()
        }

        // TODO: DELETE ACCOUNT

        deleteAccModalBinding.modalDeleteItemBtnClose.setOnClickListener {
            deleteAccModal.dismiss()
        }

        binding.inboxIcon.setOnClickListener{
            val intent = Intent(this, ProfileInboxActivity::class.java)
            startActivity(intent)
        }

        binding.settingsIcon.setOnClickListener{
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

        binding.logOutBtn.setOnClickListener{
            val intent = Intent(this, BoardingLoginActivity::class.java)
            Firebase.auth.signOut()
            UserSession.clearUser()
            startActivity(intent)
        }
    }
}