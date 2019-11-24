package com.task.movie.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.movie.BuildConfig
import com.task.movie.R
import com.task.movie.model.MovieApiResponseModel
import com.task.movie.model.Result
import com.task.movie.remote.repository.RetrofitInterface
import com.task.movie.utilis.Constants
import com.task.movie.utilis.NetworkConnectivity
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
 * File Name : MovieListViewModel.kt
 * Module Name : app
 * Desc : MovieListViewModel  Communicate data from view to viewModel with Livedata
 * Sync data from server.
 */

class MovieListViewModel : BaseViewModel() {


    @Inject
    lateinit var movieListApi: RetrofitInterface
    val movieListAdapter: MovieAdapter = MovieAdapter()
    lateinit var result: MutableList<Result>
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    private lateinit var subscription: Disposable
    private var totalPageNumber: Int = 0
    private var userClickedItemObservable = MutableLiveData<Result>()
    private var networkStatus = MutableLiveData<Boolean>()
    private lateinit var getContext: Context


    init {
        onItemClick()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    internal fun loadMovieList(context: Context) {
        getContext = context
        if (NetworkConnectivity.isNetworkAvailable(getContext)) {
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

        } else {
            networkStatus.value = true
        }
    }


    internal fun loadMore(userScrolled: Boolean) {
        if (NetworkConnectivity.isNetworkAvailable(getContext)) {
            if (totalPageNumber != Constants.PAGE_NUMBER) {
                if (userScrolled){
                    Constants.PAGE_NUMBER = Constants.PAGE_NUMBER + 1
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
                }else{
                    networkStatus.value = true
                }
            } else {
                errorMessage.value = R.string.noResultFound
            }
            }
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
            if (request!!.totalResults>0) {
                totalPageNumber = request.totalPages
                result = request.results as MutableList
                movieListAdapter.updateMovieList(result)
            }else{
                errorMessage.value = R.string.noResultFound
            }
        }
    }

    private fun onRetrievePostListError(e: Throwable) {
        errorMessage.value = e.message!!.toInt()
    }


    internal fun updateUserClickedItemToView(): LiveData<Result>? {
        return userClickedItemObservable
    }

    internal fun searchMovieName(searchString: String) {
        if (NetworkConnectivity.isNetworkAvailable(getContext)) {
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
        }else{
            networkStatus.value = true
        }
    }

    private fun onItemClick() {
        subscription = movieListAdapter.clickEvent.subscribe {
            userClickedItemObservable.value = it
        }
    }

    internal fun refreshMovieList() {
        result.clear()
        Constants.PAGE_NUMBER = 1
        loadMovieList(getContext)
    }

    internal fun updateNetworkStatusToView(): LiveData<Boolean> {
        return networkStatus
    }

}