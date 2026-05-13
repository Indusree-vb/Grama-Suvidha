package com.gramasuvidha.portal.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrBlank()) {
            view.load(url) {
                crossfade(true)
                placeholder(android.R.drawable.progress_horizontal)
                error(android.R.drawable.stat_notify_error)
            }
        } else {
            view.setImageResource(android.R.color.darker_gray)
        }
    }
}
