package com.wilson.smartexpensetracker.data.mapper

import com.wilson.smartexpensetracker.data.local.entity.ExpenseEntity
import com.wilson.smartexpensetracker.domain.model.Expense

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        notes = notes,
        receiptPath = receiptPath,
        timestamp = timestamp,
        isSynced = isSynced
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        notes = notes,
        receiptPath = receiptPath,
        timestamp = timestamp,
        isSynced = isSynced
    )
}
