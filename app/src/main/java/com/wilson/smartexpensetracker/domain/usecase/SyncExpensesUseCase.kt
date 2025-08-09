package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.data.util.SyncManager
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first

class SyncExpensesUseCase(
    private val repository: ExpenseRepository,
    private val syncManager: SyncManager
) {
    /**
     * Simulates syncing all expenses with a remote server.
     * Returns true if sync was successful.
     */
    suspend operator fun invoke(): Boolean {
        val expenses = repository.getAllExpenses().first()
        return syncManager.syncExpenses(expenses)
    }
}
