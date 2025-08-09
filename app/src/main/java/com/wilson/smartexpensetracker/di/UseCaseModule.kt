package com.wilson.smartexpensetracker.di

import com.wilson.smartexpensetracker.data.util.CsvExporter
import com.wilson.smartexpensetracker.data.util.PdfExporter
import com.wilson.smartexpensetracker.data.util.SyncManager
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository
import com.wilson.smartexpensetracker.domain.usecase.AddExpenseUseCase
import com.wilson.smartexpensetracker.domain.usecase.ClearAllExpensesUseCase
import com.wilson.smartexpensetracker.domain.usecase.DeleteExpenseUseCase
import com.wilson.smartexpensetracker.domain.usecase.ExportExpensesUseCase
import com.wilson.smartexpensetracker.domain.usecase.GetAllExpensesUseCase
import com.wilson.smartexpensetracker.domain.usecase.GetExpenseByIdUseCase
import com.wilson.smartexpensetracker.domain.usecase.GetExpensesByDateUseCase
import com.wilson.smartexpensetracker.domain.usecase.UpdateExpenseUseCase
import com.wilson.smartexpensetracker.domain.usecase.ExpenseUseCases
import com.wilson.smartexpensetracker.domain.usecase.SyncExpensesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideExpenseUseCases(
        repository: ExpenseRepository,
        csvExporter: CsvExporter,
        pdfExporter: PdfExporter,
        syncManager: SyncManager
    ): ExpenseUseCases {
        return ExpenseUseCases(
            addExpense = AddExpenseUseCase(repository),
            getExpensesByDate = GetExpensesByDateUseCase(repository),
            getAllExpenses = GetAllExpensesUseCase(repository),
            getExpenseById = GetExpenseByIdUseCase(repository),
            updateExpense = UpdateExpenseUseCase(repository),
            deleteExpense = DeleteExpenseUseCase(repository),
            clearAllExpenses = ClearAllExpensesUseCase(repository),
            exportExpenses = ExportExpensesUseCase(repository, csvExporter, pdfExporter),
            syncExpenses = SyncExpensesUseCase(repository, syncManager)
        )
    }
}
