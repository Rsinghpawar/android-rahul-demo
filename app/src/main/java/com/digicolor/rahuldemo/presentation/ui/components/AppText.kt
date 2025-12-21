package com.digicolor.rahuldemo.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digicolor.rahuldemo.R
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.theme.customColors
import com.digicolor.rahuldemo.util.CurrencyUtils
import com.digicolor.rahuldemo.util.toRupeeString


@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun PrimaryText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SecondaryText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ValueText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun EmphasisText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}


@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun CaptionText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium
    )
}



@Composable
fun StatusText(
    text: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = if (isPositive)
            MaterialTheme.customColors.positiveValue  // Use custom colors
        else
            MaterialTheme.customColors.negativeValue
    )
}

@Composable
fun ValueTextRupee(
    value: Double,
    modifier: Modifier = Modifier,
    decimalPlaces: Int = 2,
    showSymbol: Boolean = true
) {
    val symbol = stringResource(R.string.currency_symbol_rupee)
    val formattedValue = if (showSymbol) {
        CurrencyUtils.formatFinancialValueIndian(value, symbol, decimalPlaces)
    } else {
        String.format("%.${decimalPlaces}f", value)
    }

    Text(
        text = formattedValue,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun FinancialValueText(
    value: Double,
    modifier: Modifier = Modifier,
    decimalPlaces: Int = 2,
    showSymbol: Boolean = true
) {
    val symbol = stringResource(R.string.currency_symbol_rupee)
    val formattedValue = if (showSymbol) {
        CurrencyUtils.formatFinancialValueIndian(value, symbol, decimalPlaces)
    } else {
        val absValue = kotlin.math.abs(value)
        val formatted = String.format("%.${decimalPlaces}f", absValue)
        if (value < 0) "-$formatted" else formatted
    }

    val color = when {
        value < 0 -> MaterialTheme.customColors.negativeValue
        value > 0 -> MaterialTheme.customColors.positiveValue
        else -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = formattedValue,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = color
    )
}


@Preview(
    name = "App Texts",
    showBackground = true
)
@Composable
fun AppTextPreview() {
    RahulDemoTheme() {
        Column {
            HeadingText("Heading Text")

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryText("Primary Text")

            Spacer(modifier = Modifier.height(8.dp))

            SecondaryText("Secondary Text")

            Spacer(modifier = Modifier.height(12.dp))

            ValueText("₹ 119.10")

            Spacer(modifier = Modifier.height(8.dp))

            StatusText(
                text = "+₹ 12.90",
                isPositive = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            StatusText(
                text = "-₹ 235.65",
                isPositive = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            EmphasisText("₹ 27,893.65")

            Spacer(modifier = Modifier.height(12.dp))

            BodyText("This is body text used for descriptions or supporting content.")

            Spacer(modifier = Modifier.height(8.dp))

            CaptionText("CAPTION / TAG")
        }
    }
}

