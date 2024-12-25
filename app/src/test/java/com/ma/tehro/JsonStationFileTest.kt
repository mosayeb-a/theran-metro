package com.ma.tehro

import com.ma.tehro.data.StationData
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.Assert.*
import java.io.File

class JsonStationFileTest {
    private val filePath = ""
    private val stationsJson = File(filePath).readText(Charsets.UTF_8)
    private val stations: Map<String, StationData> = Json.decodeFromString(stationsJson)

    private val newFile = ""
    private val newStationsJson = File(newFile).readText(Charsets.UTF_8)
    private val newStations: Map<String, StationData> = Json.decodeFromString(newStationsJson)

    @Test
    fun testAreStationCountsEqual() {
        assertTrue("Station counts should be equal", areStationCountsEqual())
    }

    @Test
    fun testAreStationCountsEqualPerLine() {
        assertTrue("Station counts per line should be equal", areStationCountsEqualPerLine())
    }

    @Test
    fun testAreLinesInSameOrder() {
        assertTrue("Lines should be in the same order", areLinesInSameOrder())
    }

    private fun areStationCountsEqual(): Boolean {
        return stations.size == newStations.size
    }

    private fun areStationCountsEqualPerLine(): Boolean {
        val stationsByLines = stations.values.groupBy { it.property.lines }
        val newStationsByLines = newStations.values.groupBy { it.property.lines }
        return stationsByLines.keys == newStationsByLines.keys &&
                stationsByLines.all { (line, stations) ->
                    stations.size == newStationsByLines[line]?.size
                }
    }

    private fun areLinesInSameOrder(): Boolean {
        val lines = stations.values.flatMap { it.property.lines }.distinct().sorted()
        val newLines = newStations.values.flatMap { it.property.lines }.distinct().sorted()
        if (lines != newLines) return false

        for (line in lines) {
            val orderedStations = getOrderedStations(line, stations)
            val newOrderedStations = getOrderedStations(line, newStations)
            if (orderedStations != newOrderedStations) return false
        }
        return true
    }

    private fun getOrderedStations(line: Int, stations: Map<String, StationData>): List<String> {
        val visited = mutableSetOf<String>()
        val orderedStations = mutableListOf<String>()

        val startStation = stations.entries.find { entry ->
            entry.value.property.lines.contains(line) &&
                    entry.value.relations.count { it.lines.contains(line) } == 1
        }?.key ?: return emptyList()

        fun dfs(stationName: String) {
            if (stationName in visited) return
            visited.add(stationName)
            orderedStations.add(stationName)

            val currentStation = stations[stationName] ?: return
            currentStation.relations
                .filter { it.lines.contains(line) && it.name !in visited }
                .forEach { dfs(it.name) }
        }

        dfs(startStation)
        return orderedStations
    }
}