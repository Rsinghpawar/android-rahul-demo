package com.digicolor.rahuldemo.data.mapper

import com.digicolor.rahuldemo.data.dto.HoldingDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HoldingMapperTest {

    @Test
    fun `toDomainOrNull - All fields present - returns mapped Holding`() {
        // Given
        val dto = HoldingDto(
            symbol = "AAPL",
            quantity = 10,
            ltp = 150.0,
            avgPrice = 145.0,
            close = 148.0
        )

        // When
        val result = dto.toDomainOrNull()

        // Then
        assertEquals("AAPL", result?.symbol)
        assertEquals(10, result?.quantity)
        assertEquals(150.0, result?.lastTradedPrice!!, 0.0)
        assertEquals(145.0, result.avgPrice, 0.0)
        assertEquals(148.0, result.closePrice, 0.0)
    }

    @Test
    fun `toDomainOrNull - Symbol missing - returns null`() {
        // Given
        val dto = HoldingDto(
            symbol = null,
            quantity = 10,
            ltp = 150.0,
            avgPrice = 145.0,
            close = 148.0
        )

        // When
        val result = dto.toDomainOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `toDomainOrNull - Quantity missing - returns null`() {
        // Given
        val dto = HoldingDto(
            symbol = "AAPL",
            quantity = null,
            ltp = 150.0,
            avgPrice = 145.0,
            close = 148.0
        )

        // When
        val result = dto.toDomainOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `toDomainOrNull - LTP missing - returns null`() {
        // Given
        val dto = HoldingDto(
            symbol = "AAPL",
            quantity = 10,
            ltp = null,
            avgPrice = 145.0,
            close = 148.0
        )

        // When
        val result = dto.toDomainOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `toDomainOrNull - AvgPrice missing - returns null`() {
        // Given
        val dto = HoldingDto(
            symbol = "AAPL",
            quantity = 10,
            ltp = 150.0,
            avgPrice = null,
            close = 148.0
        )

        // When
        val result = dto.toDomainOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `toDomainOrNull - Close missing - returns null`() {
        // Given
        val dto = HoldingDto(
            symbol = "AAPL",
            quantity = 10,
            ltp = 150.0,
            avgPrice = 145.0,
            close = null
        )

        // When
        val result = dto.toDomainOrNull()

        // Then
        assertNull(result)
    }
}
