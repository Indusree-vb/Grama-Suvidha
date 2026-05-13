package com.gramasuvidha.portal.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            view.load(it) {
                crossfade(true)
                // placeholder(R.drawable.placeholder)
                // error(R.drawable.error)
            }
        }
    }
}
