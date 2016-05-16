package com.cultureoftech.easyexpensetracker.report.detail

import com.cultureoftech.easyexpensetracker.dao.ExpenseReport
import java.util.*

interface ReportDetailView {

    fun setData(report: ExpenseReport, hideButtons: Boolean)

    fun showDatePicker(date: Date?)

    fun dateUpdated(date: Date?)

    fun reportSaved()

    fun expensesAttached()
}