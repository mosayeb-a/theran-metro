package com.ma.tehro.di

import android.app.Application
import com.ma.tehro.R
import com.ma.tehro.data.StationData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideStations(context: Application): Map<String, StationData> {
        return try {
            val stationsJson = context.resources.openRawResource(R.raw.station2)
                .bufferedReader()
                .use { it.readText() }

            Json.decodeFromString(stationsJson)
        } catch (e: Exception) {
            android.util.Log.e("AppModule", "Failed to load station data: ${e.message}", e)
            return emptyMap()
        }
    }
}
