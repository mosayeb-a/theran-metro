package com.ma.tehro.ui.line

import androidx.lifecycle.ViewModel
import com.ma.tehro.data.StationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LineViewModel @Inject constructor(
    private val stations: Map<String, StationData>
) : ViewModel() {

    private val TAG = "MainViewModel"

    fun getLines(): List<Int> {
        val lines = stations.values
            .flatMap { it.property.lines }
            .distinct()
            .sorted()
        println("$TAG getLines -> $lines")
        return lines
    }

    fun getOrderedStationsByLine(line: Int): List<StationData> {
        if (stations.isEmpty()) return emptyList()

        val visited = mutableSetOf<String>()
        val orderedStations = mutableListOf<StationData>()

        val startStation = stations.entries.find { entry ->
            entry.value.property.lines.contains(line) &&
                    entry.value.relations.count { it.lines.contains(line) } == 1
        }?.key ?: return emptyList()

        fun dfs(stationName: String) {
            if (stationName in visited) return
            visited.add(stationName)
            stations[stationName]?.let { station ->
                orderedStations.add(station)
                station.relations
                    .filter { it.lines.contains(line) && it.name !in visited }
                    .forEach { dfs(it.name) }
            }
        }

        dfs(startStation)
        return orderedStations
    }
}