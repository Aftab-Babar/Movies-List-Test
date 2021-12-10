package com.movies.list.di.modules

import com.squareup.moshi.Moshi
import com.movies.list.data.remote.ApiInterface
import com.movies.list.utils.calladapter.flow.FlowResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRavenApi(moshi: Moshi): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/theapache64/top250/master/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
            .create(ApiInterface::class.java)
    }

}