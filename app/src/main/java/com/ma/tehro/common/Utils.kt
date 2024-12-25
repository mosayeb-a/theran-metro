package com.ma.tehro.common

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.CoroutineDispatcher


data class AppCoroutineDispatchers(
    val io: CoroutineDispatcher,
    val databaseWrite: CoroutineDispatcher,
    val databaseRead: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)
fun Painter.toImageBitmap(
    size: Size,
    density: Density,
    layoutDirection: LayoutDirection,
): ImageBitmap {
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)
    CanvasDrawScope().draw(density, layoutDirection, canvas, size) {
        draw(size)
    }
    return bitmap
}

fun getLineEndpoints(): Map<Int, Pair<String, String>> {
    return mapOf(
        1 to Pair("Tajrish", "Kahrizak"),
        2 to Pair("Farhangsara", "Sadeghiyeh"),
        3 to Pair("Qa'em", "Azadegan"),
        4 to Pair("Kolahdooz", "Allameh Jafari"),
        5 to Pair("Sadeghiyeh", "Qasem Soleimani"),
        6 to Pair("Haram-e Abdol Azim", "Kouhsar"),
        7 to Pair("Varzeshgah-e Takhti", "Meydan-e Ketab")
    )
}

fun getLineColor(lineNumber: Int): Color {
    return when (lineNumber) {
        1 -> Color(android.graphics.Color.parseColor("#E0001F"))
        2 -> Color(android.graphics.Color.parseColor("#2F4389"))
        3 -> Color(android.graphics.Color.parseColor("#67C5F5"))
        4 -> Color(android.graphics.Color.parseColor("#DACC4A"))
        5 -> Color(android.graphics.Color.parseColor("#007E46"))
        6 -> Color(android.graphics.Color.parseColor("#EF639F"))
        7 -> Color(android.graphics.Color.parseColor("#7F0B74"))
        else -> Color.Gray
    }
}