package com.ma.tehro.ui.line

import androidx.lifecycle.ViewModel
import com.ma.tehro.data.Station
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LineViewModel @Inject constructor(
    private val stations: Map<String, Station>
) : ViewModel() {

    // todo get it in init and expose it as a state to ui
    fun getLines(): List<Int> {
//        val lines = stations.values
//            .flatMap { it.lines }
//            .sorted()

        return listOf(1, 2, 3, 4, 5, 6, 7)
    }

    // TODO create seprate viewmodel for stations or ..
    fun getOrderedStationsInLineByPosition(
        line: Int,
    ): List<Station> {
        return stations.values
            .filter { it.lines.contains(line) }
            .sortedBy { station ->
                station.positionsInLine.find { it.line == line }?.position
            }
    }
}