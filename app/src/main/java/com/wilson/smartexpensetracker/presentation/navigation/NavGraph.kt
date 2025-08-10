package com.wilson.smartexpensetracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wilson.smartexpensetracker.presentation.screens.entry.ExpenseEntryScreen
import com.wilson.smartexpensetracker.presentation.screens.entry.ExpenseEntryViewModel
import com.wilson.smartexpensetracker.presentation.screens.list.ExpenseListScreen
import com.wilson.smartexpensetracker.presentation.screens.list.ExpenseListViewModel
import com.wilson.smartexpensetracker.presentation.screens.report.ExpenseReportScreen
import com.wilson.smartexpensetracker.presentation.screens.report.ExpenseReportViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = NavRoutes.ExpenseList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.ExpenseList.route) {
            val viewModel: ExpenseListViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            ExpenseListScreen(
                state = state,
                onAddExpenseClick = { navController.navigate(NavRoutes.ExpenseEntry.route) },
                onDeleteExpense = { expense ->
                    viewModel.deleteExpense(expense.id)
                },
                onViewReportClick = { navController.navigate(NavRoutes.ExpenseReport.route) },
                onReload = { dateMillis -> viewModel.loadExpensesForDate(dateMillis) }
            )
        }

        composable(NavRoutes.ExpenseEntry.route) {
            val viewModel: ExpenseEntryViewModel = hiltViewModel()
            ExpenseEntryScreen(
                onExpenseSaved = {
                    navController.popBackStack()
                },
                viewModel
            )
        }

        composable(NavRoutes.ExpenseReport.route) {
            val viewModel: ExpenseReportViewModel = hiltViewModel()
            ExpenseReportScreen(
                onBack = { navController.popBackStack() },
                viewModel
            )
        }
    }
}
