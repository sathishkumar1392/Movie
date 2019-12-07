package com.task.movie.view.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
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



@BindingAdapter(value = ["app:progressScaled"], requireAll = true)
fun setProgress(progressBar: ProgressBar,  max: Double) {
    progressBar.progress = max.div(2).toInt()
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


   /* @BindingAdapter("tmdbPosterPath", "tmdbImageUrlProvider", "imageSaturateOnLoad")
    fun loadPoster(view: ImageView, path: String?, urlProvider: TmdbImageUrlProvider?, saturateOnLoad: Boolean?) {
        GlideApp.with(view).clear(view)

        if (path != null && urlProvider != null) {
            view.doOnLayout {
                GlideApp.with(view)
                    .let { r -> if (saturateOnLoad == true) r.saturateOnLoad() else r.asDrawable() }
                    .load(urlProvider.getPosterUrl(path, it.width))
                    .thumbnail(GlideApp.with(view).load(urlProvider.getPosterUrl(path, 0)))
                    .into(view)
            }
        }
    }*/

}



