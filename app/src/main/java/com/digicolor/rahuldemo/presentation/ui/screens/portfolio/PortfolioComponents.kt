package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.digicolor.rahuldemo.R
import com.digicolor.rahuldemo.domain.model.PortfolioSummary
import com.digicolor.rahuldemo.presentation.theme.DarkBlue
import com.digicolor.rahuldemo.presentation.theme.Dimens
import com.digicolor.rahuldemo.presentation.theme.Dimens.roundedCorner
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.theme.customColors
import com.digicolor.rahuldemo.presentation.ui.components.BodyText
import com.digicolor.rahuldemo.presentation.ui.components.FinancialValueText
import com.digicolor.rahuldemo.presentation.ui.components.HeadingText
import com.digicolor.rahuldemo.presentation.ui.components.PercentageText
import com.digicolor.rahuldemo.presentation.ui.components.SecondaryText
import com.digicolor.rahuldemo.presentation.ui.components.TabText
import com.digicolor.rahuldemo.presentation.ui.components.ValueTextRupee
import com.digicolor.rahuldemo.presentation.ui.widgets.Divider
import com.digicolor.rahuldemo.util.topBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                HeadingText(
                    text = "Portfolio",
                    color = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_up_down),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun PortfolioTabs(
    selectedTab: PortfolioTab,
    onTabSelected: (PortfolioTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = Color.White,
        contentColor = DarkBlue,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = DarkBlue
            )
        },
        divider = {
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        }
    ) {
        PortfolioTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    TabText(
                        text = tab.name,
                        isSelected = selectedTab == tab
                    )
                }
            )
        }
    }
}

@Composable
fun PortfolioSummaryView(
    state: PortfolioUiState,
    onToggle: () -> Unit
) {
    state.summary?.let { summary ->
        val shape = RoundedCornerShape(topStart = roundedCorner, topEnd = roundedCorner)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .topBorder(
                    shape = shape
                ),
            shape = shape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(indication = null, interactionSource =  remember { MutableInteractionSource() }) { onToggle() }
                    .padding(horizontal = Dimens.listItemHorizontal, vertical = Dimens.space12)
            ) {
                AnimatedVisibility(visible = state.isExpanded) {
                    Column {
                        SummaryRow(label = "Current value*", value = summary.currentValue)
                        SummaryRow(label = "Total investment*", value = summary.totalInvestment)
                        SummaryRow(
                            label = "Today's Profit & Loss*",
                            value = summary.todayPnL,
                            isFinancial = true
                        )
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BodyText(
                            text = stringResource(R.string.profit_loss),
                            color = Color.Gray
                        )
                        Icon(
                            imageVector = if (state.isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FinancialValueText(value = summary.totalPnL)
                        PercentageText(
                            value = summary.totalPnLPercentage,
                            isPnL = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: Double,
    isFinancial: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SecondaryText(text = label)
        if (isFinancial) {
            FinancialValueText(value = value)
        } else {
            ValueTextRupee(value = value)
        }
    }
}

@Preview
@Composable
private fun PortfolioSummaryViewPreview() {
    RahulDemoTheme() {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.BottomStart
        ) {
            PortfolioSummaryView(
                state = PortfolioUiState(
                    summary = PortfolioSummary(
                        currentValue = 101.0,
                        totalInvestment = 1012.12,
                        totalPnL = 2000.2,
                        totalPnLPercentage = 2.2,
                        todayPnL = -235.65,
                        todayPnLPercentage = -0.84
                    ), isExpanded = true
                ),
                onToggle = {

                }
            )
        }
    }
}
