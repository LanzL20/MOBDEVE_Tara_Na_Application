package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingWelcomeBinding

class BoardingWelcomeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding= ActivityBoardingWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val currentuser= Firebase.auth.currentUser
        //sign out the user if there is a current user for testing purposes
        Firebase.auth.signOut()
        if(currentuser!=null){
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }


        viewBinding.activityBoardingWelcomeBtnSignUp.setOnClickListener {
            Log.d("MainActivity", "Sign Up Button Clicked")
            // force a crash
            val intent = Intent(this, BoardingSignUpActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityBoardingWelcomeTvLogin.setOnClickListener {
            val intent = Intent(this, BoardingLoginActivity::class.java)
            startActivity(intent)
        }
    }
}