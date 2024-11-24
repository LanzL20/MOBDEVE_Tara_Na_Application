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
    private lateinit var phoneNumber: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forgotPwModal = Dialog(this)
        forgotPwModalBinding = ModalForgotPwBinding.inflate(layoutInflater)
        forgotPwModal.setContentView(forgotPwModalBinding.root)

        forgotPwModal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        forgotPwModal.setOnShowListener {
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val margin = (30 * displayMetrics.density).toInt()

            forgotPwModal.window?.setLayout(
                screenWidth - 2 * margin,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

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
            val intent = Intent(this, BoardingPhoneAuthActivity::class.java)
            phoneNumber = binding.activityBoardingLoginEtPhone.text.toString()
            phoneNumber= "+63${phoneNumber.substring(1)}"
            // TODO: ADD ENCRYPTION FOR PASSWORD
            password = binding.activityBoardingLoginEtPassword.text.toString()
            // TODO: IMPLEMENT PASSWORD CHECKING
            intent.putExtra(User.PHONE_NUMBER_KEY, phoneNumber)
            intent.putExtra("from", "login")
            startActivity(intent)
        }

        binding.activityBoardingLoginTvSignUp.setOnClickListener {
            val intent = Intent(this, BoardingSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
