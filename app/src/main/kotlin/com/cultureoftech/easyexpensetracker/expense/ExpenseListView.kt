package com.cultureoftech.easyexpensetracker.expense

import com.cultureoftech.easyexpensetracker.dao.Expense


interface ExpenseListView {

    fun setData(expenses: List<Expense>, title: String?, mode: ExpenseListPresenter.ListMode)

    fun expenseRemoved()

    fun setRunningTotal(total: Double)

    fun switchMode(mode: ExpenseListPresenter.ListMode)

    fun selectionUpdated()

    fun goToDetails(expense: Expense)

    fun showSelectReportController()

    fun expensesAttached()

    fun goToReportDetails(reportId: Long = 0L, attachExpenses: LongArray = LongArray(0))
}