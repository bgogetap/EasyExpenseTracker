package com.cultureoftech.easyexpensetracker.report

import com.cultureoftech.easyexpensetracker.dao.ExpenseReport

interface ReportListView {

    fun setReports(reports: List<ExpenseReport>)

    fun reportRemoved()
}