package com.task.movie.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.task.movie.R
import com.task.movie.databinding.ActivityMovielistBinding
import com.task.movie.model.Result
import com.task.movie.utilis.Constants
import com.task.movie.viewmodel.MovieListViewModel

/*
 * Project Name : Movie
 * Created by : SATHISH KUMAR R
 * Created on : 18-11-2019 10:29
 * Updated on :
 * File Name : CommonUtilis.kt
 * ClassName :
 * Module Name : app
 * Desc :
 */

 class MovieListActivity : AppCompatActivity() {
    lateinit var getActivityContext: Context
    private lateinit var binding: ActivityMovielistBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: MovieListViewModel
    private var errorSnackbar: Snackbar? = null
    private var userScrolled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityContext = this@MovieListActivity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movielist)
        binding.viewModel = MovieListViewModel()
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcyVwMovieList.layoutManager = linearLayoutManager


        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        binding.rcyVwMovieList.adapter = viewModel.movieListAdapter
        viewModel.errorMessage.observe(
            this,
            Observer { errorMessage -> if (errorMessage != null) showError(errorMessage) else hideError() })
        loadMore()
        initSearchView()
        observerViewModel()
    }

  private  fun observerViewModel() {
        viewModel.getUserClickedItemObservable()!!.observe(this, object : Observer<Result> {
            override fun onChanged(t: Result?) {
                if(t.toString().isNotEmpty()){
                    val intent = Intent(getActivityContext, MovieDetailsPageActivity::class.java)
                    intent.putExtra(Constants.TAG_MovieName,t!!.title)
                    intent.putExtra(Constants.TAG_Desc,t.overview)
                    intent.putExtra(Constants.TAG_IMAGEURL,t.posterPath)
                    intent.putExtra(Constants.TAG_RatingBar,t.voteAverage)
                    startActivity(intent)
                }


            }

        })

    }


    private fun initSearchView(): Boolean {
        binding.searchViewMovies.isActivated = true
        binding.searchViewMovies.queryHint = "Search Movie Name"
        binding.searchViewMovies.onActionViewExpanded()
        binding.searchViewMovies.isIconified = false
        binding.searchViewMovies.clearFocus()

        val closeBtnId: Int = binding.searchViewMovies.getContext().getResources()
            .getIdentifier("android:id/search_close_btn", null, null)

        val closeButton: ImageView = binding.searchViewMovies.findViewById(closeBtnId) as ImageView

        binding.searchViewMovies.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.length >= 3) {
                    binding.searchViewMovies.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.length >= 3) {
                    viewModel.searchMovieName(newText)
                } else if (newText.isEmpty()) {
                    binding.searchViewMovies.clearFocus()
                }
                return true
            }

        })

        closeButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.searchViewMovies.setQuery("", false)
                binding.searchViewMovies.clearFocus()
                viewModel.loadInitalMovieList()
            }

        })



        binding.searchViewMovies.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.searchViewMovies.clearFocus()
                Toast.makeText(getActivityContext, "TextCleared", Toast.LENGTH_SHORT).show()
                return false
            }
        })
        return true
    }


    private fun loadMore() {

        binding.rcyVwMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if ((lastVisibleItemPosition + 1) >= totalItemCount) {
                    userScrolled = true
                    loadMore(userScrolled)
                    userScrolled = false
                }
            }
        })


    }


    private fun loadMore(userScroll: Boolean) {
        viewModel.loadMore(userScroll)

    }


    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.str_retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }


}


