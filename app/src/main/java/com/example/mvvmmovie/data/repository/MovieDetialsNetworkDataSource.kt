package com.example.mvvmmovie.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmmovie.data.api.MovieDBInterface
import com.example.mvvmmovie.data.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MovieDetialsNetworkDataSource(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>() //private
    val networkState: LiveData<NetworkState> //public
        get() = _networkState

    private val _downloadedMovieResponse = MutableLiveData<MovieDetails>() //private
    val downloadedMovieResponse: LiveData<MovieDetails> //public
        get() = _downloadedMovieResponse

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {

            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message.toString())
                        }
                    )
            )

        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message.toString())
        }
    }
}