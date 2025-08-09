package com.wilson.smartexpensetracker.presentation.navigation

import androidx.compose.runtime.Composable
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
            ExpenseListScreen(
                onNavigateToReport = {
                    navController.safeNavigate(NavRoutes.ExpenseReport.route)
                },
                viewModel
            )
        }

        composable(NavRoutes.ExpenseEntry.route) {
            val viewModel: ExpenseEntryViewModel = hiltViewModel()
            ExpenseEntryScreen(
                onExpenseSaved = {
                    viewModel.saveExpense()
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
