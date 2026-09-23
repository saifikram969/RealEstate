package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.databinding.FragmentWebViewBinding


class WebViewFragment() : BaseFragment<FragmentWebViewBinding>() {

    val args : WebViewFragmentArgs by navArgs()
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebViewBinding =FragmentWebViewBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.webView.webViewClient = WebViewClient()

        binding.webView.loadUrl(args.WebUrl)
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.settings.setSupportZoom(true)



    }




}