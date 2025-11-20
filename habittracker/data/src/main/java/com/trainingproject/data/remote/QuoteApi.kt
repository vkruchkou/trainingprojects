package com.trainingproject.data.remote
import retrofit2.http.GET

interface QuoteApi {
    @GET("/api/random")
    suspend fun getRandomQuote(): List<QuoteDto>

    companion object {
        const val BASE_URL = "https://zenquotes.io/"
    }
}
