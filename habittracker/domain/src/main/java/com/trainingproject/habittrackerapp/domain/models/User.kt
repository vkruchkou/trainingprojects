package com.trainingproject.habittrackerapp.domain.models

data class User(
    val id: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null
)