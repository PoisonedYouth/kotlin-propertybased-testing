package com.poisonedyouth

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class ApplicationServiceTest {

    private val applicationService = ApplicationService()

    @Test
    fun `throws exception if date is below minDate`() {
        // given
        val inputDto = createValidInputDto().copy(
            date = LocalDate.of(2020, 1, 1)
        )

        // when + then
        val exception = shouldThrow<IllegalArgumentException> {
            applicationService.createOutput(inputDto)
        }
        exception.message shouldBe "Date '2020-01-01' must be within '2022-01-01' and '2022-12-31'."
    }

    @Test
    fun `throws exception if date is above maxDate`() {
        // given
        val inputDto = createValidInputDto().copy(
            date = LocalDate.of(2025, 1, 1),
        )

        // when + then
        val exception = shouldThrow<IllegalArgumentException> {
            applicationService.createOutput(inputDto)
        }
        exception.message shouldBe "Date '2025-01-01' must be within '2022-01-01' and '2022-12-31'."
    }

    @Test
    fun `throws exception if amount is below zero`() {
        // given
        val inputDto = createValidInputDto().copy(
            amount = -1
        )

        // when + then
        val exception = shouldThrow<IllegalArgumentException> {
            applicationService.createOutput(inputDto)
        }
        exception.message shouldBe "Amount '-1' must be greater or equal null."
    }

    @Test
    fun `throws exception if sum of positions amount is below zero`() {
        // given
        val inputDto = createValidInputDto().copy(
            positions = listOf(
                InputPositionDto(
                    name = "First Position",
                    value = 2.3
                ),
                InputPositionDto(
                    name = "Second Position",
                    value = -12.2
                )
            )
        )

        // when + then
        val exception = shouldThrow<IllegalArgumentException> {
            applicationService.createOutput(inputDto)
        }
        exception.message shouldBe "Sum of positions amount must be greater than 0.0 but is EUR -9.90"
    }

    @Test
    fun `returns outputDto for valid input`() {
        // given
        val inputDto = createValidInputDto()

        // when
        val actual = applicationService.createOutput(inputDto)

        // then
        actual shouldBe OutputDto(
            date = LocalDate.of(2022, 1, 1),
            amount = 2,
            positions = listOf(
                OutputPositionDto(
                    name = "First Position",
                    value = 2.3.toEURCurrency()
                ),
                OutputPositionDto(
                    name = "Second Position",
                    value = 12.2.toEURCurrency()
                )
            )
        )
    }
}

private fun createValidInputDto() = InputDto(
    date = LocalDate.of(2022, 1, 1),
    amount = 2,
    positions = listOf(
        InputPositionDto(
            name = "First Position",
            value = 2.3
        ),
        InputPositionDto(
            name = "Second Position",
            value = 12.2
        )
    )
)