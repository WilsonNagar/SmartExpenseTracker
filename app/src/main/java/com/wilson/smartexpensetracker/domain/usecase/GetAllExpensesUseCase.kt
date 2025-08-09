package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetAllExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> {
        return repository.getAllExpenses()
    }
}
