package com.movies.list.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.movies.list.data.local.daos.MoviesDao
import com.movies.list.data.remote.ApiInterface
import com.movies.list.data.remote.Movie
import com.movies.list.utils.NetworkBoundResource
import com.movies.list.utils.calladapter.flow.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class MoviesRepo @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val apiInterface: ApiInterface,
    private val moviesDao: MoviesDao
) {

    companion object {
        private val MOVIE_EXPIRY_IN_MILLIS = Duration.hours(1).inWholeMilliseconds
        private const val KEY_LAST_SYNCED = "last_synced"
    }

    fun getTop250Movies(): Flow<List<Movie>> {
        return object : NetworkBoundResource<List<Movie>, List<Movie>>() {

            override fun fetchFromLocal(): Flow<List<Movie>> {
                return moviesDao.getAllMovies()
            }

            override fun fetchFromRemote(): Flow<Resource<List<Movie>>> {
                return apiInterface.getTop250Movies()
            }

            override fun saveRemoteData(data: List<Movie>) {
                moviesDao.nukeTableAndAdd(data)
                sharedPref.edit {
                    putLong(KEY_LAST_SYNCED, System.currentTimeMillis())
                }
            }

            override fun shouldFetchFromRemote(data: List<Movie>): Boolean {
                val lastSynced = sharedPref.getLong(KEY_LAST_SYNCED, -1)
                return lastSynced == -1L ||
                        data.isNullOrEmpty() ||
                        isExpired(lastSynced)
            }

        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun getMovie(movieId: Int) = moviesDao.getMovie(movieId)

    @ExperimentalTime
    private fun isExpired(lastSynced: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastSynced) >= MOVIE_EXPIRY_IN_MILLIS
    }
}