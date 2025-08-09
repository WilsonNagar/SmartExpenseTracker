package com.wilson.smartexpensetracker.domain.usecase

data class ExpenseUseCases(
    val addExpense: AddExpenseUseCase,
    val getExpensesByDate: GetExpensesByDateUseCase,
    val getAllExpenses: GetAllExpensesUseCase,
    val getExpenseById: GetExpenseByIdUseCase,
    val updateExpense: UpdateExpenseUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val clearAllExpenses: ClearAllExpensesUseCase,
    val exportExpenses: ExportExpensesUseCase,
    val syncExpenses: SyncExpensesUseCase
)
