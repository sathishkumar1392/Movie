package com.task.movie.viewmodel


import androidx.lifecycle.ViewModel
import com.task.movie.di.DaggerViewModelInjector
import com.task.movie.di.ViewModelInjector
import com.task.movie.remote.RetrofitClient

/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :17-11-2019 23:11
 * Updated on : 
 * File Name : BaseViewModel.kt
 * ClassName : BaseViewModel
 * Module Name : app
 * Desc : 
 */
abstract class BaseViewModel: ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(RetrofitClient)
        .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is MovieListViewModel -> injector.inject(this)
        }
    }
}