    package com.ma.tehro

    import com.ma.tehro.data.StationData
    import com.ma.tehro.ui.shortestpath.PathItem
    import com.ma.tehro.ui.shortestpath.ShortestPathViewModel
    import kotlinx.serialization.json.Json
    import org.junit.Assert.assertEquals
    import org.junit.Test
    import java.io.File

    class ShortestPathViewModelTest {
        private var viewModel: ShortestPathViewModel
        private var stations: Map<String, StationData>

        init {
            val filePath =
                "/home/mosayeb/MyAndroidStuff/AndroidProjects/RealeaseVersion/tehro/app/src/main/res/raw/station2.json"
            val stationsJson = File(filePath).readText(Charsets.UTF_8)
            stations = Json.decodeFromString(stationsJson)
            viewModel = ShortestPathViewModel(stations)
        }

        @Test
        fun `find shortest path with direction from Darvazeh Shemiran to Ayatollah Taleghani`() {
            val from = "Darvazeh Shemiran"
            val to = "Ayatollah Taleghani"

            val actualPath = viewModel.findShortestPathWithDirection(from, to)

            val expectedPath = actualPath.map {
                when (it) {
                    is PathItem.Title -> PathItem.Title(it.text)
                    is PathItem.Station -> PathItem.Station(stations[it.data.property.name]!!)
                }
            }

            assertEquals(expectedPath, actualPath)
        }

        @Test
        fun `find shortest path with direction when no path exists`() {
            val from = "Darvazeh Shemiran"
            val to = "Invalid Station"

            val expectedPath = emptyList<PathItem>()
            val actualPath = viewModel.findShortestPathWithDirection(from, to)
            assertEquals(expectedPath, actualPath)
        }
    }
