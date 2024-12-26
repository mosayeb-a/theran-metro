package com.ma.tehro

import com.ma.tehro.common.readJsonStationsAsText
import com.ma.tehro.data.Station
import com.ma.tehro.scripts.getOrderedStationsByLine
import org.junit.Test
import kotlin.test.assertEquals

class DifferenceJsonStationItemFilesTest {
    private val stations: Map<String, Station> = readJsonStationsAsText("stations")
    private val newStations: Map<String, Station> = readJsonStationsAsText("stations_updated")

    @Test
    fun `is the sizes are the same`() {
        val stationSize = stations.size
        val newStationSizeSize = newStations.size
        assertEquals(stationSize, newStationSizeSize)
    }

    @Test
    fun `check station order for each line`() {
        val lines = stations.values.flatMap { it.lines }.distinct()

        for (line in lines) {
            val oldOrder = getOrderedStationsByLine(line, newStations)
            val newOrder  = getOrderedStationsByLine(line, newStations)

            assertEquals(oldOrder, newOrder, "Order mismatch for line: $line")
        }
    }

}