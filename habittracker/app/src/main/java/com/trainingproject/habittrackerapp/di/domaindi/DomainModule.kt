package com.trainingproject.habittrackerapp.di.domaindi

import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import com.trainingproject.habittrackerapp.domain.repository.QuoteRepository
import com.trainingproject.habittrackerapp.domain.usecase.auth.GetCurrentUserUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.LoginUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.LogoutUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.RegisterUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.AddHabitUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.DeleteHabitUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.GetHabitByIdUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.GetHabitsUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.UpdateHabitUseCase
import com.trainingproject.habittrackerapp.domain.usecase.quotes.GetQuoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    fun provideAddHabitUseCase(habitRepository: HabitRepository): AddHabitUseCase {
        return AddHabitUseCase(habitRepository)
    }

    @Provides
    fun provideGetHabitsUseCase(habitRepository: HabitRepository): GetHabitsUseCase {
        return GetHabitsUseCase(habitRepository)
    }

    @Provides
    fun provideGetHabitByIdUseCase(habitRepository: HabitRepository): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(habitRepository)
    }

    @Provides
    fun provideUpdateHabitUseCase(habitRepository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepository)
    }

    @Provides
    fun provideDeleteHabitUseCase(habitRepository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(habitRepository)
    }

    @Provides
    fun provideGetQuoteUseCase(quoteRepository: QuoteRepository): GetQuoteUseCase {
        return GetQuoteUseCase(quoteRepository)
    }
}