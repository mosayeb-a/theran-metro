package com.ma.tehro

import com.ma.tehro.common.readJsonStationsAsText
import com.ma.tehro.data.Station
import com.ma.tehro.scripts.getOrderedStationsByLine
import com.ma.tehro.ui.line.LineViewModel
import org.junit.Test
import kotlin.test.assertEquals

class JsonStationItemFileTest {

    private val stations: Map<String, Station> = readJsonStationsAsText("stations_updated")

    @Test
    fun `check the order of stations is correct by position in line`() {
        val lines = stations.values.flatMap { it.lines }.distinct()
        for (line in lines) {
            val expectedOrder = getOrderedStationsByLine(line, stations)
            val actualOrder = LineViewModel(stations).getOrderedStationsInLineByPosition(line)

            assertEquals(
                expectedOrder,
                actualOrder,
                "The order of stations for line $line is incorrect."
            )
        }

    }
}