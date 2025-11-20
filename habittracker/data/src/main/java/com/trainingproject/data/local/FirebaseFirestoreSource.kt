package com.trainingproject.data.local

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.trainingproject.habittrackerapp.domain.models.Habit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val habitsCollection = firestore.collection("habits")

    fun getHabits(userId: String): Flow<List<Habit>> = callbackFlow {
        val subscription = habitsCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val habits = snapshot.toObjects(Habit::class.java)
                    trySend(habits).isSuccess
                } else {
                    trySend(emptyList()).isSuccess
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun getHabitById(habitId: String): Habit? {
        return try {
            habitsCollection.document(habitId).get().await().toObject(Habit::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addHabit(habit: Habit) {
        if (habit.id.isEmpty()) {
            habitsCollection.add(habit).await()
        } else {
            habitsCollection.document(habit.id).set(habit).await()
        }
    }

    suspend fun updateHabit(habit: Habit) {
        habitsCollection.document(habit.id).set(habit).await()
    }

    suspend fun deleteHabit(habitId: String) {
        habitsCollection.document(habitId).delete().await()
    }
}