package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository

class UpdateExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        require(expense.title.isNotBlank()) { "Expense title cannot be empty" }
        require(expense.amount > 0) { "Expense amount must be greater than zero" }
        repository.updateExpense(expense)
    }
}
