package com.rsudbrebes.epresensi.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pageRequest = intent.getStringExtra("page_request")

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
        binding.swReload.setOnRefreshListener {

            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }



    }

    fun dispatchInformations(mesg: String?) {
        binding.includedFooter.tvFooter.text = mesg
    }
    fun dispatchAction(act : String?){
        if(act == "GONE"){
            binding.includedHeader.root.visibility = View.GONE
            binding.includedFooter.root.visibility = View.GONE
            binding.swReload.isEnabled = false
        } else{
            binding.includedHeader.root.visibility = View.VISIBLE
            binding.includedFooter.root.visibility = View.VISIBLE
            binding.swReload.isEnabled = true
        }

    }
}