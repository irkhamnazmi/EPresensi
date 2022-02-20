package com.rsudbrebes.epresensi.ui.auth.signsuccess

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSignSuccessBinding
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.auth.AuthActivity


class SignSuccessFragment : Fragment() {

    private lateinit var binding: FragmentSignSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(EPresensi.getApp().getUser().isNullOrEmpty()){
            view?.let { Navigation.findNavController(it).navigate(R.id.action_signin) }
        }
        if(EPresensi.getApp().getActive().isNullOrEmpty()){
            binding.btnNext.setOnClickListener {
                EPresensi.getApp().setActive("1")
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()

            }
        }
        else {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }


    }


}