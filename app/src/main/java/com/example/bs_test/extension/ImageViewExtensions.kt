package com.example.bs_test.extension

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation

import com.example.bs_test.R
import com.example.bs_test.extension.px

fun ImageView.loadProfileImage(imageUrl: String) {

    if (imageUrl.isBlank()) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background))
    } else {
        load(imageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
            size(80.px, 80.px)
        }
    }
}

fun ImageView.loadBase64(data: String) {
    try {
        val imageBytes = Base64.decode(data, Base64.NO_WRAP)
        val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        setImageBitmap(bmp)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}