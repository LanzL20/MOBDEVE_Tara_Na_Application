package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingWelcomeBinding
import kotlinx.coroutines.launch

class BoardingWelcomeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding= ActivityBoardingWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        Log.d("MainActivity", "Welcome Activity Created")
        val currentuser= Firebase.auth.currentUser
        if(currentuser!=null){
            val intent = Intent(this, ProfileUserActivity::class.java)
            var user: User?= null
            lifecycleScope.launch{
                user= DBHelper.getUserFromNumber(currentuser.phoneNumber!!)
                UserSession.setUser(user!!)
                startActivity(intent)
            }
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