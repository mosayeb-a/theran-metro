package com.ma.tehro.common

import android.net.Uri
import android.os.Bundle
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import com.ma.tehro.data.Station
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


data class AppCoroutineDispatchers(
    val io: CoroutineDispatcher,
    val databaseWrite: CoroutineDispatcher,
    val databaseRead: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)

inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, json.encodeToString(value))
}

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

fun getLineColorByNumber(lineNumber: Int): Color {
    return when (lineNumber) {
        1 -> Color(android.graphics.Color.parseColor(COLOR_LINE_1))
        2 -> Color(android.graphics.Color.parseColor(COLOR_LINE_2))
        3 -> Color(android.graphics.Color.parseColor(COLOR_LINE_3))
        4 -> Color(android.graphics.Color.parseColor(COLOR_LINE_4))
        5 -> Color(android.graphics.Color.parseColor(COLOR_LINE_5))
        6 -> Color(android.graphics.Color.parseColor(COLOR_LINE_6))
        7 -> Color(android.graphics.Color.parseColor(COLOR_LINE_7))
        else -> Color.Gray
    }
}

fun getLineNumberByColor(color: Color): Int {
    return when (color) {
        Color(android.graphics.Color.parseColor(COLOR_LINE_1)) -> 1
        Color(android.graphics.Color.parseColor(COLOR_LINE_2)) -> 2
        Color(android.graphics.Color.parseColor(COLOR_LINE_3)) -> 3
        Color(android.graphics.Color.parseColor(COLOR_LINE_4)) -> 4
        Color(android.graphics.Color.parseColor(COLOR_LINE_5)) -> 5
        Color(android.graphics.Color.parseColor(COLOR_LINE_6)) -> 6
        Color(android.graphics.Color.parseColor(COLOR_LINE_7)) -> 7
        else -> -1
    }
}


fun readJsonStationsAsText(fileName: String): MutableMap<String, Station> {
    val path =
        "/home/mosayeb/MyAndroidStuff/AndroidProjects/RealeaseVersion/tehro/app/src/main/res/raw/$fileName.json"
    val file = File(path).readText(Charsets.UTF_8)
    val stations: MutableMap<String, Station> = Json.decodeFromString(file)
    return stations
}

fun calculateLineName(lineNumber: Int): String {
    val endpoint = getLineEndpoints()
    return "Line $lineNumber - ${endpoint[lineNumber]?.first}/${endpoint[lineNumber]?.second}"
}