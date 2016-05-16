package com.cultureoftech.easyexpensetracker.expense.detail

import android.content.Intent
import com.cultureoftech.easyexpensetracker.base.Presenter
import com.cultureoftech.easyexpensetracker.dagger.ForDetail
import com.cultureoftech.easyexpensetracker.dao.Expense
import com.cultureoftech.easyexpensetracker.model.DataHandler
import com.cultureoftech.easyexpensetracker.model.ImageHelper
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@ForDetail
class DetailPresenter @Inject constructor(
        val expense: Expense,
        val imageHelper: ImageHelper,
        val dataHandler: DataHandler,
        @Named("reportId") var reportId: Long
) : Presenter<DetailView>() {

    var reportTitle: String? = null
    var selectedReportPosition: Int = -1

    init {
        val reportIdToCheck = if (reportId == 0L) expense.reportId else reportId
        reportTitle = dataHandler.getExpenseReportForId(reportIdToCheck)?.title
    }

    override fun viewAttached() {
        val reports = dataHandler.getExpenseReports()
        val reportArray = Array(reports.size, { reports[it].title })
        val payees = dataHandler.getPayees()
        val payeeArray = Array(payees.size, { i -> payees[i].name })
        view?.setData(expense, payeeArray, reportArray, reportTitle)
    }

    fun photoTaken(data: Intent?) {
        expense.imagePath = imageHelper.lastGeneratedUri.path
        view?.photoUpdated(expense.imagePath)
    }

    fun photoChosen(data: Intent?) {
        data?.data?.let {
            imageHelper.copyImageToCacheObservable(it)
                    .subscribe({
                        expense.imagePath = it
                        view?.photoUpdated(expense.imagePath)
                    }, { Timber.e("Error copying chosen image to app directory", it) })
        }
    }

    fun saveExpense(title: String, amount: Double, comments: String, payee: String, report: String) {
        Observable.fromCallable {
            expense.title = title
            expense.amount = amount
            expense.comments = comments
            expense.payeeId = dataHandler.getPayeeIdForName(payee)
            expense.reportId = dataHandler.getExpenseReportIdForName(report)
            dataHandler.saveExpense(expense)
        }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.expenseSaved() }, { Timber.e("Error saving expense", it) })
    }

    fun deletePhoto() {
        dataHandler.removePhotoFromExpense(expense).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {   expense.imagePath = null
                            view?.photoUpdated(expense.imagePath) },
                        { Timber.e("Error deleting photo from Expense", it) });
    }

    fun getImagePath(): String? {
        return expense.imagePath
    }
}