package com.poisonedyouth

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bigDecimal
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs


class ApplicationServicePropertyBasedTest : AnnotationSpec() {

    private val applicationService = ApplicationService()

    @Test
    suspend fun `createInput not throws exception for valid input`() {
        checkAll(createInputArb()) { inputDto ->
            applicationService.createOutput(inputDto)
        }
    }
}


fun createInputArb() = arbitrary {
    InputDto(
        date = dateArb.bind(),
        amount = amountArb.bind(),
        positions = createInputPositionsArb().bind()
    )
}

fun createInputPositionsArb() = arbitrary {
    val positions = mutableListOf<InputPositionDto>()
    val amountOfPositions = amountPositionsArb.bind()
    var totalSum = 0.0
    repeat(amountOfPositions) {
        val value = valueArb.bind().setScale(Arb.int(0..10).bind(), RoundingMode.HALF_UP).toDouble()
        totalSum += value
        positions.add(createInputPositionArb(value).bind())
    }
    if (totalSum < 0.0) {
        positions.add(createInputPositionArb(abs(totalSum)).bind())
    }
    positions.toList()
}

fun createInputPositionArb(value: Double) = arbitrary {
    InputPositionDto(
        name = nameArb.bind(),
        value = value
    )
}

private val dateArb = Arb.localDate(minDate = minDate, maxDate = maxDate)
private val amountArb = Arb.int(min = 0, max = Int.MAX_VALUE)
private val valueArb = Arb.bigDecimal(
    min = BigDecimal(-10000.0),
    max = BigDecimal(10000.0)
)
private val amountPositionsArb = Arb.int(min = 1, max = 100)
private val nameArb = Arb.string(minSize = 1, maxSize = 100)
