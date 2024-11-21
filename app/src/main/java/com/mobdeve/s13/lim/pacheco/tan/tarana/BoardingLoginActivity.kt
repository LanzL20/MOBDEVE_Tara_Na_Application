package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingLoginBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ModalForgotPwBinding

class BoardingLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardingLoginBinding
    private lateinit var forgotPwModalBinding: ModalForgotPwBinding
    private lateinit var forgotPwModal: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forgotPwModal = Dialog(this)
        forgotPwModalBinding = ModalForgotPwBinding.inflate(layoutInflater)
        forgotPwModal.setContentView(forgotPwModalBinding.root)
        forgotPwModal.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        forgotPwModal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding.activityBoardingLoginForgotPwBtn.setOnClickListener {
            forgotPwModal.show()
        }

        forgotPwModalBinding.modalForgotPwBtnSubmit.setOnClickListener {
            val phoneNum = forgotPwModalBinding.modalForgotPwEt.text.toString()
            if (phoneNum.isNotEmpty()) {
                Toast.makeText(this, "Password recovery text sent", Toast.LENGTH_SHORT).show()
                forgotPwModal.dismiss()
            } else {
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
            }
        }

        forgotPwModalBinding.modalForgotPwBtnClose.setOnClickListener {
            forgotPwModal.dismiss()
        }

        binding.activityBoardingLoginBtnLogin.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            // TODO: Temp for testing
            intent.putExtra(User.USERNAME_KEY, "Tytan6249")
            startActivity(intent)
        }

        binding.activityBoardingLoginTvSignUp.setOnClickListener {
            val intent = Intent(this, BoardingSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
