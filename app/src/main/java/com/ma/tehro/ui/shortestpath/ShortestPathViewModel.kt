package com.ma.tehro.ui.shortestpath

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.ma.tehro.common.getLineEndpoints
import com.ma.tehro.data.StationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

@Immutable
sealed class PathItem {
    data class Title(val text: String) : PathItem()
    data class Station(val data: StationData) : PathItem()
}

@Immutable
data class PathUiState(
    val isLoading: Boolean = false,
    val selectedStartStation: String = "",
    val selectedDestStation: String = "",
)

private const val TAG = "ShortestPathViewModel"

@HiltViewModel
class ShortestPathViewModel @Inject constructor(
    val stations: Map<String, StationData>,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PathUiState())
    val uiState: StateFlow<PathUiState> get() = _uiState

    fun onSelectedChange(isFrom: Boolean, station: String) {
        _uiState.value = _uiState.value.copy(
            selectedStartStation = if (isFrom) station else _uiState.value.selectedStartStation,
            selectedDestStation = if (!isFrom) station else _uiState.value.selectedDestStation
        )
    }


    fun isIndexInTheRange(index: Int, lineAndTitlePosition: List<Pair<Int, Pair<Int, Int>>>): Int? {
        return lineAndTitlePosition.firstOrNull { (_, range) ->
            index in range.first..range.second
        }?.first
    }


    private fun getLineByPath(path: List<PathItem>): List<Pair<Int, Pair<Int, Int>>> {
        val lines = mutableListOf<Pair<Int, Pair<Int, Int>>>()
        var currentLine: Int? = null
        var segmentStartIndex = 0

        path.forEachIndexed { index, item ->
            if (item is PathItem.Title) {
                val lineNumber = item.text.substring(5, 6).toInt()

                if (currentLine != null && lineNumber != currentLine) {
                    lines.add(currentLine!! to (segmentStartIndex to index - 1))
                }

                currentLine = lineNumber
                segmentStartIndex = index
            }
        }

        if (currentLine != null) {
            lines.add(currentLine!! to (segmentStartIndex to path.lastIndex))
        }

        return lines
    }

    private val lineAndTitlePositionCache =
        mutableMapOf<List<PathItem>, List<Pair<Int, Pair<Int, Int>>>>()

    fun getCachedLineByPath(path: List<PathItem>): List<Pair<Int, Pair<Int, Int>>> {
        return lineAndTitlePositionCache.getOrPut(path) {
            getLineByPath(path)
        }
    }

    private val pathCache = mutableMapOf<Pair<String, String>, List<PathItem>>()

    fun findShortestPathWithDirectionCache(from: String, to: String): List<PathItem> {
        val key = Pair(from, to)


        if (pathCache.containsKey(key)) {
            return pathCache[key]!!
        }


        val path = findShortestPathWithDirection(from, to)
        pathCache[key] = path
        return path
    }

    fun findShortestPathWithDirection(from: String, to: String): List<PathItem> {
        val shortestPath = findShortestPath(stations, from, to)
        if (shortestPath.isEmpty()) return emptyList()

        val directions = mutableListOf<PathItem>()
        var currentLine: Int? = null
        var previousStation: StationData? = null

        val firstStation = stations[shortestPath.first()] ?: return emptyList()
        directions.add(PathItem.Station(firstStation))

        for (i in 0 until shortestPath.size - 1) {
            val currentStationName = shortestPath[i]
            val nextStationName = shortestPath[i + 1]

            val currentStation = stations[currentStationName] ?: continue
            val nextStation = stations[nextStationName] ?: continue

            val currentLinePosition =
                currentStation.property.positionsInLine.firstOrNull { pos ->
                    nextStation.property.positionsInLine.any { it.line == pos.line }
                } ?: continue

            val nextLinePosition =
                nextStation.property.positionsInLine.first { it.line == currentLinePosition.line }

            if (currentLine != currentLinePosition.line) {
                currentLine = currentLinePosition.line
                val endpoints = getLineEndpoints()[currentLine] ?: continue
                val direction = if (currentLinePosition.position < nextLinePosition.position) {
                    "Line $currentLine: To ${endpoints.second}"
                } else {
                    "Line $currentLine: To ${endpoints.first}"
                }
                directions.add(PathItem.Title(direction))

                if (previousStation != null) {
                    directions.add(PathItem.Station(currentStation))
                }
            }

            if (previousStation == null || previousStation.property.name != nextStation.property.name) {
                directions.add(PathItem.Station(nextStation))
            }

            previousStation = nextStation
        }

        if (directions.size > 1) {
            directions.swap(0, 1)
        }

        return directions
    }

    private fun findShortestPath(
        stations: Map<String, StationData>,
        from: String,
        to: String
    ): List<String> {
        val visited = mutableSetOf<String>()
        val queue: Queue<List<String>> = LinkedList()
        queue.add(listOf(from))

        while (queue.isNotEmpty()) {
            val path = queue.poll()
            val current = path.last()

            if (current == to) return path
            if (visited.contains(current)) continue

            visited.add(current)
            val station = stations[current] ?: continue
            station.relations.filter { !it.disabled }.forEach { relation ->
                queue.add(path + relation.name)
            }
        }
        return emptyList()
    }

    private fun MutableList<PathItem>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}
