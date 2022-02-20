package com.rsudbrebes.epresensi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.rsudbrebes.epresensi.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pageRequest = intent.getIntExtra("page_request",0)
        if(pageRequest == 1){
            val navOption = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentCheck, true)
                .build()

            Navigation.findNavController(findViewById(R.id.homeHostFragment))
                .navigate(R.id.action_check_success, null, navOption)
        }
    }

}