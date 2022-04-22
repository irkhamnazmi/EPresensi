package com.rsudbrebes.epresensi.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pageRequest = intent.getIntExtra("page_request",0)
        if(pageRequest == 1){
            val navOption = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentCheck, true)
                .build()

            Navigation.findNavController(findViewById(R.id.homeHostFragment))
                .navigate(R.id.action_check_success, null, navOption)
        }

        binding.swReload.setOnRefreshListener {

            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }
    }

}