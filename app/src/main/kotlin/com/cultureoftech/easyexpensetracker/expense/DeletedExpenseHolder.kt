package com.cultureoftech.easyexpensetracker.expense

import com.cultureoftech.easyexpensetracker.dao.Expense

data class DeletedExpenseHolder(val expense: Expense, val position: Int)