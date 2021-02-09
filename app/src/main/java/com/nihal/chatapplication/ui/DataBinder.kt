package com.nihal.chatapplication.ui

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nihal.chatapplication.R

/**
 * Loads images into ImageView using Glide.
 * @param imageView The image view where the image will be loaded to.
 * @param url The url to download the image from.
 */
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    val context: Context = imageView.context
    Glide.with(context).load(url)
        .placeholder(R.drawable.defaultprofilepicture).into(imageView)
}
