package com.trainingproject.data.remote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.trainingproject.habittrackerapp.domain.models.Quote

@Serializable
data class QuoteDto(
    @SerialName("q") val q: String,

    @SerialName("a") val a: String,

    @SerialName("h") val h: String
) {
    fun toDomain(): Quote {
        return Quote(q, a)
    }
}