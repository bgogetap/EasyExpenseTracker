package com.cultureoftech.easyexpensetracker.expense.selectReport

import com.cultureoftech.easyexpensetracker.dagger.ForSelectReport
import dagger.Subcomponent

@ForSelectReport
@Subcomponent(modules = arrayOf(SelectReportModule::class))
interface SelectReportComponent {

    fun inject(controller: SelectReportController)
}