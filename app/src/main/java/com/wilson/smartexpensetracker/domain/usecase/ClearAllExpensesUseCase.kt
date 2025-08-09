package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository

class ClearAllExpensesUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke() {
        repository.clearAll()
    }
}
