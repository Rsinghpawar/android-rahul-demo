package com.digicolor.rahuldemo.presentation.ui.widgets

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digicolor.rahuldemo.presentation.theme.DividerColor

@Composable
internal fun Divider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = DividerColor
    )
}