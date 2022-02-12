package com.rsudbrebes.epresensi.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentCheckBinding
import com.rsudbrebes.epresensi.ui.MainActivity


class CheckFragment : Fragment() {

    private lateinit var binding : FragmentCheckBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCheckIn.setOnClickListener {
            val checkin = Intent(activity, MainActivity::class.java)
            checkin.putExtra("page_request",1)
            startActivity(checkin)
        }
    }

}