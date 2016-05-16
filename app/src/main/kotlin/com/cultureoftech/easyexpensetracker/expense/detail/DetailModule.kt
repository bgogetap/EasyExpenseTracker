package com.cultureoftech.easyexpensetracker.expense.detail

import com.cultureoftech.easyexpensetracker.dagger.ForDetail
import com.cultureoftech.easyexpensetracker.dao.DaoSession
import com.cultureoftech.easyexpensetracker.dao.Expense
import com.cultureoftech.easyexpensetracker.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DetailModule(val expenseId: Long, val reportId: Long) {

    @Provides @ForDetail fun provideExpense(daoSession: DaoSession): Expense {
        val expense = daoSession.expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Id.eq(expenseId))
                .unique()
        return expense ?: Expense.Builder(0.0).build()
    }

    @Provides @Named("reportId") fun provideReportId(): Long {
        return if (reportId == 0L) 1L else reportId
    }
}