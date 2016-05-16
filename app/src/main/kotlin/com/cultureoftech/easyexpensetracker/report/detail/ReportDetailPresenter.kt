package com.cultureoftech.easyexpensetracker.report.detail

import com.cultureoftech.easyexpensetracker.base.Presenter
import com.cultureoftech.easyexpensetracker.dagger.ForReportDetail
import com.cultureoftech.easyexpensetracker.dao.ExpenseReport
import com.cultureoftech.easyexpensetracker.model.DataHandler
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ForReportDetail
class ReportDetailPresenter @Inject constructor(
        val dataHandler: DataHandler,
        val report: ExpenseReport,
        @Named("addedExpenses") val addedExpenses: LongArray
) : Presenter<ReportDetailView>() {

    var expensesAttached: Boolean = false

    init {
        if (addedExpenses.size != 0 && report.id != null) {
            attachExpensesToReport(report.id)
        }
    }
    override fun viewAttached() {
        view?.setData(report, addedExpenses.size > 0 || report.id == null)
    }

    private fun attachExpensesToReport(reportId: Long) {
        if (addedExpenses.size > 0) {
            Observable.fromCallable {
                dataHandler.attachExpensesToReport(addedExpenses, reportId)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.expensesAttached()
                        expensesAttached = true
                    }, { Timber.e("Error attaching expenses to report", it) })
        }
    }

    fun updateDateClicked() {
        if (report.dateSubmitted == null) {
            view?.showDatePicker(report.dateSubmitted)
        } else {
            report.dateSubmitted = null
            view?.dateUpdated(null)
        }
    }

    fun submittedDateSelected(time: Date?) {
        report.dateSubmitted = time
        view?.dateUpdated(time)
    }

    fun save(title: String, comments: String) {
        Observable.fromCallable {
            report.title = title
            report.comments = comments
            val id = dataHandler.saveExpenseReport(report)
            if (!expensesAttached) {
                attachExpensesToReport(id)
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.reportSaved() }, { Timber.e("Error saving Report", it) })
    }
}