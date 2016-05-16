package com.cultureoftech.easyexpensetracker.report.detail

import com.cultureoftech.easyexpensetracker.dagger.ForReportDetail
import com.cultureoftech.easyexpensetracker.dao.ExpenseReport
import com.cultureoftech.easyexpensetracker.model.DataHandler
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import javax.inject.Named

@Module
class ReportDetailModule(
        val reportId: Long,
        val addedExpenses: LongArray
) {

    @Provides @ForReportDetail fun provideReport(dataHandler: DataHandler): ExpenseReport {
        when (reportId) {
            0L -> return ExpenseReport()
            else -> return dataHandler.getExpenseReportForId(reportId)!!
        }
    }

    @Provides @Named("addedExpenses") fun provideExpenseIds(): LongArray {
        return addedExpenses
    }

    @Provides @ForReportDetail fun provideDateFormat(): SimpleDateFormat {
        return SimpleDateFormat("EEE, MMM d, yyyy")
    }
}