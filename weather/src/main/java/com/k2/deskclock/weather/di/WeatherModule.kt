package com.k2.deskclock.weather.di

import com.k2.deskclock.location.AddressProvider
import com.k2.deskclock.weather.Constants.API_ENDPOINT
import com.k2.deskclock.weather.WeatherProvider
import com.k2.deskclock.weather.WeatherProviderImpl
import com.k2.deskclock.weather.data.local.WeatherDaoProvider
import com.k2.deskclock.weather.data.local.WeatherLocalDataSource
import com.k2.deskclock.weather.data.local.WeatherLocalDataSourceImpl
import com.k2.deskclock.weather.data.models.AddressCacheDao
import com.k2.deskclock.weather.data.models.WeatherCacheDao
import com.k2.deskclock.weather.data.remote.WeatherApiInterface
import com.k2.deskclock.weather.data.remote.WeatherRemoteDataSource
import com.k2.deskclock.weather.data.remote.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Provides
    fun provideWeatherProvider(
        remoteDataSource: WeatherRemoteDataSource,
        localDataSource: WeatherLocalDataSource,
        addressProvider: AddressProvider
    ): WeatherProvider {
        return WeatherProviderImpl(remoteDataSource, localDataSource, addressProvider)
    }

    @Provides
    fun provideRemoteDataSource(weatherApi: WeatherApiInterface): WeatherRemoteDataSource {
        return WeatherRemoteDataSourceImpl(weatherApi)
    }

    @Provides
    @DelicateCoroutinesApi
    fun provideLocalDataSource(
        weatherCacheDao: WeatherCacheDao,
        addressCacheDao: AddressCacheDao
    ): WeatherLocalDataSource {
        return WeatherLocalDataSourceImpl(weatherCacheDao, addressCacheDao)
    }

    @Provides
    fun provideWeatherApi(@WeatherRetrofit retrofit: Retrofit): WeatherApiInterface {
        return retrofit.create(WeatherApiInterface::class.java)
    }

    @Provides
    fun provideWeatherDao(weatherDaoProvider: WeatherDaoProvider): WeatherCacheDao {
        return weatherDaoProvider.weatherCacheDao()
    }

    @Provides
    fun provideAddressDao(weatherDaoProvider: WeatherDaoProvider): AddressCacheDao {
        return weatherDaoProvider.addressCacheDao()
    }

    @Provides
    @WeatherRetrofit
    fun provideRetrofit(
        @Named(API_ENDPOINT) endPoint: String,
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(endPoint)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Named(API_ENDPOINT)
    fun provideApiEndpoint(): String {
        return "https://api.open-meteo.com/"
    }

}
