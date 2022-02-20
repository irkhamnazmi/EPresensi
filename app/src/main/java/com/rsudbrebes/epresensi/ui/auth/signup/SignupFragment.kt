package com.rsudbrebes.epresensi.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsudbrebes.epresensi.databinding.FragmentSignupBinding
import com.rsudbrebes.epresensi.ui.auth.AuthActivity

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvLogin.setOnClickListener {
            val signin = Intent(activity, AuthActivity::class.java)
            signin.putExtra("page_request",2)
            startActivity(signin)
        }
    }


}