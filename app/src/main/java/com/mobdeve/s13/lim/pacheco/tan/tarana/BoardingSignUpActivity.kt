package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingSignupBinding

class BoardingSignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityBoardingSignupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityBoardingSignupBtnSignup.setOnClickListener {
            val intent = Intent(this, BoardingGreetingActivity::class.java)
            val number= viewBinding.activitySignupLoginEtPhone.text.toString()
            val username= viewBinding.activitySignupLoginEtName.text.toString()
            val password= viewBinding.activitySignupLoginEtPasswordd.text.toString()

            val firebaseauth: FirebaseAuth = FirebaseAuth.getInstance()
            // use phone number, username, and password to create a new user
            firebaseauth.createUserWithEmailAndPassword(number, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = User(username, password, number, R.drawable.ic_launcher_foreground, number)
                        DBHelper.addUser(user)
                        startActivity(intent)
                    }
                }

            startActivity(intent)
        }
        viewBinding.activityBoardingSignupTvLogin.setOnClickListener {
            val intent = Intent(this, BoardingLoginActivity::class.java)
            startActivity(intent)
        }

    }
}