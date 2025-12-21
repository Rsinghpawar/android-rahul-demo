package com.digicolor.rahuldemo.util

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun `formatFinancialValueIndian - Positive value - returns correctly formatted string with symbol`() {
        // Given
        val value = 1234567.89
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("₹ 12,34,567.89", result)
    }

    @Test
    fun `formatFinancialValueIndian - Negative value - returns correctly formatted string with sign and symbol`() {
        // Given
        val value = -1234567.89
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("-₹ 12,34,567.89", result)
    }

    @Test
    fun `formatFinancialValueIndian - Zero value - returns correctly formatted string`() {
        // Given
        val value = 0.0
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("₹ 0.00", result)
    }

    @Test
    fun `formatFinancialValueIndian - Small value - returns string without commas`() {
        // Given
        val value = 123.45
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("₹ 123.45", result)
    }

    @Test
    fun `formatFinancialValueIndian - Large value with custom decimal places - returns correctly formatted string`() {
        // Given
        val value = 1234567.89123
        val symbol = "$"
        val decimalPlaces = 3

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol, decimalPlaces)

        // Then
        assertEquals("$ 12,34,567.891", result)
    }

    @Test
    fun `formatFinancialValueIndian - NaN value - returns zero formatted string`() {
        // Given
        val value = Double.NaN
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("₹ 0.00", result)
    }

    @Test
    fun `formatFinancialValueIndian - Infinite value - returns zero formatted string`() {
        // Given
        val value = Double.POSITIVE_INFINITY
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("₹ 0.00", result)
    }

    @Test
    fun `formatFinancialValueIndian - Large number with many digits - returns correctly formatted Indian style`() {
        // Given
        val value = 10000000.0 // 1 Crore
        val symbol = "₹"

        // When
        val result = CurrencyUtils.formatFinancialValueIndian(value, symbol)

        // Then
        assertEquals("₹ 1,00,00,000.00", result)
    }
}
