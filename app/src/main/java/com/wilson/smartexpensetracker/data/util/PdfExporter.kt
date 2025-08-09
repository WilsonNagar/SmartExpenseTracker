package com.wilson.smartexpensetracker.data.util

import com.wilson.smartexpensetracker.domain.model.Expense

object PdfExporter {

    /**
     * Generates a mock PDF content string.
     * Replace with actual PDF library integration (e.g., iText, PdfBox) later.
     */
    fun export(expenses: List<Expense>): String {
        return """
            --- Smart Daily Expense Report ---
            Total Records: ${expenses.size}
            Total Amount: ₹${expenses.sumOf { it.amount }}
            Generated: ${System.currentTimeMillis()}
            
            Details:
            ${expenses.joinToString("\n") { "- ${it.title}: ₹${it.amount} (${it.category})" }}
            
            (This is a mock PDF export. Replace with real PDF content generator.)
        """.trimIndent()
    }
}
