package com.ma.tehro.di

import android.app.Application
import com.ma.tehro.R
import com.ma.tehro.data.Station
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideStations(context: Application): Map<String, Station> {
            val stationsJson = context.resources.openRawResource(R.raw.stations_updated)
                .bufferedReader()
                .use { it.readText() }

           return Json.decodeFromString(stationsJson)
    }
}
