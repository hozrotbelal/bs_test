package com.example.bs_test.ui.search

import android.graphics.Color
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.bs_test.R
import com.example.bs_test.data.interfaces.AfterCallBack
import com.example.bs_test.ui.widget.superbottomsheet.SuperBottomSheetFragment
import com.example.bs_test.data.interfaces.BottomSheetSlideCallback
import com.example.bs_test.databinding.FragmentSearchLayoutFilterViewBinding
import com.example.bs_test.managers.StateManageClass

//import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


@AndroidEntryPoint
class SearchFilterBottomSheetFragment : SuperBottomSheetFragment(), BottomSheetSlideCallback
     {
    private var _binding: FragmentSearchLayoutFilterViewBinding? = null
    private val binding get() = _binding!!
    private var emailTextWatcher: TextWatcher? = null

    companion object {
        const val TAG = "BottomSheetDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentSearchLayoutFilterViewBinding.inflate(layoutInflater)

        callback = this

        binding.rbGroupSortBy.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_sort_by_stars) {
                binding.rbSortByStars.isChecked = true
                binding.rbSortByForks.isChecked = false
                binding.rbSortByLastUpdate.isChecked = false
                StateManageClass.sortBy = "stars"

            }
            if (checkedId == R.id.rb_sort_by_forks) {
                binding.rbSortByStars.isChecked = false
                binding.rbSortByForks.isChecked = true
                binding.rbSortByLastUpdate.isChecked = false
                StateManageClass.sortBy = "forks"
            }
            if (checkedId == R.id.rb_sort_by_last_update) {
                binding.rbSortByStars.isChecked = false
                binding.rbSortByForks.isChecked = false
                binding.rbSortByLastUpdate.isChecked = true
                StateManageClass.sortBy = "updated"
            }
        }
            binding.rbGroupOrderBy.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.rb_order_by_ascending) {
                    binding.rbOrderByAscending.isChecked = true
                    binding.rbOrderByDescending.isChecked = false
                    StateManageClass.orderBy = "asc"

                }
                if (checkedId == R.id.rb_order_by_descending) {
                    binding.rbOrderByAscending.isChecked = false
                    binding.rbOrderByDescending.isChecked = true
                    StateManageClass.orderBy = "desc"
                }
            }
        binding.tvClear.setOnClickListener {
            dismiss()
            try {
                //findNavController().navigate(R.id.SelectSongListBottomSheetFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _binding = null
        }

        binding.btnNext.setOnClickListener {

            dismiss()
        }


//        KeyboardVisibilityEvent.setEventListener(
//            requireActivity()
//        ) { isOpen ->
//            if (isOpen) {
//                afterCallBack()
//            } else {
//                afterCallBackAgain()
//            }
//        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun getPeekHeight() = requireContext().resources.getDimension(R.dimen._392sdp).toInt()
    override fun getDim() = 0f
    override fun isSheetCancelableOnTouchOutside() = false

    override fun getCornerRadius() = requireContext().resources.getDimension(R.dimen._20sdp)

    override fun getStatusBarColor() = Color.WHITE


    override fun getTheme(): Int = R.style.SheetDialog

    override fun onDestroy() {
        super.onDestroy()
        //binding.etDesc.removeTextChangedListener(emailTextWatcher)
        _binding = null

    }

         override fun onStateChanged(bottomSheet: View, newState: Int) {
         }


         override fun onSlide(bottomSheet: View, slideOffset: Float) {
        if (slideOffset.isNaN() || slideOffset <= 0) {
            binding.bottomSheetTopImage.alpha = 1f
            //img_cross?.visibility=View.VISIBLE
            return
        }
        val invertOffset = 1 - (1 * slideOffset)
        binding.bottomSheetTopImage.alpha = invertOffset
    }

}
