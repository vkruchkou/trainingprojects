package com.trainingproject.habittrackerapp.di.datadi

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.trainingproject.data.local.FirebaseAuthSource
import com.trainingproject.data.local.FirebaseFirestoreSource
import com.trainingproject.data.remote.QuoteApi
import com.trainingproject.data.repository.AuthRepositoryImpl
import com.trainingproject.data.repository.HabitRepositoryImpl
import com.trainingproject.data.repository.QuoteRepositoryImpl
import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import com.trainingproject.habittrackerapp.domain.repository.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(QuoteApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideQuoteApi(retrofit: Retrofit): QuoteApi {
        return retrofit.create(QuoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthSource(firebaseAuth: FirebaseAuth): FirebaseAuthSource {
        return FirebaseAuthSource(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreSource(firestore: FirebaseFirestore): FirebaseFirestoreSource {
        return FirebaseFirestoreSource(firestore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuthSource: FirebaseAuthSource): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthSource)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(firestoreSource: FirebaseFirestoreSource): HabitRepository {
        return HabitRepositoryImpl(firestoreSource)
    }

    @Provides
    @Singleton
    fun provideQuoteRepository(quoteApi: QuoteApi): QuoteRepository {
        return QuoteRepositoryImpl(quoteApi)
    }
}
