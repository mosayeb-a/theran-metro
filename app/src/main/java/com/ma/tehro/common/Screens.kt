package com.ma.tehro.common

import kotlinx.serialization.Serializable

@Serializable
object LinesScreen

@Serializable
data class StationsScreen(val lineNumber: Int)

@Serializable
object StationSelectorScreen

@Serializable
data class PathFinderScreen(val startStation: String, val destination: String)
