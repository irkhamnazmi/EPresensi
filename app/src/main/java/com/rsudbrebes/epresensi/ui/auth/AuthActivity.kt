package com.rsudbrebes.epresensi.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.rsudbrebes.epresensi.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val pageRequest = intent.getIntExtra("page_request",0)
//        if (pageRequest == 1){
//            val navOption = NavOptions.Builder()
//                .setPopUpTo(R.id.fragmentSignIn, true)
//                .build()
//
//            Navigation.findNavController(findViewById(R.id.authHostFragment))
//                .navigate(R.id.action_signup, null,navOption)
//        }
//
//
//        if (pageRequest == 2){
//            val navOption = NavOptions.Builder()
//                .setPopUpTo(R.id.fragmentSignUp, true)
//                .build()
//
//            Navigation.findNavController(findViewById(R.id.authHostFragment))
//                .navigate(R.id.action_signin, null,navOption)
//        }



    }
}