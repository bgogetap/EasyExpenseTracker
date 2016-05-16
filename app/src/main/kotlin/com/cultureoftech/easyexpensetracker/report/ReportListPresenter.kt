package com.cultureoftech.easyexpensetracker.report

import com.cultureoftech.easyexpensetracker.base.Presenter
import com.cultureoftech.easyexpensetracker.dagger.ForReportList
import com.cultureoftech.easyexpensetracker.dao.ExpenseReport
import com.cultureoftech.easyexpensetracker.model.DataHandler
import com.cultureoftech.easyexpensetracker.ui.ListPresenter
import rx.Observable
import rx.Subscription
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ForReportList
class ReportListPresenter @Inject constructor(
        val dataHandler: DataHandler
) : Presenter<ReportListView>(), ListPresenter {

    var reports: MutableList<ExpenseReport> = ArrayList()
    var deletedReportHolder: DeletedReportHolder? = null

    private val deleteReportMap: MutableMap<ExpenseReport, Subscription> = LinkedHashMap()

    override fun viewAttached() {
        reports.clear()
        reports.addAll(dataHandler.getExpenseReports())
        setData()
    }

    private fun setData() {
        view?.setReports(reports)
    }

    private fun deleteObservable(report: ExpenseReport): Subscription {
        return Observable.interval(5, TimeUnit.SECONDS).subscribe {
            dataHandler.deleteReport(report)
            deleteReportMap[report]?.unsubscribe()
            deleteReportMap.remove(report)
        }
    }

    fun undoLastRemovedReport() {
        deletedReportHolder?.report?.let {
            deleteReportMap[it]?.unsubscribe()
            deleteReportMap.remove(it)
            reports.add(deletedReportHolder!!.position, it)
            setData()
            deletedReportHolder = null
        }
    }

    fun removeReport(adapterPosition: Int) {
        reports.get(adapterPosition)?.let {
            deletedReportHolder = DeletedReportHolder(it, adapterPosition)
            reports.remove(it)
            view?.reportRemoved()
            deleteReportMap.put(it, deleteObservable(it))
        }
    }

    fun reportClicked(report: ExpenseReport) {

    }

    override fun itemSwiped(position: Int) {
        removeReport(position)
    }
}