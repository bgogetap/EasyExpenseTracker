package com.cultureoftech.easyexpensetracker.expense

import com.cultureoftech.easyexpensetracker.dagger.ForExpenseList
import com.cultureoftech.easyexpensetracker.ui.ListPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ExpenseListModule(val reportId: Long) {

    @Provides @Named("reportId") fun provideReportId(): Long {
        return reportId
    }

    @Provides @ForExpenseList fun provideListPresenter(presenter: ExpenseListPresenter): ListPresenter {
        return presenter
    }
}