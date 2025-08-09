package com.wilson.smartexpensetracker.presentation.screens.entry
import android.net.Uri

//data class ExpenseEntryState(
//    val title: String = "",
//    val amount: String = "",
//    val category: String = "",
//    val notes: String = "",
//    val receiptImageUri: Uri? = null,
//    val totalSpentToday: Double = 0.0,
//    val isSaving: Boolean = false,
//    val errorMessage: String? = null
//)
//
//package com.wilson.smartexpensetracker.presentation.expense_entry

data class ExpenseEntryState(
    val title: String = "",
    val amount: String = "",
    val category: String = "",
    val notes: String = "",
    val receiptPath: String? = null,
    val totalSpentToday: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

