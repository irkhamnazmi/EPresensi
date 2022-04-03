package com.rsudbrebes.epresensi.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.ActivitySplashScreenBinding
import com.rsudbrebes.epresensi.databinding.FragmentSignupBinding
import com.rsudbrebes.epresensi.ui.auth.AuthActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.includedFooter.tvFooter.text = "Powered by:\n Team IT RSUD BREBES"


        Handler().postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        },3000)
    }
}