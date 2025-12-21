package com.digicolor.rahuldemo.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.digicolor.rahuldemo.R
import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.presentation.theme.Dimens
import com.digicolor.rahuldemo.presentation.theme.Dimens.listItemHorizontal
import com.digicolor.rahuldemo.presentation.theme.Dimens.listItemVertical
import com.digicolor.rahuldemo.presentation.theme.Dimens.sectionSpacing
import com.digicolor.rahuldemo.presentation.theme.DividerColor
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.ui.components.FinancialValueText
import com.digicolor.rahuldemo.presentation.ui.components.PrimaryText
import com.digicolor.rahuldemo.presentation.ui.components.SecondaryText
import com.digicolor.rahuldemo.presentation.ui.components.ValueText
import com.digicolor.rahuldemo.presentation.ui.components.ValueTextRupee

@Composable
fun StockListItem(holding: Holding, modifier: Modifier = Modifier) {

    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = Dimens.space2,
            color = DividerColor
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = listItemHorizontal,
                    vertical = listItemVertical
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PrimaryText(holding.symbol)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryText(stringResource(R.string.label_ltp))
                    ValueTextRupee(
                        value = holding.lastTradedPrice
                    )
                }

            }
            Spacer(Modifier.height(sectionSpacing))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryText(stringResource(R.string.label_net_qty))
                    ValueText(holding.quantity.toString())
                }
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryText("P&L: ")
                    FinancialValueText(holding.totalPnL)
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun StockListItemPreview() {
    RahulDemoTheme(darkTheme = false) {
        Scaffold() {
            Column() {
                StockListItem(
                    holding = Holding(
                        symbol = "ICICI",
                        quantity = 20,
                        lastTradedPrice = 20000.0,
                        avgPrice = 20.0,
                        closePrice = 19.0
                    )
                )
                StockListItem(
                    holding = Holding(
                        symbol = "ICICI",
                        quantity = 20,
                        lastTradedPrice = 10.0,
                        avgPrice = 20.0,
                        closePrice = 19.0
                    )
                )
                StockListItem(
                    holding = Holding(
                        symbol = "ICICI",
                        quantity = 3,
                        lastTradedPrice = 23.0,
                        avgPrice = 20.0,
                        closePrice = 19.0
                    )
                )
            }

        }

    }

}