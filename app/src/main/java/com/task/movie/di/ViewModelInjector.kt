package com.task.movie.di

import com.task.movie.remote.RetrofitClient
import com.task.movie.viewmodel.MovieListViewModel
import dagger.Component
import javax.inject.Singleton

/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :18-11-2019 17:51
 * Updated on : 
 * File Name : ViewModelInjector.kt
 * ClassName : ViewModelInjector
 * Module Name : app
 * Desc : Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(RetrofitClient::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified MovieListViewModel.
     *  which to inject the dependencies
     */
    fun inject(movieListViewModel:MovieListViewModel)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: RetrofitClient): Builder

    }


}