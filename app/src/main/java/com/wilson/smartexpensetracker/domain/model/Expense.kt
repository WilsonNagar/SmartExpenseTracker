package com.wilson.smartexpensetracker.domain.model

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val receiptPath: String? = null,
    val timestamp: Long,
    val isSynced: Boolean = false
)
