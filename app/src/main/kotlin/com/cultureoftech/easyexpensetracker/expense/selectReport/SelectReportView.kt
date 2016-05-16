package com.cultureoftech.easyexpensetracker.expense.selectReport

interface SelectReportView {

    fun setReports(reports: Array<String>?)

    fun reportChosen(reportId: Long)
}