package com.rsudbrebes.epresensi.ui.home.checksuccess

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsudbrebes.epresensi.R
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
        binding.tvCheck.setTextColor(Color.parseColor("#00FC44"))
    }


}