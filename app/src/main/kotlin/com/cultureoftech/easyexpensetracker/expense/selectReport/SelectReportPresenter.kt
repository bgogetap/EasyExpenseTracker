package com.cultureoftech.easyexpensetracker.expense.selectReport

import android.content.Context
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.Presenter
import com.cultureoftech.easyexpensetracker.model.DataHandler
import javax.inject.Inject


class SelectReportPresenter @Inject constructor(
        val dataHandler: DataHandler,
        val context: Context
): Presenter<SelectReportView>() {

    var reports: Array<String>? = null

    override fun viewAttached() {
        if (reports == null) {
            val currentReports = dataHandler.getExpenseReports()
            reports = Array(currentReports.size + 1, {
                if (it < currentReports.size) {
                    return@Array currentReports[it].title
                }
                return@Array context.getString(R.string.new_report)
            })

        }
        dataLoaded()
    }

    private fun dataLoaded() {
        view?.setReports(reports)
    }

    fun reportSelected(position: Int) {
        var reportId = 0L
        if (position != reports!!.size - 1) {
            reportId = dataHandler.getExpenseReportIdForName(reports!![position])
        }
        view?.reportChosen(reportId)
    }
}