package com.trainingproject.data.repository

import com.trainingproject.data.remote.QuoteApi
import com.trainingproject.habittrackerapp.domain.models.Quote
import com.trainingproject.habittrackerapp.domain.repository.QuoteRepository
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val quoteApi: QuoteApi
) : QuoteRepository {

    override suspend fun getRandomQuote(): Result<Quote> {
        return try {
            val quotesDto = quoteApi.getRandomQuote()
            if (quotesDto.isNotEmpty()) {
                Result.success(quotesDto.first().toDomain())
            } else {
                Result.failure(Exception("No quote received from API"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
