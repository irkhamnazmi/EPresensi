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

        val pageRequest = intent.getStringExtra("page_request")
        if (pageRequest.equals("webview")){
            val navOption = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentSignSuccess, true)
                .build()

            Navigation.findNavController(findViewById(R.id.authHostFragment))
                .navigate(R.id.action_webview, null,navOption)
        }
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