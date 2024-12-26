package com.ma.tehro.ui.shortestpath.pathfinder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ma.tehro.R
import com.ma.tehro.common.getLineColor
import com.ma.tehro.common.timelineview.TimelineView
import com.ma.tehro.common.timelineview.TimelineView.SingleNode
import com.ma.tehro.common.toImageBitmap
import com.ma.tehro.data.StationData
import com.ma.tehro.ui.line.stations.StationItem
import com.ma.tehro.ui.shortestpath.PathItem

@Composable
fun PathFinder(
    modifier: Modifier = Modifier,
    from: String,
    to: String,
    findShortestPath: () -> List<PathItem>,
    onBack: () -> Unit,
    getLineByPath: (path: List<PathItem>) -> List<Pair<Int, Pair<Int, Int>>>,
    isIndexInTheRange: (index: Int, lineAndTitlePosition: List<Pair<Int, Pair<Int, Int>>>) -> Int?
) {
    var path by remember { mutableStateOf<List<PathItem>>(emptyList()) }
    var colorLinePositions by remember { mutableStateOf<List<Pair<Int, Pair<Int, Int>>>>(emptyList()) }

    LaunchedEffect(from, to) {
        if (path.isEmpty()) {
            path = findShortestPath()
            colorLinePositions = getLineByPath(path)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            Appbar(from = from, to = to, onBack = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding),
        ) {
            itemsIndexed(
                items = path,
                key = { index, _ -> index }
            ) { index, item ->
                val color = remember(index) {
                    getLineColor(isIndexInTheRange(index, colorLinePositions) ?: 0)
                }

                when (item) {
                    is PathItem.Title -> {
                        PinableTitle(
                            title = item.text,
                            isFirstItem = index == 0,
                            lineColor = color
                        )
                    }

                    is PathItem.Station -> {
                        StationRow(
                            modifier = Modifier
                                .clickable { },
                            stationData = item.data,
                            itemHeight = 76f,
                            lineColor = color,
                            isLastItem = index == path.size - 1
                        )

                        HorizontalDivider(thickness = .28.dp)

                    }
                }
            }
        }
    }
}


@Composable
fun PinableTitle(
    modifier: Modifier = Modifier,
    title: String,
    isFirstItem: Boolean,
    lineColor: Color,
) {
    val iconPainter =
        painterResource(id = if (isFirstItem) R.drawable.arrow_drop_down_24px else R.drawable.sync_alt_24px)

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val iconImageBitmap = remember {
        iconPainter.toImageBitmap(
            size = Size(38f, 38f),
            density = density,
            layoutDirection = layoutDirection
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(MaterialTheme.colorScheme.secondary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleNode(
            modifier = Modifier.padding(start = 10.5.dp),
            color = lineColor,
            nodeType = if (isFirstItem) TimelineView.NodeType.FIRST else TimelineView.NodeType.MIDDLE,
            nodeSize = 42f,
            isChecked = true,
            lineWidth = 0.8f,
            iconBitmap = if (isFirstItem) iconImageBitmap else iconImageBitmap
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )
    }
}

@Composable
fun StationRow(
    modifier: Modifier = Modifier,
    stationData: StationData,
    itemHeight: Float,
    lineColor: Color,
    isLastItem: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight.dp)
            .background(lineColor)
    ) {
        SingleNode(
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
            nodeType = if (isLastItem) TimelineView.NodeType.LAST else TimelineView.NodeType.MIDDLE,
            nodeSize = 20f,
            isChecked = true,
            lineWidth = 0.8f
        )
        StationItem(
            modifier = Modifier.weight(1f),
            station = stationData,
            itemHeight = itemHeight,
            lineColor = lineColor
        )
    }
}
