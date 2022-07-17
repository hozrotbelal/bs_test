package com.example.bs_test.ui.home
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.bs_test.databinding.FragmentHomeBinding
import com.example.bs_test.ui.common.BaseFragment
import com.example.bs_test.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var mContext: Activity? = null
    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}