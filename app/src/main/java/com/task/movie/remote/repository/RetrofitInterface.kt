package com.task.movie.remote.repository

import com.task.movie.model.MovieApiResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Project Name : MovieApp
 * Created by : SATHISH KUMAR R
 * Created on :17-11-2019 12:31
 * File Name : RetrofitInterface.kt
 * ClassName : RetrofitInterface
 * Module Name : app
 * Desc : This interface class  contain request api from server
 */
interface RetrofitInterface {

   @GET ("movie/now_playing?")
   fun getMovieList(@Query("api_key")mApi_key:String,@Query("language")mLanguage:String,
    @Query("page")mPage:Int) : Observable<Response<MovieApiResponseModel>>

    @GET ("search/movie?")
    fun searchMovieName(@Query("api_key")mApi_key:String,
                        @Query("language")mLanguage:String,
                        @Query("query")mquery:String,@Query("page")mPage:Int): Observable<Response<MovieApiResponseModel>>



}