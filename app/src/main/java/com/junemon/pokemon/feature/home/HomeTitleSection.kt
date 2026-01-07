package com.junemon.pokemon.feature.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.junemon.pokemon.R
import com.junemon.pokemon.ui.theme.PokemonTheme

@Composable
fun HomeTitleSection(
    modifier: Modifier = Modifier,
    onIconSearchClick: () -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (titleRef, secondTitleRef, iconSearchRef) = createRefs()
        IconButton(
            onClick = {
                onIconSearchClick.invoke()
            },
            modifier = Modifier
                .constrainAs(iconSearchRef) {
                    top.linkTo(titleRef.top)
                    bottom.linkTo(titleRef.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = Color.Black
            )
        }

        Text(
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(iconSearchRef.start)
                width = Dimension.fillToConstraints
            },
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.constrainAs(secondTitleRef) {
                top.linkTo(titleRef.bottom)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            text = stringResource(R.string.sub_main_title),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeTitleSectionPreview() {
    PokemonTheme {
        HomeTitleSection(onIconSearchClick = {})
    }
}
