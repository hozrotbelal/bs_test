package com.example.bs_test.ui.home
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import com.example.bs_test.databinding.FragmentHomeBinding
import com.example.bs_test.ui.common.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var mContext: Activity? = null
    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.e("HomePage","Loaded")
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireActivity()
//        webView.settings.setJavaScriptEnabled(true)
//        webView.clearCache(true)
////        webView.webViewClient = object : WebViewClient() {
////            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
////            }
////
////            override fun onPageFinished(view: WebView, url: String) {
////            }
////        }
//
//        webView.webViewClient = object : WebViewClient() {
//
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
//                Log.e("shouldOverrideUrlLoad",url)
//                if (url.startsWith("http://")) {
//                    return false
//                } else if (url.startsWith("tel")) {
//                   // telIntent(url)
//                    return true
//                }
//                return false
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest
//            ): Boolean {
//                Log.e("shouldOverrideUrlL",request.url.toString())
//
//                if (request.url.toString().startsWith("http://")) {
//                    return false
//                } else if (request.url.toString().startsWith("tel")) {
//                    //telIntent(request.url.toString())
//                    return true
//                }
//                return false
//            }
//
//            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
//                //hasPageStarted = true
//                try {
//                    /*if (customDialog != null && !customDialog.isShowing()) {
//                        customDialog.show()
//                    }*/
//                } catch (e: Exception) {
//                    Timber.e(e)
//                }
//                super.onPageStarted(view, url, favicon)
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//
////                if (!hasPageStarted) {
////                    if (lottie != null)
////                        lottie.visibility = View.VISIBLE
////
////                    webView.reload()
////                } else {
////                    if (lottie != null)
////                        lottie.visibility = View.GONE
////                }
//
//                /*if (customDialog != null && customDialog.isShowing()) {
//                    customDialog.dismiss()
//                }*/
//                Timber.d("onURL Load onPageFinished: $url")
//                /*if (url.contains("#exit")) {
//                    view.clearCache(true)
//                    val bundle = Bundle()
//                    bundle.putString(AppConstants.LOCALE, AppConstants.DONT_SET_LOCALE)
//                    NewLandingActivity.startActivity(
//                        context,
//                        bundle,
//                        AppConstants.FLAG_ACTIVITY.FLAG_ACTIVITY_CLEAR_TOP
//                    )
//                    finish()
//                } else if (url.contains("payment-failed") || url.contains("payment-success")) {
//                    getAccessToken(services)
//                } else {
//                    loadWebViewWithToken(view)
//                }*/
//                super.onPageFinished(view, url)
//            }
//
//            @TargetApi(Build.VERSION_CODES.M)
//            override fun onReceivedError(
//                view: WebView,
//                request: WebResourceRequest,
//                error: WebResourceError
//            ) {
//                Timber.e("Error: %s", error.description)
//                Log.e("onReceivedError",error.description.toString())
//
//                super.onReceivedError(view, request, error)
//            }
//        }
//
//        webView.setInitialScale(1);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.loadUrl("https://bd50.ocdev.me/beats/")
//        clickView.setOnClickListener {
//Log.e("Load","Music Screen")
//       //navigate(HomeFragmentDirections.actionHomeScreenFragmentToMusicPlayScreenFragment())

      //  }
    }


}