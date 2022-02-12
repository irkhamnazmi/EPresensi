package com.rsudbrebes.epresensi.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSigninBinding
import com.rsudbrebes.epresensi.ui.auth.AuthActivity

class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      binding.tvRegister.setOnClickListener {
          val signup = Intent(activity, AuthActivity::class.java)
          signup.putExtra("page_request",1)
          startActivity(signup)
      }

      binding.btnSignin.setOnClickListener {
          val submit = Intent(activity, AuthActivity::class.java)
          submit.putExtra("page_request",2)
          startActivity(submit)
      }

    }


}