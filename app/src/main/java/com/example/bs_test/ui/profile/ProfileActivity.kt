package com.example.bs_test.ui.profile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bs_test.R
import com.example.bs_test.data.model.Item
import com.example.bs_test.data.storage.PreferenceStorage
import com.example.bs_test.databinding.ActivityProfileBinding
import com.example.bs_test.ui.viewmodel.MainViewModel
import com.example.bs_test.utils.INTENT_KEY_ITEM
import com.example.bs_test.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import org.parceler.Parcels
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!
    private var data = true
    private val mainViewModel : MainViewModel by viewModels()
    lateinit var selectionItem: Item
    @Inject
    lateinit var preferences: PreferenceStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        val mParcelableItem: Parcelable = intent.getParcelableExtra(INTENT_KEY_ITEM)!!
        if (mParcelableItem != null) {
            selectionItem = Parcels.unwrap(mParcelableItem)
            Log.e("mParcelableItem",selectionItem.toString()+">>")
        }
      _binding!!.imgBack.setOnClickListener {
          onBackPressed()
      }

        if(selectionItem != null) {
            Glide.with(this)
                .load(selectionItem.owner!!.avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(_binding!!.ivProfile)
            _binding!!.tvProfileName.text = selectionItem.name
            _binding!!.tvProfileFullName.text = selectionItem.fullName
            _binding!!.tvProfileDescription.text = selectionItem.description
            _binding!!.tvProfileProjectLink.text = selectionItem.html_url
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }


    override fun onBackPressed() {
        super.onBackPressed()
        Timber.e("onBackPressed")
    }


    fun visible(activity: ProfileActivity) {
        activity.data = false
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

}