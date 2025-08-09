package com.wilson.smartexpensetracker.data.util

import com.wilson.smartexpensetracker.domain.model.Expense
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SyncManager {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    /**
     * Simulates syncing expenses with a remote server.
     * In real-world, replace this with Retrofit/GraphQL/WebSocket calls.
     */
    suspend fun syncExpenses(expenses: List<Expense>): Boolean {
        _isSyncing.value = true
        delay(2000) // simulate network delay
        _isSyncing.value = false
        return true // simulate success
    }
}
