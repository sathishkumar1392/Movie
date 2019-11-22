package com.task.movie.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.squareup.picasso.Picasso
import com.task.movie.R
import com.task.movie.utilis.Constants
import getParentActivity
import kotlin.math.roundToInt


/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :19-11-2019 22:44
 * Updated on : 
 * File Name : BindingAdap.kt
 * ClassName : 
 * Module Name : app
 * Desc : 
 */






@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(
            parentActivity,
            Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}


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


