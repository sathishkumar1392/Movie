package com.task.movie.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.task.movie.R
import com.task.movie.databinding.ItemMovieDetailsBinding
import com.task.movie.model.Result
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :17-11-2019 22:39
 * File Name : MovieAdapter.kt
 * ClassName :  MovieAdapter
 * Module Name : app
 * Desc : 
 */

 class MovieAdapter :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private lateinit var movieList: List<Result>
    private val clickSubject = PublishSubject.create<Result>()
    val clickEvent: Observable<Result> = clickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMovieDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie_details, parent, false
        )
        return ViewHolder(binding, clickSubject)
    }

    override fun getItemCount(): Int {
        return if (::movieList.isInitialized) movieList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    internal fun updateMovieList(movieList: List<Result>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }


     class ViewHolder(
        private val binding: ItemMovieDetailsBinding,
        clickSubject: PublishSubject<Result>
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var item: Result? = null
        val publishSub = clickSubject

        init {
            itemView.setOnClickListener(this)
        }

        internal fun bind(item: Result) {
            this.item = item
            binding.itemDetails = item
        }

        override fun onClick(v: View?) {
            item?.let { publishSub.onNext(it) }
        }
    }
}
