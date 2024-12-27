package com.ma.tehro.common

import com.ma.tehro.data.Station
import kotlinx.serialization.Serializable

@Serializable
object LinesScreen

@Serializable
data class StationsScreen(val lineNumber: Int)

@Serializable
object StationSelectorScreen

@Serializable
data class PathFinderScreen(val startStation: String, val destination: String)

@Serializable
data class StationDetailScreen(val station: Station, val lineNumber: Int)