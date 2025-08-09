package com.wilson.smartexpensetracker.data.util

import com.wilson.smartexpensetracker.domain.model.Expense

object CsvExporter {

    fun export(expenses: List<Expense>): String {
        val header = "ID,Title,Amount,Category,Notes,ReceiptPath,Timestamp,IsSynced"
        val rows = expenses.joinToString("\n") { e ->
            listOf(
                e.id,
                e.title,
                e.amount.toString(),
                e.category,
                e.notes ?: "",
                e.receiptPath ?: "",
                e.timestamp.toString(),
                e.isSynced.toString()
            ).joinToString(",")
        }
        return "$header\n$rows"
    }
}
