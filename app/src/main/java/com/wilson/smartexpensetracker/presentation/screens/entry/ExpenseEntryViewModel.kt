package com.wilson.smartexpensetracker.presentation.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.domain.usecase.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

@HiltViewModel
class ExpenseEntryViewModel @javax.inject.Inject constructor(
    private val expenseUseCases: ExpenseUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseEntryState())
    val state: StateFlow<ExpenseEntryState> = _state.asStateFlow()

    private val eventChannel = Channel<UiEvent>()
    val events = eventChannel.receiveAsFlow()

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object ExpenseAdded : UiEvent()
    }

    init {
        loadTotalSpentToday()
    }

    fun onTitleChange(value: String) {
        _state.update { it.copy(title = value) }
    }

    fun onAmountChange(value: String) {
        _state.update { it.copy(amount = value) }
    }

    fun onCategoryChange(value: String) {
        _state.update { it.copy(category = value) }
    }

    fun onNotesChange(value: String) {
        _state.update { it.copy(notes = value) }
    }

    fun onReceiptSelected(path: String?) {
        _state.update { it.copy(receiptPath = path) }
    }

    fun saveExpense() {
        val title = state.value.title.trim()
        val amount = state.value.amount.toDoubleOrNull()
        val category = state.value.category

        if (title.isEmpty()) {
            sendEvent(UiEvent.ShowToast("Title cannot be empty"))
            return
        }
        if (amount == null || amount <= 0) {
            sendEvent(UiEvent.ShowToast("Enter a valid amount"))
            return
        }
        if (category.isEmpty()) {
            sendEvent(UiEvent.ShowToast("Select a category"))
            return
        }

        val expense = Expense(
            id = UUID.randomUUID().toString(),
            title = title,
            amount = amount,
            category = category,
            notes = state.value.notes.takeIf { it.isNotBlank() },
            receiptPath = state.value.receiptPath,
            timestamp = System.currentTimeMillis(),
            isSynced = false
        )

        viewModelScope.launch {
            expenseUseCases.addExpense(expense)
            sendEvent(UiEvent.ExpenseAdded)
            resetForm()
            loadTotalSpentToday()
        }
    }

    private fun loadTotalSpentToday() {
        viewModelScope.launch {
            val todayStart = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            expenseUseCases.getExpensesByDate(todayStart).collect { expenses ->
                val total = expenses.sumOf { it.amount }
                _state.update { it.copy(totalSpentToday = total) }
            }
        }
    }

    private fun resetForm() {
        _state.update {
            it.copy(
                title = "",
                amount = "",
                category = "",
                notes = "",
                receiptPath = null
            )
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}
