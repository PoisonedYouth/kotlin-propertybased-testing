package com.poisonedyouth

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.util.stream.Stream

class ApplicationServiceParameterizedTest {
    private val applicationService = ApplicationService()


    @ParameterizedTest
    @MethodSource("createInputData")
    fun `returns outputDto for valid input`(inputDto: InputDto, outputDto: OutputDto) {

        // when
        val actual = applicationService.createOutput(inputDto)

        // then
        actual shouldBe outputDto
    }


    companion object {
        @JvmStatic
        private fun createInputData() = Stream.of(
            Arguments.arguments(
                InputDto(
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
                ),
                OutputDto(
                    date = LocalDate.of(2022, 1, 1),
                    amount = 2,
                    positions = listOf(
                        OutputPositionDto(
                            name = "First Position",
                            value = 2.3.toEURCurrency()
                        ),
                        OutputPositionDto(
                            name = "Second Position",
                            12.2.toEURCurrency()
                        )
                    )
                )
            ),
            Arguments.arguments(
                InputDto(
                    date = LocalDate.of(2022, 12, 31),
                    amount = 8,
                    positions = listOf(
                        InputPositionDto(
                            name = "First Position",
                            value = -0.0003
                        ),
                        InputPositionDto(
                            name = "Second Position",
                            value = 0.0004
                        )
                    )
                ),
                OutputDto(
                    date = LocalDate.of(2022, 12, 31),
                    amount = 8,
                    positions = listOf(
                        OutputPositionDto(
                            name = "First Position",
                            value = (-0.0003).toEURCurrency()
                        ),
                        OutputPositionDto(
                            name = "Second Position",
                            (0.0004).toEURCurrency()
                        ),
                    )
                )
            ),
            Arguments.arguments(
                InputDto(
                    date = LocalDate.of(2022, 1, 1),
                    amount = 2,
                    positions = listOf(
                        InputPositionDto(
                            name = "First Position",
                            value = -122.30
                        ),
                        InputPositionDto(
                            name = "Second Position",
                            value = 122.31
                        )
                    )
                ),
                OutputDto(
                    date = LocalDate.of(2022, 1, 1),
                    amount = 2,
                    positions = listOf(
                        OutputPositionDto(
                            name = "First Position",
                            value = (-122.30).toEURCurrency()
                        ),
                        OutputPositionDto(
                            name = "Second Position",
                            122.31.toEURCurrency()
                        )
                    )
                )
            )
        )
    }
}