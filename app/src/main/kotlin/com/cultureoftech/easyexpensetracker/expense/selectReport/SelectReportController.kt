package com.cultureoftech.easyexpensetracker.expense.selectReport

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.MyApplication
import com.cultureoftech.easyexpensetracker.controllers.ModalController
import com.cultureoftech.easyexpensetracker.dagger.ForSelectReport
import com.cultureoftech.easyexpensetracker.expense.ExpenseListController
import javax.inject.Inject


class SelectReportController(val bundle: Bundle): ModalController(), SelectReportView, SelectReportAdapter.ReportSelectedListener {

    constructor(): this(Bundle())

    @Inject lateinit var presenter: SelectReportPresenter
    lateinit var component: SelectReportComponent

    @BindView(R.id.recycler_view) lateinit var recyclerView: RecyclerView

    override fun onViewBound(view: View) {
        component.inject(this)
        presenter.attach(this)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    override fun setReports(reports: Array<String>?) {
        if (reports != null) {
            val adapter = SelectReportAdapter(reports, this)
            recyclerView.adapter = adapter
        }
    }

    // Recycler Click Listener
    override fun reportSelected(position: Int) {
        presenter.reportSelected(position)
    }

    override fun reportChosen(reportId: Long) {
        (getParentController() as ExpenseListController).attachExpensesToReport(reportId)
    }

    override fun getContentView(): Int {
        return R.layout.controller_select_report
    }

    override fun getScope(): String {
        return ForSelectReport::class.java.name
    }

    override fun initComponent(): Any {
        component = (router.activity.application as MyApplication).component.plus(SelectReportModule())
        return component
    }
}