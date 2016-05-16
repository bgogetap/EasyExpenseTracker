package com.cultureoftech.easyexpensetracker.model

import android.content.Context
import com.cultureoftech.easyexpensetracker.base.DEFAULT_EXPENSE_REPORT
import com.cultureoftech.easyexpensetracker.base.DEFAULT_PAYEE
import com.cultureoftech.easyexpensetracker.dao.*
import rx.Observable
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataHandler @Inject constructor(val context: Context, val daoSession: DaoSession) {

    init {
        // Set up defaults
        if (getPayees().size == 0) {
            savePayee(Payee(null, DEFAULT_PAYEE, null))
        }
        if (getExpenseReports().size == 0) {
            saveExpenseReport(ExpenseReport(null, DEFAULT_EXPENSE_REPORT, null, null, null))
        }
    }

    fun saveExpense(
            title: String = "",
            amount: Double,
            payeeName: String = DEFAULT_PAYEE,
            comment: String = "",
            report: String = DEFAULT_EXPENSE_REPORT,
            imagePath: String = ""
    ): Long {
        val expense = Expense.Builder(amount)
                .title(title)
                .comments(comment)
                .date(Date())
                .payee(getPayeeIdForName(payeeName))
                .expenseReport(getExpenseReportIdForName(report))
                .filePath(imagePath)
                .build()

        return daoSession.expenseDao.insert(expense)
    }

    fun saveExpense(expense: Expense) {
        when (expense.id) {
            null -> daoSession.expenseDao.insert(expense)
            else -> daoSession.expenseDao.update(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        daoSession.expenseDao.update(expense)
    }

    fun deleteExpense(expense: Expense) {
        Timber.d("Deleted expense ${expense.title}")
        daoSession.expenseDao.delete(expense)
    }

    fun savePayee(payee: Payee) {
        daoSession.payeeDao.insert(payee)
    }

    fun deletePayee(payee: Payee) {

    }

    /**
     * Returns all Expenses
     */
    fun getExpenses(): List<Expense> {
        return daoSession.expenseDao.queryBuilder().list()
    }

    /**
     * Returns expenses for a given ExpenseReport
     */
    fun getExpenses(reportId: Long): List<Expense> {
        val report = daoSession.expenseReportDao.queryBuilder()
                .where(ExpenseReportDao.Properties.Id.eq(reportId))
                .uniqueOrThrow()
        if (report != null) {
            return report.expenses
        }
        return ArrayList()
    }

    fun getExpenseForId(expenseId: Long): Expense {
        return daoSession.expenseDao.queryBuilder().where(ExpenseDao.Properties.Id.eq(expenseId)).unique()
    }

    fun getPayees(): List<Payee> {
        return daoSession.payeeDao.queryBuilder().list()
    }

    fun saveExpenseReport(expenseReport: ExpenseReport): Long {
        when (expenseReport.id) {
            null -> return daoSession.expenseReportDao.insert(expenseReport)
            else -> {
                daoSession.expenseReportDao.update(expenseReport)
                return expenseReport.id
            }
        }
    }

    fun getExpenseReportForId(id: Long): ExpenseReport? {
        return daoSession.expenseReportDao.queryBuilder()
                .where(ExpenseReportDao.Properties.Id.eq(id))
                .unique()
    }

    fun getExpenseReportIdForName(name: String): Long {
        var report = daoSession.expenseReportDao.queryBuilder()
                .where(ExpenseReportDao.Properties.Title.eq(name))
                .unique()
        if (report == null) {
            report = ExpenseReport(null, name, Date(), null, null)
            return daoSession.expenseReportDao.insert(report)
        }
        return report.id
    }

    fun getExpenseReports(): List<ExpenseReport> {
        return daoSession.expenseReportDao.queryBuilder().list()
    }

    fun getPayeeIdForName(name: String): Long {
        var payee = daoSession.payeeDao.queryBuilder()
                .where(PayeeDao.Properties.Name.eq(name))
                .unique()
        if (payee == null) {
            payee = Payee.create(name, null)
            return daoSession.payeeDao.insert(payee)
        }
        return payee.id
    }

    fun removePhotoFromExpense(expense: Expense): Observable<Unit?> {
        return Observable.fromCallable {
            expense.imagePath?.let {
                val imageFile = File(expense.imagePath)
                imageFile.delete()
                expense.imagePath = null
                daoSession.expenseDao.update(expense)
            }
        }.subscribeOn(Schedulers.io())
    }

    fun attachExpensesToReport(expenseIds: LongArray, reportId: Long) {
        expenseIds.forEach {
            val expense = getExpenseForId(it)
            expense.reportId = reportId
            saveExpense(expense)
        }
    }

    fun deleteReport(report: ExpenseReport) {
        report.expenses.forEach {
            it.delete()
        }
        report.delete()
    }
}
