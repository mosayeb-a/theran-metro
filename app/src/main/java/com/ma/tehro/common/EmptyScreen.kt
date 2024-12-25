package com.ma.tehro.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.random.Random


data class EmptyScreenAction(
    val hint: String,
    val onClick: () -> Unit,
)

@Composable
fun EmptyScreen(
    message: String,
    modifier: Modifier = Modifier,
    action: EmptyScreenAction? = null,
) {
    val face = remember { getRandomErrorFace() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = face,
                style = MaterialTheme.typography.displayMedium
                    .copy(color = MaterialTheme.colorScheme.secondary.copy(alpha = .8f)),
            )
        }
        Text(
            text = message,
            modifier = Modifier
                .padding(top = 24.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        if (action != null) {
            TextButton(
                modifier = Modifier
                    .padding(start = 98.dp, end = 98.dp, top = 12.dp),
                onClick = { action.onClick.invoke() },
            ) {
                Text(action.hint)
            }
        }
    }
}


private val ErrorFaces = listOf(
    "(･o･;)",
    "Σ(ಠ_ಠ)",
    "ಥ_ಥ",
    "(˘･_･˘)",
    "(；￣Д￣)",
    "(･Д･。",
)

private fun getRandomErrorFace(): String {
    return ErrorFaces[Random.nextInt(ErrorFaces.size)]
}
