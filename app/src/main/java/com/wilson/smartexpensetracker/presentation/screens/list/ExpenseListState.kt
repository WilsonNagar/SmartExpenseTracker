package com.wilson.smartexpensetracker.presentation.screens.list

import com.wilson.smartexpensetracker.domain.model.Expense

data class ExpenseListState(
    val expenses: List<Expense> = emptyList(),
    val totalAmount: Double = 0.0,
    val totalCount: Int = 0,
    val selectedDate: Long = System.currentTimeMillis(),
    val groupByCategory: Boolean = false,
    val isLoading: Boolean = false
)
