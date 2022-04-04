package com.rsudbrebes.epresensi.ui.auth.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.fragment.app.Fragment
import com.rsudbrebes.epresensi.databinding.FragmentWebviewBinding

class WebviewFragment : Fragment() {
    private lateinit var binding: FragmentWebviewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val url = requireActivity().intent.getStringExtra("url")
        binding.webview1.loadUrl(url.toString())
        val webSettings = binding.webview1.settings
        webSettings.javaScriptEnabled = true
    }



}