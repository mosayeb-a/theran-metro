package com.ma.tehro.ui.shortestpath.pathfinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ma.tehro.R
import com.ma.tehro.common.Appbar

@Composable
fun Appbar(from: String, to: String, onBack: () -> Unit) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
        Appbar(
            title = "Suggested Path",
            handleBack = true,
            onBackClick = onBack
        )
        AppbarDetail(from = from, to = to)
        HorizontalDivider()
    }
}

@Composable
fun AppbarDetail(modifier: Modifier = Modifier, from: String, to: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(26.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = to.uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.arrow_back_24px),
            contentDescription = "Going to .."
        )
        Text(
            text = from.uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
