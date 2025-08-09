package com.wilson.smartexpensetracker.data.repository

import com.wilson.smartexpensetracker.data.local.dao.ExpenseDao
import com.wilson.smartexpensetracker.data.mapper.toDomain
import com.wilson.smartexpensetracker.data.mapper.toEntity
import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }

    override fun getTodayExpenses(): Flow<List<Expense>> {
        return expenseDao.getTodayExpenses().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getExpensesByDate(epochMillis: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesByDate(epochMillis).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)?.toDomain()
    }

    override suspend fun clearAll() {
        expenseDao.clearAll()
    }
}
