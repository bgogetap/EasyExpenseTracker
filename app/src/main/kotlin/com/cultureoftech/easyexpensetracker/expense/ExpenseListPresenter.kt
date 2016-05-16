package com.cultureoftech.easyexpensetracker.expense

import android.content.Context
import android.util.SparseArray
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.Presenter
import com.cultureoftech.easyexpensetracker.dagger.ForExpenseList
import com.cultureoftech.easyexpensetracker.dao.Expense
import com.cultureoftech.easyexpensetracker.model.DataHandler
import com.cultureoftech.easyexpensetracker.ui.ListPresenter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@ForExpenseList
class ExpenseListPresenter @Inject constructor(
        val dataHandler: DataHandler,
        val context: Context,
        @Named("reportId") val reportId: Long
) : Presenter<ExpenseListView>(), ListPresenter {

    private var expenses: MutableList<Expense> = ArrayList()
    private var deletedExpenseHolder: DeletedExpenseHolder? = null
    private var title: String? = null
    private var mode = ListMode.NORMAL
    private var selectedExpenses: MutableList<Long>? = null

    var selectionHolder: SparseArray<Boolean> = SparseArray()
        private set

    private val deleteExpenseMap: MutableMap<Expense, Subscription> = LinkedHashMap()

    enum class ListMode {
        NORMAL,
        ASSIGN_REPORT
    }

    override fun viewAttached() {
        refreshExpenses()
    }

    private fun dataLoaded() {
        view?.setData(expenses, title, mode)
        recalculateTotal()
    }

    private fun recalculateTotal() {
        var total = 0.0
        expenses.forEach {
            total += it.amount
        }
        view?.setRunningTotal(total)
    }

    fun removeExpense(position: Int) {
        expenses.get(position)?.let {
            deletedExpenseHolder = DeletedExpenseHolder(it, position)
            expenses.remove(it)
            view?.expenseRemoved()
            recalculateTotal()
            deleteExpenseMap.put(it, deleteObservable(it))
        }
    }

    override fun itemSwiped(position: Int) {
        removeExpense(position)
    }

    private fun deleteObservable(expense: Expense): Subscription {
        return Observable.interval(5, TimeUnit.SECONDS).subscribe {
            dataHandler.deleteExpense(expense)
            deleteExpenseMap[expense]?.unsubscribe()
            deleteExpenseMap.remove(expense)
        }
    }

    fun undoLastRemovedExpense() {
        deletedExpenseHolder?.expense?.let {
            deleteExpenseMap[it]?.unsubscribe()
            deleteExpenseMap.remove(it)
            expenses.add(deletedExpenseHolder!!.position, it)
            dataLoaded()
            deletedExpenseHolder = null
        }
    }

    fun refreshExpenses() {
        expenses.clear()
        when (reportId) {
            0L -> expenses.addAll(dataHandler.getExpenses())
            else -> {
                val report = dataHandler.getExpenseReportForId(reportId)
                report?.resetExpenses()
                expenses.addAll(report?.expenses!!)
                title = context.getString(R.string.report_expenses_format, report?.title)
            }
        }
        dataLoaded()
    }

    fun forReport(): Boolean {
        return reportId > 0
    }

    fun getTitle(): String? {
        return title
    }

    fun goToSelectMode() {
        toggleAssignReportMode(ListMode.ASSIGN_REPORT)
    }

    fun toggleAssignReportMode(mode: ListMode) {
        this.mode = mode
        view?.switchMode(mode)
    }

    fun inSelectMode(): Boolean {
        return mode == ListMode.ASSIGN_REPORT
    }

    fun expenseLongClicked(position: Int) {
        if (mode == ListMode.ASSIGN_REPORT) {
            updateSelection(position)
        } else {
            goToSelectMode()
        }
    }

    fun expenseClicked(position: Int) {
        if (mode == ListMode.ASSIGN_REPORT) {
            updateSelection(position)
        } else {
            view?.goToDetails(expenses[position])
        }
    }

    private fun updateSelection(position: Int) {
        val current = selectionHolder.get(position, false)
        selectionHolder.put(position, !current)
        view?.selectionUpdated()
    }

    fun doneWithSelection() {
        var i = 0
        selectedExpenses = ArrayList<Long>()
        while (i < selectionHolder.size()) {
            val position = selectionHolder.keyAt(i)
            val selected = selectionHolder.get(position)
            if (selected) {
                val expense = expenses[position]
                selectedExpenses!!.add(expense.id)
            }
            i++
        }
        selectionHolder.clear()
        view?.showSelectReportController()
        mode = ListMode.NORMAL
        view?.switchMode(mode)
    }

    fun cancelSelection() {
        selectionHolder.clear()
        selectedExpenses = null
        mode = ListMode.NORMAL
        view?.switchMode(mode)
    }

    fun attachExpensesToReport(reportId: Long) {
        val expenseArray = LongArray(selectedExpenses!!.size, { selectedExpenses!![it]})
        if (reportId != 0L) {
            Observable.fromCallable {
                dataHandler.attachExpensesToReport(expenseArray, reportId)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.expensesAttached()
                        selectedExpenses = null
                    }, { Timber.e("Error attaching expenses to report", it)})
        } else {
            view?.goToReportDetails(reportId, expenseArray)
            selectedExpenses = null
        }
    }

    fun handleBack(): Boolean {
        if (mode == ListMode.ASSIGN_REPORT) {
            cancelSelection()
            return true
        }
        return false
    }
}