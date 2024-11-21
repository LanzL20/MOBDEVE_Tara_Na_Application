package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingSignupBinding
import kotlinx.coroutines.launch

class BoardingSignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityBoardingSignupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityBoardingSignupBtnSignup.setOnClickListener {
            val intent = Intent(this, BoardingPhoneAuthActivity::class.java)
            var phoneNumber= viewBinding.activitySignupLoginEtPhone.text.toString()
            val name= viewBinding.activitySignupLoginEtName.text.toString()
            val password= viewBinding.activitySignupLoginEtPasswordd.text.toString()
            //username will be the name without spaces and attached with a random 4 digit number
            val username= name.replace("\\s".toRegex(), "")+((1000..9999).random()).toString()
            phoneNumber= "+63${phoneNumber.substring(1)}"
            intent.putExtra(User.NAME_KEY, name)
            intent.putExtra(User.USERNAME_KEY, username)
            intent.putExtra(User.PHONE_NUMBER_KEY, phoneNumber)
            intent.putExtra(User.PASSWORD_KEY, password)
            intent.putExtra("from", "signup")

            if(phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()){
                viewBinding.activitySignupLoginEtPhone.error="Please fill up this field"
                viewBinding.activitySignupLoginEtName.error="Please fill up this field"
                viewBinding.activitySignupLoginEtPasswordd.error="Please fill up this field"
                return@setOnClickListener
            }
            if(phoneNumber.length!=13){
                viewBinding.activitySignupLoginEtPhone.error="Invalid phone number length"
                return@setOnClickListener
            }



            //check if user is already registered
            lifecycleScope.launch{
                val userExists= DBHelper.checkIfUserExists(username, phoneNumber)
                Log.d("MainActivity", "User exists: $userExists")
                if(userExists==3){
                    viewBinding.activitySignupLoginEtName.error="User already exists"
                    viewBinding.activitySignupLoginEtPhone.error="Phone number already exists"
                    return@launch
                }
                if(userExists==2){
                    viewBinding.activitySignupLoginEtPhone.error="Phone number already exists"
                    return@launch
                }
                if(userExists==1){
                    viewBinding.activitySignupLoginEtName.error="User already exists"
                    return@launch
                }
                if(userExists==0){
                    startActivity(intent)
                    return@launch
                }
            }
        }
        viewBinding.activityBoardingSignupTvLogin.setOnClickListener {
            val intent = Intent(this, BoardingLoginActivity::class.java)
            startActivity(intent)
        }

    }
}