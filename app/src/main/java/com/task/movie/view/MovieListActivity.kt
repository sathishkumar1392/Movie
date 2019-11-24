package com.task.movie.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
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
 * File Name : MovieListActivity.kt
 * ClassName :
 * Module Name : app
 * Desc :
 */

class MovieListActivity : AppCompatActivity() {
    lateinit var getActivityContext: Context
    private lateinit var binding: ActivityMovielistBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: MovieListViewModel
    private var errorSnackBar: Snackbar? = null
    private var userScrolled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityContext = this@MovieListActivity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movielist)
        binding.viewModel = MovieListViewModel()
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcyVwMovieList.layoutManager = linearLayoutManager

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        viewModel.loadMovieList(getActivityContext)
        binding.rcyVwMovieList.adapter = viewModel.movieListAdapter
        viewModel.loadingVisibility.observe(this, Observer {
            binding.progress.visibility = it
        })
        viewModel.errorMessage.observe(
            this,
            Observer { errorMessage -> if (errorMessage != null) showError(errorMessage) else hideError() })
        loadMore()
        initSearchView()
        observerViewModel()
        getNetworkStatusFromViewModel()
    }

    private fun getNetworkStatusFromViewModel() {
        viewModel.updateNetworkStatusToView().observe(this,object :Observer<Boolean>{
            override fun onChanged(t: Boolean?) {
                val networkStatus = t
                if (networkStatus!!) checkNetworkConnectivity()
            }
        })
    }

    private fun observerViewModel() {
        viewModel.updateUserClickedItemToView()!!.observe(this, object : Observer<Result> {
            override fun onChanged(t: Result?) {
                if (t.toString().isNotEmpty()) {
                    val intent = Intent(getActivityContext, MovieDetailsPageActivity::class.java)
                    intent.putExtra(Constants.TAG_MovieName, t!!.title)
                    intent.putExtra(Constants.TAG_DESC, t.overview)
                    intent.putExtra(Constants.TAG_IMAGE, t.posterPath)
                    intent.putExtra(Constants.TAG_RATINGBAR, t.voteAverage)
                    startActivity(intent)
                }
            }
        })
    }



    private fun initSearchView(): Boolean {
        binding.searchViewMovies.isActivated = true
        binding.searchViewMovies.queryHint = getString(R.string.search_MovieName)
        binding.searchViewMovies.onActionViewExpanded()
        binding.searchViewMovies.isIconified = false
        binding.searchViewMovies.clearFocus()

        val closeBtnId: Int = binding.searchViewMovies.context.resources
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

        closeButton.setOnClickListener {
            binding.searchViewMovies.setQuery("", false)
            binding.searchViewMovies.clearFocus()
            viewModel.refreshMovieList()
        }



        binding.searchViewMovies.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.searchViewMovies.clearFocus()
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
                }else if (linearLayoutManager.findFirstCompletelyVisibleItemPosition()==0){
                    reset()

                }
            }
        })
    }

    private fun reset() {
        Constants.PAGE_NUMBER =1
    }


    private fun loadMore(userScroll: Boolean) {
        viewModel.loadMore(userScroll)
    }



    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
        if (errorMessage ==(R.string.noResultFound)){
            errorSnackBar?.setText(R.string.noResultFound)
        }else{
            errorSnackBar?.setText(R.string.noItemFound)
        }
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }



    // Checking NetworkState
    private fun checkNetworkConnectivity() {

        val builder: AlertDialog.Builder? =
            AlertDialog.Builder(getActivityContext, R.style.DialogTheme)
        builder!!.setTitle(R.string.tittle_network_error)
        builder.setMessage(R.string.msg_error_state)
        try {
            builder.setNegativeButton(
                R.string.retry,
                DialogInterface.OnClickListener { dialog, which ->
                    viewModel.loadMovieList(getActivityContext)
                    dialog.dismiss()
                    return@OnClickListener
                })
            val dialog: AlertDialog? =
                builder.setPositiveButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog1, which ->
                        finish()
                        return@OnClickListener
                    }).create()
            dialog!!.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.setOnShowListener { dialogInterface ->
                val positive =
                    (dialogInterface as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    positive.setTextColor(getColor(R.color.dialog_text_color))
                }
                val negative = dialogInterface.getButton(AlertDialog.BUTTON_NEGATIVE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    negative.setTextColor(getColor(R.color.dialog_text_color))
                }
            }
            dialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
        }
    }
}


