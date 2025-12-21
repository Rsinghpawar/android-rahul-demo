package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import com.digicolor.rahuldemo.domain.usecase.GetUserHoldingsUseCase
import javax.inject.Inject


class PortfolioViewModel @Inject constructor(
    private val getUserHoldingsUseCase: GetUserHoldingsUseCase
) : ViewModel() {



}