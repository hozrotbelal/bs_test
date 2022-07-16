package com.example.bs_test.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bs_test.R
import com.example.bs_test.data.interfaces.SearchSelectionListener
import com.example.bs_test.data.model.Item
import com.example.bs_test.data.network.Resource
import com.example.bs_test.data.network.Status
import com.example.bs_test.data.storage.PreferenceStorage
import com.example.bs_test.databinding.ActivityMainBinding
import com.example.bs_test.managers.StateManageClass
import com.example.bs_test.ui.adapter.SearchAdapter
import com.example.bs_test.ui.viewmodel.MainViewModel
import com.example.bs_test.util.isEmpty
import com.example.bs_test.utils.LocaleHelper
import com.example.bs_test.utils.SEARCH_KEY
import com.example.bs_test.utils.SEARCH_TIME_DELAY
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchSelectionListener {
    var navController: NavController? = null
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var constraintSet1: ConstraintSet? = null
    private var constraintSet2: ConstraintSet? = null
    private var zoom = false
    private var data = true
    private var move = true
    private var moveToMap = true
    private var isGPSEnabled = false
    private var orderBy: String? = null
    private var sortBy: String? = null
   private val mainViewModel : MainViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private val mHandlerSearchChange: Handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var preferences: PreferenceStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel.getReposByList(SEARCH_KEY,StateManageClass.sortBy,StateManageClass.orderBy)

        setContentView(binding.root)
        setupNavController()
        setUpView()
    }

    private fun setUpView() {

        searchAdapter = SearchAdapter(this, mutableListOf(), mainViewModel, this)
        binding?.rcSearch.apply {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = searchAdapter

        }

        mainViewModel.postData.observe(this) {
            it?.let { resource ->
                run {
                    when (resource.status) {
                        Status.SUCCESS -> {
                            searchAdapter.stopPagination(mainViewModel.mIsLastPage)
                            if (searchAdapter.hasData()) {
                                Log.e("resource>>>",resource.data!!.toString()+"")
                                searchAdapter.addAll(resource.data!!)
                            } else {
                                Log.e("postList>>>",mainViewModel.postList.toString()+"")

                                searchAdapter.addAll(mainViewModel.postList)
                            }
                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {

                        }
                    }
                }
            }
        }

        val mRunnableSearchChange = kotlinx.coroutines.Runnable {
            observeLatestDataList()

        }

        _binding!!.ivFilter.setOnClickListener {
           navController?.navigate(R.id.SearchFilterBottomSheetFragment)
          //onFilterBottomDialog()
       }
        Timber.e("shouldOverrideUrlLoading--");

      //  navController?.navigate(R.id.homeScreenFragment)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                mHandlerSearchChange.removeCallbacks(mRunnableSearchChange)
            }

            override fun afterTextChanged(editable: Editable) {
                mHandlerSearchChange.postDelayed(mRunnableSearchChange, SEARCH_TIME_DELAY)
            }
        })

    }

    private fun observeLatestDataList() {
//        if (isEmpty(_binding!!.etSearch.text.toString().trim())) {
//            binding.rcSearch.visibility = View.GONE
//            return
//        }

        SEARCH_KEY = _binding!!.etSearch.text.toString().trim()
        if (mainViewModel.postList.size > 0){
            mainViewModel.postData.postValue(Resource.loading(null))
            mainViewModel.postList.clear()

        }

        lifecycleScope.launchWhenResumed {
            binding.rcSearch.visibility = View.VISIBLE
            if(SEARCH_KEY.isNullOrEmpty()){
                SEARCH_KEY ="android"
            }
            mainViewModel.getReposByList(SEARCH_KEY,StateManageClass.sortBy,StateManageClass.orderBy)
            searchAdapter.setItemList(mainViewModel.postList)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }



    override fun onBackPressed() {
        super.onBackPressed()
        Log.e("exx", "ddd" + data)
        Timber.e("onBackPressed")

    }


    fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.homeScreenFragment) as NavHostFragment
        navController = navHostFragment.navController
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            supportFragmentManager.popBackStack(
                R.id.content_viewer,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

    }

    fun visible(activity: MainActivity) {
        activity.data = false
    }

    fun invisible(activity: MainActivity) {
        activity.data = true
    }


    fun show(activity: MainActivity) {

        val navHostFragment =
            activity.supportFragmentManager.findFragmentById(R.id.homeScreenFragment) as NavHostFragment
        navController = navHostFragment.navController

        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            activity.supportFragmentManager.popBackStack(
                R.id.content_viewer,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

        }

//        Handler().postDelayed(Runnable {
//            TransitionManager.beginDelayedTransition(activity.constraintLayout)
//            activity.constraintSet1!!.applyTo(activity.constraintLayout)
//        }, 10)

        Timber.e("show")

        //  activity.con_layout.visibility = View.VISIBLE


        activity.zoom = false
        navController?.popBackStack()


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

        }
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSearchSelect(searchSelection: Item) {
        Log.e("searchSelection", "searchSelection" + searchSelection.toString())
       // findNavController().navigate(R.id.profileFragment)

       // navController?.navigate(R.id.profileFragment)
    }

    override fun onPagination() {
        if (mainViewModel.mIsLoading || mainViewModel.mIsLastPage) {
            return
        }
        mainViewModel.getReposByList(SEARCH_KEY,StateManageClass.sortBy,StateManageClass.orderBy)
    }



}