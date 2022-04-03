package com.rsudbrebes.epresensi.ui.home.checksuccess

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsudbrebes.epresensi.databinding.FragmentCheckSuccessBinding


class CheckSuccessFragment : Fragment() {

    private lateinit var binding : FragmentCheckSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckSuccessBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val checkStatus = arguments?.getString("status")
        if(checkStatus.equals("checkIn")){
             binding.tvCheck.text = "Check-In"
        } else{
            binding.tvCheck.text = "Check-Out"
        }

        Handler().postDelayed({
            getActivity()?.recreate();
        },3000)

    }


}