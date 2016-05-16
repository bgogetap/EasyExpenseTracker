package com.cultureoftech.easyexpensetracker.expense.detail

import com.cultureoftech.easyexpensetracker.dao.Expense

interface DetailView {

    fun setData(expense: Expense, payees: Array<String>, expenseReports: Array<String>, reportTitle: String?)

    fun photoUpdated(imagePath: String?)

    fun expenseSaved()
}