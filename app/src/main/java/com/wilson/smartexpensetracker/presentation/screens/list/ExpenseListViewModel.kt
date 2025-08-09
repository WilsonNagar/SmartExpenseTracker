package com.wilson.smartexpensetracker.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilson.smartexpensetracker.domain.usecase.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val useCases: ExpenseUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseListState())
    val state: StateFlow<ExpenseListState> = _state.asStateFlow()

    init {
        loadExpensesForDate(System.currentTimeMillis())
    }

    fun loadExpensesForDate(dateMillis: Long) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            useCases.getExpensesByDate(dateMillis).collect { expenses ->
                _state.update {
                    it.copy(
                        expenses = expenses,
                        totalAmount = expenses.sumOf { e -> e.amount },
                        totalCount = expenses.size,
                        selectedDate = dateMillis,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun toggleGroupByCategory() {
        _state.update { it.copy(groupByCategory = !it.groupByCategory) }
    }

    fun deleteExpense(id: String) {
        viewModelScope.launch {
            val expenseToDelete = _state.value.expenses.find { it.id == id }
            expenseToDelete?.let { useCases.deleteExpense(it) }
        }
    }
}
