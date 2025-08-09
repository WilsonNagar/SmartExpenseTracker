package com.wilson.smartexpensetracker.domain.usecase

import com.wilson.smartexpensetracker.data.util.CsvExporter
import com.wilson.smartexpensetracker.data.util.PdfExporter
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first

class ExportExpensesUseCase(
    private val repository: ExpenseRepository,
    private val csvExporter: CsvExporter,
    private val pdfExporter: PdfExporter
) {
    /**
     * Exports expenses in CSV or PDF format using provided exporters.
     * @param format "csv" or "pdf"
     * @return String representing exported file content
     */
    suspend operator fun invoke(format: String = "csv"): String {
        val expenses = repository.getAllExpenses().first()

        return when (format.lowercase()) {
            "csv" -> csvExporter.export(expenses)
            "pdf" -> pdfExporter.export(expenses)
            else -> throw IllegalArgumentException("Unsupported export format: $format")
        }
    }
}
