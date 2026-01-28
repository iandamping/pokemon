package com.junemon.pokemon.feature.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SharedPokemonStat(
    statName: String,
    statPoint: Int,
    selectedColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            modifier = Modifier.weight(0.35f),
            style = MaterialTheme.typography.labelMedium
        )

        LinearProgressIndicator(
            progress = {
                statPoint.toFloat() / 200F
            },
            modifier = Modifier
                .weight(0.65f)
                .height(8.dp)
                .clip(CircleShape),
            color = selectedColor,
            trackColor = selectedColor.copy(alpha = 0.2f)
        )
        Text(
            text = statPoint.toString(),
            modifier = Modifier.weight(0.1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
