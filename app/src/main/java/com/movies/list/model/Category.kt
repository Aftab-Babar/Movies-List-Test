package com.movies.list.model

import com.movies.list.data.remote.Movie


data class Category(
    val id: Long,
    val genre: String,
    val movies: List<Movie>
)
