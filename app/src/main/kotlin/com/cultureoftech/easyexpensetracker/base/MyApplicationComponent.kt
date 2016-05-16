package com.cultureoftech.easyexpensetracker.base

import com.cultureoftech.easyexpensetracker.expense.ExpenseListComponent
import com.cultureoftech.easyexpensetracker.expense.ExpenseListModule
import com.cultureoftech.easyexpensetracker.expense.detail.DetailComponent
import com.cultureoftech.easyexpensetracker.expense.detail.DetailModule
import com.cultureoftech.easyexpensetracker.expense.selectReport.SelectReportComponent
import com.cultureoftech.easyexpensetracker.expense.selectReport.SelectReportModule
import com.cultureoftech.easyexpensetracker.image.viewer.ImageViewerComponent
import com.cultureoftech.easyexpensetracker.image.viewer.ImageViewerModule
import com.cultureoftech.easyexpensetracker.report.ReportListComponent
import com.cultureoftech.easyexpensetracker.report.ReportListModule
import com.cultureoftech.easyexpensetracker.report.detail.ReportDetailComponent
import com.cultureoftech.easyexpensetracker.report.detail.ReportDetailModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(MyApplicationModule::class))
interface MyApplicationComponent {

    fun plus(listModule: ExpenseListModule): ExpenseListComponent

    fun plus(listModule: ReportListModule): ReportListComponent

    fun plus(reportDetailModule: ReportDetailModule): ReportDetailComponent

    fun plus(selectReportModule: SelectReportModule): SelectReportComponent

    fun plus(imageViewerModule: ImageViewerModule): ImageViewerComponent

    fun plus(detailModule: DetailModule): DetailComponent

    fun inject(myApplication: MyApplication)
}