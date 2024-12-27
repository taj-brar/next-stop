package com.tb.nextstop.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tb.nextstop.R
import com.tb.nextstop.ui.theme.NextStopTheme

@Composable
fun NavigationBar(
    onNearbyClicked: () -> Unit,
    onMapClicked: () -> Unit,
    onSavedClicked: () -> Unit,
    selectedScreen: String = NextStopApp.Nearby.name,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(
                horizontal = dimensionResource(R.dimen.padding_small),
                vertical = dimensionResource(R.dimen.padding_large)
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        NavigationButton(
            labelId = R.string.nearby,
            iconId = R.drawable.nearby,
            onClick = onNearbyClicked,
            selected = selectedScreen == NextStopApp.Nearby.name
        )
        NavigationButton(
            labelId = R.string.map,
            iconId = R.drawable.map,
            onClick = onMapClicked,
            selected = selectedScreen == NextStopApp.Map.name
        )
        NavigationButton(
            labelId = R.string.saved,
            iconId = R.drawable.saved,
            onClick = onSavedClicked,
            selected = selectedScreen == NextStopApp.Saved.name
        )
    }
}

@Composable
fun NavigationButton(
    @StringRes labelId: Int,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    selected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val icon =
        @Composable
        {
            Image(
                painter = painterResource(iconId),
                contentDescription = stringResource(labelId),
                modifier = modifier.size(dimensionResource(R.dimen.nav_bar_icon_size))
            )
        }

    if (selected) {
        OutlinedButton(onClick = {}) { icon() }
    } else {
        Button(onClick = onClick) { icon() }
    }
}

@Preview
@Composable
fun NavigationBarPreview() {
    NextStopTheme {
        NavigationBar(
            onNearbyClicked = {},
            onMapClicked = {},
            onSavedClicked = {},
        )
    }
}