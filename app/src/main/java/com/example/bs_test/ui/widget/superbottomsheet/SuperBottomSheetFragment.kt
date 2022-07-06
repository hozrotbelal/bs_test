/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 André Sousa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.bs_test.ui.widget.superbottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.bs_test.R
import com.example.bs_test.data.interfaces.AfterCallBack
import com.example.bs_test.ui.widget.superbottomsheet.calculateColor
import com.example.bs_test.ui.widget.superbottomsheet.hasMinimumSdk
import com.example.bs_test.data.interfaces.BottomSheetBackCallback
import com.example.bs_test.data.interfaces.BottomSheetSlideCallback

abstract class SuperBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var sheetTouchOutsideContainer: View
    private lateinit var sheetContainer: CornerRadiusFrameLayout
    public lateinit var behavior: BottomSheetBehavior<*>
    private lateinit var btn_header_back: AppCompatImageButton

    // Customizable properties
    private var propertyDim = 0f
    private var propertyCornerRadius = 0f
    private var propertyStatusBarColor = 0
    private var propertyIsAlwaysExpanded = false
    private var propertyIsSheetCancelableOnTouchOutside = false
    private var propertyIsSheetCancelable = false
    private var propertyAnimateCornerRadius = true

    // Bottom sheet properties
    private var canSetStatusBarColor = false

    //callback for bottom sheet slide
    var callback: BottomSheetSlideCallback? = null
    var afterCallBack: AfterCallBack? = null

    //callback for bottom sheet slide
    var backBtncallback: BottomSheetBackCallback? = null

    /** Methods from [BottomSheetDialogFragment]  */

    open override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (animateStatusBar()) {

            return object : SuperBottomSheetDialog(this.requireActivity(), R.style.superBottomSheetDialog) {
                override fun onBackPressed() {
                    // Handle backpress in here
                    Log.e("BACK","Back Button pressed")
                    backBtncallback?.onBackBtnPressed()
                }
            }
        }

        return object : SuperBottomSheetDialog(this.requireContext()!!) {
            override fun onBackPressed() {
                // Handle backpress in here
                Log.e("BACK","Back Button pressed")
                backBtncallback?.onBackBtnPressed()
            }
        }
    }

    @CallSuper
    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change status bar on the condition: API >= 21
        val supportsStatusBarColor = hasMinimumSdk(Build.VERSION_CODES.LOLLIPOP)
        canSetStatusBarColor = !requireContext().isTablet() && supportsStatusBarColor

        // Init properties
        propertyDim = getDim()
        propertyCornerRadius = getCornerRadius()
        propertyStatusBarColor = getStatusBarColor()
        propertyIsAlwaysExpanded = isSheetAlwaysExpanded()
        propertyIsSheetCancelable = isSheetCancelable()
        propertyIsSheetCancelableOnTouchOutside = isSheetCancelableOnTouchOutside()
        propertyAnimateCornerRadius = animateCornerRadius()

        dialog.runIfNotNull {
            setCancelable(propertyIsSheetCancelable)

            val isCancelableOnTouchOutside =
                propertyIsSheetCancelable && propertyIsSheetCancelableOnTouchOutside
            setCanceledOnTouchOutside(isCancelableOnTouchOutside)

            window.runIfNotNull {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(propertyDim)

                if (supportsStatusBarColor) {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    setStatusBarColor(1f)
                }

                if (requireContext().isTablet() && !requireContext().isInPortrait()) {
                    setGravity(Gravity.CENTER_HORIZONTAL)
                    setLayout(
                        resources.getDimensionPixelSize(R.dimen.super_bottom_sheet_width),
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }
        }

        return null
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        // Init UI components
        try {
            iniBottomSheetUiComponents()
        } catch (e: Exception) {
        }
    }

    //region UI METHODS

    @UiThread
    private fun iniBottomSheetUiComponents() {
        // Store views references
        sheetContainer = dialog?.findViewById(R.id.super_bottom_sheet)!!
      //  btn_header_back = dialog?.findViewById(R.id.btn_header_back)!!
        sheetTouchOutsideContainer = dialog?.findViewById(R.id.touch_outside)!!

        // Set the bottom sheet radius
        sheetContainer.setBackgroundColor(getBackgroundColor())
        sheetContainer.setCornerRadius(propertyCornerRadius)

        // Load bottom sheet behaviour
        behavior = BottomSheetBehavior.from(sheetContainer)

        //set the back button functionality
//        btn_header_back.setOnClickListener{
//            backBtncallback?.onBackBtnPressed()
//        }

        // Set tablet sheet width when in landscape. This will avoid full bleed sheet
        if (requireContext().isTablet() && !requireContext().isInPortrait()) {
            val layoutParams = sheetContainer.layoutParams
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.super_bottom_sheet_width)
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            sheetContainer.layoutParams = layoutParams
        }

        // If is always expanded, there is no need to set the peek height
        if (!propertyIsAlwaysExpanded) {
            behavior.peekHeight = getPeekHeight()

            sheetContainer.run {
                minimumHeight = behavior.peekHeight
            }
        } else {
            val layoutParams = sheetContainer.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            sheetContainer.layoutParams = layoutParams
        }

        // Only skip the collapse state when the device is in landscape or the sheet is always expanded
        val deviceInLandscape =
            (!requireContext().isTablet() && !requireContext().isInPortrait()) || propertyIsAlwaysExpanded
        behavior.skipCollapsed = deviceInLandscape

        if (deviceInLandscape) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            setStatusBarColor(1f)

            // Load content container height
            sheetContainer.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (sheetContainer.height > 0) {
                        sheetContainer.viewTreeObserver.removeOnPreDrawListener(this)

                        // If the content sheet is expanded set the background and status bar properties
                        if (sheetContainer.height == sheetTouchOutsideContainer.height) {
                            setStatusBarColor(0f)

                            if (propertyAnimateCornerRadius) {
                                sheetContainer.setCornerRadius(0f)
                            }
                        }
                    }

                    return true
                }
            })
        }

        // Override sheet callback events
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    callback?.let {
                        callback!!.onStateChanged(bottomSheet, newState)
                    }
                    backBtncallback?.let {
                        backBtncallback!!.onBackBtnPressed()
                    }
                    setStatusBarColor(1f)
                    dialog?.cancel()
                }
                else
                { try {
                        callback!!.onStateChanged(bottomSheet, newState)
                    } catch (e: Exception) {
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setRoundedCornersOnScroll(bottomSheet, slideOffset)
                setStatusBarColorOnScroll(bottomSheet, slideOffset)
                callback?.let {
                    callback!!.onSlide(bottomSheet, slideOffset)
                }
            }
        })
    }

    //region STATUS BAR

    @UiThread
    fun afterCallBack(){
        behavior.state=BottomSheetBehavior.STATE_EXPANDED
        afterCallBack?.onStateChanged(1)
    }
    fun afterCallBackAgain(){
        behavior.state=BottomSheetBehavior.STATE_COLLAPSED
        afterCallBack?.onStateChanged(1)
    }
    @UiThread
    private fun setStatusBarColorOnScroll(bottomSheet: View, slideOffset: Float) {
        if (!canSetStatusBarColor) {
            return
        }

        if (bottomSheet.height != sheetTouchOutsideContainer.height) {
            canSetStatusBarColor = false
            return
        }

        if (slideOffset.isNaN() || slideOffset <= 0) {
            setStatusBarColor(1f)
            return
        }

        val invertOffset = 1 - (1 * slideOffset)
        setStatusBarColor(invertOffset)

    }

    @SuppressLint("NewApi")
    @UiThread
    private fun setStatusBarColor(dim: Float) {
        if (!canSetStatusBarColor) {
            return
        }

        val color = calculateColor(propertyStatusBarColor, dim)
        dialog?.window!!.statusBarColor = color
    }

    //endregion

    //region CORNERS

    @UiThread
    private fun setRoundedCornersOnScroll(bottomSheet: View, slideOffset: Float) {
        if (!propertyAnimateCornerRadius) {
            return
        }

        if (bottomSheet.height != sheetTouchOutsideContainer.height) {
            propertyAnimateCornerRadius = false
            return
        }

        if (slideOffset.isNaN() || slideOffset <= 0) {
            sheetContainer.setCornerRadius(propertyCornerRadius)
            return
        }

        if (propertyAnimateCornerRadius) {
            val radius = propertyCornerRadius - (propertyCornerRadius * slideOffset)
            sheetContainer.setCornerRadius(radius)
        }
    }

    //endregion

    //region PUBLIC

    @Dimension
    open fun getPeekHeight(): Int = with(requireContext()!!.getAttrId(R.attr.superBottomSheet_peekHeight)) {
        val peekHeightMin = when (this) {
            INVALID_RESOURCE_ID -> resources.getDimensionPixelSize(R.dimen.super_bottom_sheet_peek_height)
            else -> resources.getDimensionPixelSize(this)
        }
        // 16:9 ratio
        return with(resources.displayMetrics) {
            val height = heightPixels - heightPixels * 9 / 16
            Math.max(peekHeightMin, height)
        }
    }

    @Dimension
    open fun getDim(): Float = with(requireContext()!!.getAttrId(R.attr.superBottomSheet_dim)) {
        return when (this) {
            INVALID_RESOURCE_ID -> TypedValue().run {
                resources.getValue(R.dimen.super_bottom_sheet_dim, this, true)
                float
            }

            else -> TypedValue().let {
                resources.getValue(this, it, true)
                it.float
            }
        }
    }
    @ColorInt
    open fun getBackgroundColor() = with(requireContext().getAttrId(R.attr.superBottomSheet_backgroundColor)) {
        when (this) {
            INVALID_RESOURCE_ID -> Color.WHITE
            else -> ContextCompat.getColor(requireContext(), this)
        }
    }

    @ColorInt
    open fun getStatusBarColor() = with(requireContext().getAttrId(R.attr.superBottomSheet_statusBarColor)) {
        when (this) {
            INVALID_RESOURCE_ID -> ContextCompat.getColor(requireContext(), requireContext().getAttrId(R.attr.colorPrimaryDark))
            else -> ContextCompat.getColor(requireContext(), this)
        }
    }


    @Dimension
    open fun getCornerRadius(): Float =
        with(requireContext()!!.getAttrId(R.attr.superBottomSheet_cornerRadius)) {
            return when (this) {
                INVALID_RESOURCE_ID -> requireContext()!!.resources.getDimension(R.dimen.super_bottom_sheet_radius)
                else -> resources.getDimension(this)
            }
        }

    open fun isSheetAlwaysExpanded(): Boolean =
        with(requireContext()!!.getAttrId(R.attr.superBottomSheet_alwaysExpanded)) {
            return when (this) {
                INVALID_RESOURCE_ID -> requireContext()!!.resources.getBoolean(R.bool.super_bottom_sheet_isAlwaysExpanded)
                else -> resources.getBoolean(this)
            }
        }

    open fun isSheetCancelableOnTouchOutside(): Boolean =
        with(requireContext()!!.getAttrId(R.attr.superBottomSheet_cancelableOnTouchOutside)) {
            return when (this) {
                INVALID_RESOURCE_ID -> requireContext()!!.resources.getBoolean(R.bool.super_bottom_sheet_cancelableOnTouchOutside)
                else -> resources.getBoolean(this)
            }
        }

    open fun isSheetCancelable(): Boolean =
        with(requireContext()!!.getAttrId(R.attr.superBottomSheet_cancelable)) {
            return when (this) {
                INVALID_RESOURCE_ID -> requireContext()!!.resources.getBoolean(R.bool.super_bottom_sheet_cancelable)
                else -> resources.getBoolean(this)
            }
        }

    open fun animateCornerRadius(): Boolean =
        with(requireContext()!!.getAttrId(R.attr.superBottomSheet_animateCornerRadius)) {
            return when (this) {
                INVALID_RESOURCE_ID -> requireContext()!!.resources.getBoolean(R.bool.super_bottom_sheet_animate_corner_radius)
                else -> resources.getBoolean(this)
            }
        }

    open fun animateStatusBar(): Boolean =
        with(requireContext()!!.getAttrId(R.attr.superBottomSheet_animateStatusBar)) {
            return when (this) {
                INVALID_RESOURCE_ID -> requireContext()!!.resources.getBoolean(R.bool.super_bottom_sheet_animate_status_bar)
                else -> resources.getBoolean(this)
            }
        }

    //endregion
}