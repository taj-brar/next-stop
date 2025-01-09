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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
    val nearbySelected = selectedScreen == NextStopApp.Nearby.name
    val mapSelected = selectedScreen == NextStopApp.Map.name
    val savedSelected = selectedScreen == NextStopApp.Saved.name
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
            iconId = if (nearbySelected)
                R.drawable.nearby_purple else R.drawable.nearby_black,
            onClick = onNearbyClicked,
            selected = nearbySelected
        )
        NavigationButton(
            labelId = R.string.map,
            iconId = if (mapSelected)
                R.drawable.map_purple else R.drawable.map_black,
            onClick = onMapClicked,
            selected = mapSelected
        )
        NavigationButton(
            labelId = R.string.saved,
            iconId = if (savedSelected)
                R.drawable.saved_purple else R.drawable.saved_black,
            onClick = onSavedClicked,
            selected = savedSelected
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
                modifier = Modifier.size(dimensionResource(R.dimen.nav_bar_icon_size))
            )
        }
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    val onClickFunc = if (selected) ({}) else onClick
    Button(
        onClick = onClickFunc,
        colors = buttonColors,
        modifier = modifier
    ) {
        icon()
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