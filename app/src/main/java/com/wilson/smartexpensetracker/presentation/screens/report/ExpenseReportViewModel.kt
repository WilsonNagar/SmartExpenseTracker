package com.wilson.smartexpensetracker.presentation.screens.report

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wilson.smartexpensetracker.domain.usecase.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseReportViewModel @Inject constructor(
    private val useCases: ExpenseUseCases,
    private val app: Application
) : AndroidViewModel(app) {

    private val _state = MutableStateFlow(ExpenseReportState())
    val state: StateFlow<ExpenseReportState> = _state.asStateFlow()

    init {
        loadReport()
    }

    fun loadReport() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            useCases.getAllExpenses().collect { expenses ->
                val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                val last7Days = (0..6).map { daysAgo ->
                    val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -daysAgo) }
                    dateFormat.format(cal.time)
                }.reversed()

                val dailyTotals = last7Days.associateWith { date ->
                    expenses.filter {
                        dateFormat.format(Date(it.timestamp)) == date
                    }.sumOf { it.amount }
                }

                val categoryTotals = expenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                _state.update {
                    it.copy(
                        dailyTotals = dailyTotals,
                        categoryTotals = categoryTotals,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun exportReport(format: String) {
        viewModelScope.launch {
            try {
                val fileContent = useCases.exportExpenses(format)
                val fileName = "expense_report_${System.currentTimeMillis()}.$format"
                val file = File(app.getExternalFilesDir(null), fileName)

                FileOutputStream(file).use { fos ->
                    fos.write(fileContent.toByteArray())
                }

                shareFile(file, format)
                _state.update { it.copy(exportSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(exportSuccess = false) }
            }
        }
    }

    private fun shareFile(file: File, format: String) {
        val uri: Uri = FileProvider.getUriForFile(
            app,
            "${app.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = if (format == "csv") "text/csv" else "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Share Expense Report")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(chooser)
    }
}
