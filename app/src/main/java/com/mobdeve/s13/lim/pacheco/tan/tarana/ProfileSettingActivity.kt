package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileSettingsBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalDeleteAccBinding
import kotlinx.coroutines.launch

class ProfileSettingActivity:AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingsBinding
    private lateinit var deleteAccModalBinding: ModalDeleteAccBinding
    private lateinit var deleteAccModal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityProfileSettingsEtName.setText(UserSession.getUser().name)
        binding.activityProfileSettingsEtUsername.setText(UserSession.getUser().username)
        binding.activityProfileSettingsEtPhone.setText(UserSession.getUser().phoneNumber)

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

        binding.saveBtn.setOnClickListener{
            lifecycleScope.launch {
                if(DBHelper.checkIfUserExists(binding.activityProfileSettingsEtUsername.text.toString(), UserSession.getUser().phoneNumber) != 2){
                    Toast.makeText(binding.root.context, "Username already exists.", Toast.LENGTH_SHORT).show()
                } else if(binding.activityProfileSettingsEtName.text.toString().isNotEmpty() && binding.activityProfileSettingsEtUsername.text.toString().isNotEmpty()){
                    var newPassword = UserSession.getUser().password
                    var newSalt = UserSession.getUser().salt
                    if(binding.activityProfileSettingsEtPassword.text.toString().isNotEmpty()){
                        newSalt = PasswordHashing.byteArrayToHexString(PasswordHashing.generateSalt())
                        newPassword = PasswordHashing.byteArrayToHexString(PasswordHashing.hashPassword(binding.activityProfileSettingsEtPassword.text.toString(), PasswordHashing.hexStringToByteArray(newSalt)))}

                    var newUser = User(
                        binding.activityProfileSettingsEtName.text.toString(),
                        binding.activityProfileSettingsEtUsername.text.toString(),
                        newPassword,
                        UserSession.getUser().phoneNumber,
                        UserSession.getUser().profilePicture,
                        UserSession.getUser().friendsList,
                        UserSession.getUser().lakwatsaList,
                        UserSession.getUser().friendRequestsSent,
                        UserSession.getUser().friendRequestsReceived,
                        UserSession.getUser().unavailableList,
                        UserSession.getUser().uid,
                        UserSession.getUser().latitude,
                        UserSession.getUser().longitude,
                        UserSession.getUser().salt,
                        UserSession.getUser().notificationList
                    )
                    DBHelper.updateUser(newUser)
                    Toast.makeText(binding.root.context, "Profile updated.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(binding.root.context, "Name and Username cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }

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
            val serviceIntent= Intent(this, LocationService::class.java)
            stopService(serviceIntent)
            val intent = Intent(this, BoardingWelcomeActivity::class.java)
            Firebase.auth.signOut()
            UserSession.clearUser()
            startActivity(intent)
            finish()
        }
    }
}