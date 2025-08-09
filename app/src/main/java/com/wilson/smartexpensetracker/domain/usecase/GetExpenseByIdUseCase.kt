package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository

class GetExpenseByIdUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(id: String): Expense? {
        return repository.getExpenseById(id)
    }
}
