package com.example.bs_test.ui.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.bs_test.data.storage.SessionPreference
import com.example.bs_test.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    @Inject
    lateinit var mPref: SessionPreference

    override fun onAttach(context: Context) {
        super.onAttach(LocaleHelper.onAttach(context))
    }
}