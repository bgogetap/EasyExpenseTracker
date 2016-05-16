package com.cultureoftech.easyexpensetracker.report

import com.cultureoftech.easyexpensetracker.dagger.ForReportList
import com.cultureoftech.easyexpensetracker.report.detail.ReportDetailComponent
import com.cultureoftech.easyexpensetracker.report.detail.ReportDetailModule
import dagger.Subcomponent

@ForReportList
@Subcomponent(modules = arrayOf(ReportListModule::class))
interface ReportListComponent {

    fun inject(reportListController: ReportListController)
}