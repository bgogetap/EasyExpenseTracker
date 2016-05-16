package com.cultureoftech.easyexpensetracker.report.detail

import com.cultureoftech.easyexpensetracker.dagger.ForReportDetail
import dagger.Subcomponent

@ForReportDetail
@Subcomponent(modules = arrayOf(ReportDetailModule::class))
interface ReportDetailComponent {

    fun inject(reportDetailController: ReportDetailController)
}