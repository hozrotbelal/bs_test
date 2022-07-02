package com.example.bs_test.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy

import com.example.bs_test.R
import com.example.bs_test.data.storage.SessionPreference
import com.example.bs_test.extension.px
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class BindingUtil @Inject constructor(private val mPref: SessionPreference) {
    @BindingAdapter(value = ["loadImageFromUrl", "imageWidth", "imageHeight"], requireAll = false)
    fun bindImageFromUrl(view: ImageView, imageUrl: String?, width: Int = 0, height: Int = 0) {
        if (imageUrl.isNullOrEmpty()) {
            if(android.os.Build.VERSION.SDK_INT < 24) {
                view.scaleType = ImageView.ScaleType.FIT_XY
            }
            view.setImageResource(R.drawable.ic_launcher_background)
        } else {
            if(android.os.Build.VERSION.SDK_INT < 24) {
                view.scaleType = ImageView.ScaleType.FIT_XY
            }
            view.load(imageUrl) {
                fallback(R.drawable.ic_launcher_background)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
                diskCachePolicy(CachePolicy.ENABLED)
                crossfade(false)
//                UtilsKt.getImageSize(view.context, 720).apply {
//                    size(x, y)
//                }

                if (width > 0 && height > 0) {
                    size(width, height)
                }
                else {
                    size(min(360.px, 720), min(202.px, 405))
                }
                listener(
                    onSuccess = { _, _->
                        view.scaleType = ImageView.ScaleType.CENTER_CROP
                })
            }
        }
    }

    @BindingAdapter("loadImageFromUrlRounded")
    fun bindRoundImage(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            view.load(imageUrl) {
            //    transformations(CircleCropTransformation())
             //   crossfade(false)
                fallback(R.drawable.ic_launcher_background)
                placeholder(R.drawable.ic_launcher_background)
              //  error(R.drawable.ic_launcher_background)
                diskCachePolicy(CachePolicy.ENABLED)
             //   size(min(80.px, 150), min(80.px, 150))
            }
        } else {
            view.setImageResource(R.drawable.ic_launcher_background)
        }
    }


}