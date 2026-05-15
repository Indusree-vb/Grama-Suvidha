package com.gramasuvidha.portal.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.gramasuvidha.portal.R

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrBlank()) {
            view.load(url) {
                crossfade(true)
                // Use built-in system placeholders for maximum reliability
                placeholder(android.R.drawable.ic_menu_report_image)
                error(android.R.drawable.stat_notify_error)
            }
        } else {
            view.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }
}
