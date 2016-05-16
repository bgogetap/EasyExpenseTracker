package com.cultureoftech.easyexpensetracker.report

import com.cultureoftech.easyexpensetracker.dao.ExpenseReport


data class DeletedReportHolder(val report: ExpenseReport, val position: Int)