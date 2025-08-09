package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesByDateUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(epochMillis: Long): Flow<List<Expense>> {
        return repository.getExpensesByDate(epochMillis)
    }
}
