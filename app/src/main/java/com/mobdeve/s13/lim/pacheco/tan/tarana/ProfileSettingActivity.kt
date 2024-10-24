package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityProfileSettingsBinding

class ProfileSettingActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}