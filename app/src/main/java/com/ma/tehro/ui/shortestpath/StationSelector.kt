package com.ma.tehro.ui.shortestpath

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ma.tehro.common.Appbar
import com.ma.tehro.common.timelineview.TimelineView
import com.ma.tehro.common.timelineview.TimelineView.SingleNode

@Composable
fun StationSelector(
    viewState: PathUiState,
    stations: List<String>,
    onFindPathClick: (from: String, to: String) -> Unit,
    onSelectedChange: (isFrom: Boolean, query: String) -> Unit,
    onBack: () -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            Column {
                Appbar(
                    title = "Path Finder",
                    handleBack = true,
                    onBackClick = onBack
                )
                HorizontalDivider()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(it)
                .fillMaxWidth()
        ) {
            StationDropdown(
                query = viewState.selectedStartStation,
                stations = stations,
                onStationSelected = { query -> onSelectedChange(true, query) },
                isFrom = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            StationDropdown(
                query = viewState.selectedDestStation,
                stations = stations,
                onStationSelected = { query -> onSelectedChange(false, query) },
                isFrom = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(76.dp),
                onClick = {
                    onFindPathClick(viewState.selectedStartStation, viewState.selectedDestStation)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = .5f)
                ),
                enabled = viewState.selectedStartStation.isNotEmpty() &&
                        viewState.selectedDestStation.isNotEmpty()
            ) {
                Text("Find Path")
            }

        }
    }
}

@Composable
fun StationDropdown(
    query: String,
    stations: List<String>,
    onStationSelected: (String) -> Unit,
    isFrom: Boolean
) {
    var selectedStation by rememberSaveable { mutableStateOf(query) }
    SearchableExpandedDropDownMenu(
        listOfItems = stations,
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth(),
        onDropDownItemSelected = { station ->
            selectedStation = station
            onStationSelected(station)
        },
        dropdownItem = { stationName ->
            Text(text = stationName)
        },
        defaultItem = { defaultStation ->
            selectedStation = defaultStation
        },
        startContent = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                SingleNode(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
                    nodeType = if (isFrom) TimelineView.NodeType.FIRST else TimelineView.NodeType.LAST,
                    nodeSize = 20f,
                    isChecked = true,
                    lineWidth = 5.2f,
                    isDashed = true
                )
                Text(
                    text = if (isFrom) "FROM" else "TO",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        onSearchTextFieldClicked = {

        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black
        )
    )
}
