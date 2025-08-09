package com.wilson.smartexpensetracker.presentation.screens.report

data class ExpenseReportState(
    val dailyTotals: Map<String, Double> = emptyMap(), // date -> total
    val categoryTotals: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = false,
    val exportSuccess: Boolean? = null // null: no attempt, true: success, false: fail
)
