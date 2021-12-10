package com.movies.list.data.remote

import com.movies.list.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ApiInterface {

    @GET("top250_min.json")
    fun getTop250Movies(): Flow<Resource<List<Movie>>>
}