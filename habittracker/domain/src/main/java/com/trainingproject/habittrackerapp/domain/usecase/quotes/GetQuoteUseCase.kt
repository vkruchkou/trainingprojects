package com.trainingproject.habittrackerapp.domain.usecase.quotes

import com.trainingproject.habittrackerapp.domain.models.Quote
import com.trainingproject.habittrackerapp.domain.repository.QuoteRepository
import javax.inject.Inject

class GetQuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(): Result<Quote> {
        return quoteRepository.getRandomQuote()
    }
}