package com.trainingproject.habittrackerapp.usecase.quotes

import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.models.Quote
import com.trainingproject.habittrackerapp.domain.repository.QuoteRepository
import com.trainingproject.habittrackerapp.domain.usecase.quotes.GetQuoteUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetQuoteUseCaseTest {
    private lateinit var getQuoteUseCase: GetQuoteUseCase
    private lateinit var mockQuoteRepository: QuoteRepository

    @Before
    fun setUp() {
        mockQuoteRepository = mock()
        getQuoteUseCase = GetQuoteUseCase(mockQuoteRepository)
    }

    @Test
    fun `invoke returns success result when repository succeeds`() = runTest {

        val expectedQuote = Quote(
            quoteText = "some test",
            author = "some author"
        )
        whenever(mockQuoteRepository.getRandomQuote()).thenReturn(Result.success(expectedQuote))

        Assert.assertEquals(getQuoteUseCase().getOrNull(), expectedQuote)
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runTest {
        whenever(mockQuoteRepository.getRandomQuote()).thenReturn(Result.failure(RuntimeException()))

        Assert.assertTrue(getQuoteUseCase().isFailure)
    }
}