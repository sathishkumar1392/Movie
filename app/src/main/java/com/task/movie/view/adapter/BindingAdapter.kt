package com.task.movie.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.task.movie.R
import com.task.movie.utilis.Constants


/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :19-11-2019 22:44
 * Updated on : 
 * File Name : BindingAdapter.kt
 * ClassName : 
 * Module Name : app
 * Desc : 
 */

@BindingAdapter("image")
fun setPosterPath(view: ImageView, imageURL: String?) {
    Picasso.get()
        .load(Constants.TAG_IMAGEURL + imageURL)
        .placeholder(R.drawable.ic_launcher_background)
        .into(view)
}

@BindingAdapter("ratingBar")
fun setRatingBarStatus(view: RatingBar, ratingBar: Double?) {
    if (!ratingBar!!.equals(0.0)) {
        val value = ratingBar.div(2)
        view.rating = value.toFloat()
    } else {
        view.visibility = View.GONE
    }
}



