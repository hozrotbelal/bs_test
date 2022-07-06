package com.example.bs_test.data.interfaces

import android.view.View

interface BottomSheetSlideCallback {
    fun onStateChanged(bottomSheet: View, newState: Int)
    fun onSlide(bottomSheet: View, slideOffset: Float)
}