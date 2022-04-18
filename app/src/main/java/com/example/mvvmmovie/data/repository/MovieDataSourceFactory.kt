package com.example.mvvmmovie.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.mvvmmovie.data.api.MovieDBInterface
import com.example.mvvmmovie.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : androidx.paging.DataSource.Factory<Int, Movie>() {

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): androidx.paging.DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        movieLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}