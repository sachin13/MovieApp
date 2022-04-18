package com.example.mvvmmovie.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvmmovie.data.api.MovieDBInterface
import com.example.mvvmmovie.data.api.POST_PER_PAGE
import com.example.mvvmmovie.data.model.Movie
import com.example.mvvmmovie.data.repository.MovieDataSource
import com.example.mvvmmovie.data.repository.MovieDataSourceFactory
import com.example.mvvmmovie.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService: MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder() //Page Configuration
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState
        )
    }
}