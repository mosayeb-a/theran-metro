package com.ma.tehro.data

import kotlinx.serialization.Serializable

@Serializable
data class Station(
    val name: String,
    val fa: String,
    val lines: List<Int>,
    val positionsInLine: List<PositionInLine> = emptyList(),
    val longitude: String ? = null,
    val latitude: String ? = null,
    val address: String ? = null,
    val colors: List<String> = emptyList(),
    val disabled: Boolean,
    val wc: Boolean? = null,
    val coffeeShop: Boolean ? = null,
    val groceryStore: Boolean ? = null,
    val fastFood: Boolean ? = null,
    val atm: Boolean ? = null,
    val relations: List<String> = emptyList()
)

@Serializable
data class PositionInLine(
    val position: Int,
    val line: Int
)
