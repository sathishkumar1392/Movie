package com.task.movie.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.movie.BuildConfig
import com.task.movie.model.MovieApiResponseModel
import com.task.movie.model.Result
import com.task.movie.remote.repository.RetrofitInterface
import com.task.movie.utilis.Constants
import com.task.movie.view.adapter.MovieAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject


/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on :17-11-2019 14:37
 * File Name : MovieListViewModel.kt ClassName : MovieListViewModel
 * Module Name : app
 * Desc :  This MovieListViewModel  class receives data from server and Update to UI part.
 */

class MovieListViewModel() : BaseViewModel() {


    @Inject
    lateinit var movieListApi: RetrofitInterface
    val movieListAdapter: MovieAdapter = MovieAdapter()
    lateinit var result: MutableList<Result>
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { }
    private lateinit var subscription: Disposable
    private var totalPageNumber: Int = 0
    private var userClickedItemObservable = MutableLiveData<Result>()

    init {
        loadMovieList()
    }


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }


    private fun loadMovieList() {
        subscription = movieListApi.getMovieList(
            BuildConfig.API_KEY,
            Constants.TAG_LANGUAGE,
            Constants.PAGE_NUMBER
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveMovieListStart() }
            .doOnTerminate { onRetrieveMovieListFinish() }
            .subscribeWith(object : DisposableObserver<Response<MovieApiResponseModel>>() {
                override fun onComplete() {

                }

                override fun onNext(t: Response<MovieApiResponseModel>) {
                    onRetrievePostListSuccess(t)
                }

                override fun onError(e: Throwable) {
                    onRetrievePostListError(e)
                }

            })
        OnItemClick()
    }


    internal fun loadMore(userScrolled: Boolean) {
        if (totalPageNumber == Constants.PAGE_NUMBER) {
            Constants.PAGE_NUMBER = 0
        }
        if (userScrolled) {
            Constants.PAGE_NUMBER = Constants.PAGE_NUMBER + 1
        }
        subscription = movieListApi.getMovieList(
            BuildConfig.API_KEY,
            Constants.TAG_LANGUAGE, Constants.PAGE_NUMBER
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveMovieListStart() }
            .doOnTerminate { onRetrieveMovieListFinish() }
            .subscribeWith(object : DisposableObserver<Response<MovieApiResponseModel>>() {
                override fun onComplete() {

                }

                override fun onNext(t: Response<MovieApiResponseModel>) {
                    onUpdateList(t)
                }

                override fun onError(e: Throwable) {
                    onRetrievePostListError(e)
                }

            })
    }

    private fun onUpdateList(res: Response<MovieApiResponseModel>) {
        if (res.isSuccessful) {
            val request = res.body()
            val value = result.size
            result.addAll(value, request!!.results)
            movieListAdapter.updateMovieList(result)
        }
    }

    private fun onRetrieveMovieListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveMovieListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(response: Response<MovieApiResponseModel>) {
        if (response.isSuccessful) {
            val request = response.body()
            totalPageNumber = request!!.totalPages
            result = request.results as MutableList
            movieListAdapter.updateMovieList(result)
        }

    }

    private fun onRetrievePostListError(e: Throwable) {
        errorMessage.value = e.message!!.toInt()
    }


    fun getUserClickedItemObservable(): LiveData<Result>? {
        return userClickedItemObservable
    }

    internal fun searchMovieName(searchString: String) {
        result.clear()
        subscription = movieListApi.searchMovieName(
            BuildConfig.API_KEY,
            Constants.TAG_LANGUAGE, searchString, Constants.PAGE_NUMBER
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveMovieListStart() }
            .doOnTerminate { onRetrieveMovieListFinish() }
            .subscribeWith(object : DisposableObserver<Response<MovieApiResponseModel>>() {
                override fun onComplete() {

                }

                override fun onNext(t: Response<MovieApiResponseModel>) {
                    onRetrievePostListSuccess(t)
                }

                override fun onError(e: Throwable) {
                    onRetrievePostListError(e)
                }

            })
    }

    internal fun OnItemClick() {
        subscription = movieListAdapter.clickEvent.subscribe { it ->
            userClickedItemObservable.value = it
        }
    }

    internal fun loadInitalMovieList() {
        loadingVisibility.value = View.VISIBLE

        loadMovieList()
    }


}