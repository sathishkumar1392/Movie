package com.task.movie.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.task.movie.R
import com.task.movie.utilis.CommonUtils
import com.task.movie.utilis.Constants
import kotlinx.android.synthetic.main.activity_moviedetails.*

/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :21-11-2019 20:21
 * Updated on : 
 * File Name : MovieDetailsPageActivity.kt
 * ClassName : 
 * Module Name : app
 * Desc : 
 */
class MovieDetailsPageActivity : AppCompatActivity() {

    private var getActivityContext: Context? = null
    private lateinit var moviename: String
    private lateinit var desc: String
    private lateinit var posterpath: String
    private var ratingvalue: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moviedetails)
        getActivityContext = this@MovieDetailsPageActivity
        getBundleValue()
        loadData()
    }


    private fun getBundleValue() {
        val intent: Intent? = intent
        val bundle = intent!!.extras
        moviename = bundle!!.getString(Constants.TAG_MovieName)!!
        desc = bundle.getString(Constants.TAG_DESC)!!
        posterpath = bundle.getString(Constants.TAG_IMAGE)!!
        ratingvalue = bundle.getDouble(Constants.TAG_RATINGBAR)
    }


    private fun loadData() {
        if (moviename.isNotEmpty())  txtView_MovieName.text = moviename else txtView_MovieName.visibility = View.GONE
        if (desc.isNotEmpty())txtView_MovieDesc.text = desc else txtView_MovieDesc.visibility = View.GONE
        if (posterpath.isNotEmpty())  CommonUtils.setImagePoster(posterpath,ImgView_MoviePoster) else ImgView_MoviePoster.visibility = View.GONE
        if (!ratingvalue.equals(0.0))  rating_Bar.rating = ratingvalue.div(2).toFloat() else rating_Bar.visibility = View.GONE
    }




    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}