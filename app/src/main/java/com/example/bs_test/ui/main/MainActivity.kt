package com.example.bs_test.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bs_test.R
import com.example.bs_test.data.interfaces.SearchSelectionListener
import com.example.bs_test.data.model.Item
import com.example.bs_test.data.network.Status
import com.example.bs_test.data.storage.PreferenceStorage
import com.example.bs_test.databinding.ActivityMainBinding
import com.example.bs_test.ui.adapter.SearchAdapter
import com.example.bs_test.ui.viewmodel.MainViewModel
import com.example.bs_test.utils.LocaleHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
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
   // lateinit var loginViewModel: MainViewModel
   private val mainViewModel : MainViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var preferences: PreferenceStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel.getReposByList("android","stars","desc")

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
                                searchAdapter.addAll(resource.data!!)
                            } else {
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

//        mainViewModel.highLightPostSelectionUpdate.observe(this) {
//            it?.let { resource ->
//                run {
//                    searchAdapter.selectionUpdate(resource)
//                }
//            }
//        }

        _binding!!.ivFilter.setOnClickListener {
            navController?.navigate(R.id.SearchFilterBottomSheetFragment)
           //onFilterBottomDialog()
       }
        Timber.e("shouldOverrideUrlLoading--");

      //  navController?.navigate(R.id.homeScreenFragment)
//        img_notification.setOnClickListener {
//            navController?.navigate(R.id.NotificationBottomSheetFragment)
//            //  goNotificationDetails(this)
//            img_commons.visibility = View.GONE
//            image_two.visibility = View.VISIBLE
//            image_one.visibility = View.VISIBLE
//            image_middle.visibility = View.VISIBLE
//        }
//        img_plus_icon.setOnClickListener {
//
//            //Check Location/GPS is ON or OFF
//            LocationUtil(this).turnGPSOn(object :
//                LocationUtil.OnLocationOnListener {
//                override fun locationStatus(isLocationOn: Boolean) {
//                    this@MainActivity.isGPSEnabled = isLocationOn
//                }
//            })
//            checkPermissions()
//            // navController?.navigate(R.id.HashtagSelectionBottomSheetFragment)
//            // showHideIcon(false)
//            // navController.navigate(R.id.MusicPlayScreenFragment)
//            StateManageClass.clear()
//            // navController?.navigate(HomeFragmentDirections.actionHomeScreenFragmentToMapScreen())
//        }



    }

     fun onFilterBottomDialog() {
        //  setViewAndChildrenEnabled(binding.swipeLayout,false)
        val dialog = BottomSheetDialog(this,R.style.CustomBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.fragment_search_layout_filter_view, null)
        val tvSkip = view.findViewById<TextView>(R.id.tv_filter)
        val rbGroupSortBy = view.findViewById<RadioGroup>(R.id.rb_group_sort_by)
        val rbStars = view.findViewById<RadioButton>(R.id.rb_sort_by_stars)
        val rbForks = view.findViewById<RadioButton>(R.id.rb_sort_by_forks)
        val rbLastUpdate = view.findViewById<RadioButton>(R.id.rb_sort_by_last_update)

        tvSkip.setOnClickListener {
            dialog.dismiss()
        }

         rbGroupSortBy.setOnCheckedChangeListener { group, checkedId ->
             if (checkedId == R.id.rb_sort_by_stars) {
                 rbStars.isChecked = true
                 rbForks.isChecked = false
                 rbLastUpdate.isChecked = false
                 Log.e("rbLeftQuestion","rbLeft");

             }
             if (checkedId == R.id.rb_sort_by_forks) {
                 rbStars.isChecked = false
                 rbForks.isChecked = true
                 rbLastUpdate.isChecked = false
                 Log.e("rbLeft","rbLeft");

             }
             if (checkedId == R.id.rb_sort_by_last_update) {
                 rbStars.isChecked = false
                 rbForks.isChecked = false
                 rbLastUpdate.isChecked = true
                 Log.e("rbRightQuestion","rbRight");

             }
         }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()

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

    }

    override fun onPagination() {
    }


}