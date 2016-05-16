package com.cultureoftech.easyexpensetracker.report

import com.cultureoftech.easyexpensetracker.dagger.ForReportList
import com.cultureoftech.easyexpensetracker.ui.ListPresenter
import dagger.Module
import dagger.Provides

@Module
class ReportListModule {

    @Provides @ForReportList fun provideListPresenter(presenter: ReportListPresenter): ListPresenter {
        return presenter
    }
}


