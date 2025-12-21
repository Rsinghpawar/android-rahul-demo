package com.digicolor.rahuldemo.data.mapper

import com.digicolor.rahuldemo.data.dto.HoldingDto
import com.digicolor.rahuldemo.domain.model.Holding

fun HoldingDto.toDomainOrNull(): Holding? {
    val symbol = symbol ?: return null
    val quantity = quantity ?: return null
    val ltp = ltp ?: return null
    val avgPrice = avgPrice ?: return null
    val close = close ?: return null

    return Holding(
        symbol = symbol,
        quantity = quantity,
        lastTradedPrice = ltp,
        avgPrice = avgPrice,
        closePrice = close
    )
}
