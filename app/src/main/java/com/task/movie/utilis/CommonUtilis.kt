package com.task.movie.utilis

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.task.movie.R

/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :22-11-2019 01:29
 * Updated on : 
 * File Name : CommonUtilis.kt
 * ClassName : 
 * Module Name : app
 * Desc : 
 */

object CommonUtilis {

    fun setImageThumnail( imagepath: String, mImageView: ImageView) {
        Picasso.get().load(Constants.TAG_IMAGEURL + imagepath)
            .placeholder(R.drawable.ic_launcher_background).into(mImageView)
    }
}