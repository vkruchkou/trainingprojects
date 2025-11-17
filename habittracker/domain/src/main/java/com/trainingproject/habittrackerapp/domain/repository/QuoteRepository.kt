package com.trainingproject.habittrackerapp.domain.repository

import com.trainingproject.habittrackerapp.domain.models.Quote

interface QuoteRepository {
    suspend fun getRandomQuote(): Result<Quote>
}