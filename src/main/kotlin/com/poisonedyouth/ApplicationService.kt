package com.poisonedyouth

import nl.hiddewieringa.money.asCurrency
import nl.hiddewieringa.money.ofCurrency
import nl.hiddewieringa.money.typedMonetaryContext
import org.javamoney.moneta.FastMoney
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import javax.money.MonetaryAmount

val minDate: LocalDate = LocalDate.of(2022, 1, 1)
val maxDate: LocalDate = LocalDate.of(2022, 12, 31)

class ApplicationService {

    fun createOutput(inputDto: InputDto): OutputDto {
        return mapInputToOutput(inputDto)
    }

    private fun mapInputToOutput(inputDto: InputDto): OutputDto {
        return OutputDto(
            date = inputDto.date,
            amount = inputDto.amount,
            positions = mapInputPositionToOutputPosition(
                positions = inputDto.positions
            )
        )
    }

    private fun mapInputPositionToOutputPosition(positions: List<InputPositionDto>): List<OutputPositionDto> {
        require(positions.isNotEmpty()) {
            "Positions must not be empty."
        }
        val sum = positions.fold(0.0) { acc, next -> acc + next.value }
        require(sum.compareTo(0.0) > -1) {
            "Sum of positions amount must be greater than 0.0 but is $sum"
        }
        return positions.map {
            OutputPositionDto(
                name = it.name,
                value = it.value.toEURCurrency()
            )
        }
    }
}

data class InputDto(
    val date: LocalDate,
    val amount: Int,
    val positions: List<InputPositionDto>
)

data class InputPositionDto(
    val name: String,
    val value: Double
)

data class OutputDto(
    val date: LocalDate,
    val amount: Int,
    val positions: List<OutputPositionDto>
) {
    init {
        require(date in minDate..maxDate) {
            "Date '${date}' must be within '$minDate' and '${maxDate}'."
        }

        require(amount >= 0) {
            "Amount '$amount' must be greater or equal null."
        }
    }

}

data class OutputPositionDto(
    val name: String,
    val value: MonetaryAmount
)

fun Double.toEURCurrency(): MonetaryAmount {
    return BigDecimal(this, MathContext(2, RoundingMode.HALF_UP))
        .ofCurrency<FastMoney>("EUR".asCurrency(), typedMonetaryContext<FastMoney> {
        setPrecision(2)
    })
}
