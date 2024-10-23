package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingLoginBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityBoardingWelcomeBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.ui.theme.TaraNaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding= ActivityBoardingWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.activityBoardingWelcomeBtnSignUp.setOnClickListener {
            Log.d("MainActivity", "Sign Up Button Clicked")
            val intent = Intent(this, BoardingSignUpActivity::class.java)
            startActivity(intent)
        }
        viewBinding.activityBoardingWelcomeTvLogin.setOnClickListener {
            val intent = Intent(this, BoardingLoginActivity::class.java)
            startActivity(intent)
        }
    }
}
