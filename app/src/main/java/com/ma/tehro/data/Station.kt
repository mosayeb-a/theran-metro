package com.ma.tehro.data

import kotlinx.serialization.Serializable


@Serializable
data class StationData(
    val property: StationProperty,
    val relations: List<StationRelation>
)

@Serializable
data class StationProperty(
    val name: String,
    var positionsInLine: List<PositionInLine> = emptyList(),
    val fa: String,
    var colors: List<String>,
    val lines: List<Int>,
    val disabled: Boolean,
)

@Serializable
data class StationRelation(
    val name: String,
    val fa: String,
    var colors: List<String>,
    val lines: List<Int>,
    val disabled: Boolean
)

@Serializable
data class PositionInLine(
    val position: Int,
    val line: Int
)
