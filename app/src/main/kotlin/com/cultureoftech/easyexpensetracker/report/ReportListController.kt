package com.cultureoftech.easyexpensetracker.report

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.MyApplication
import com.cultureoftech.easyexpensetracker.controllers.ScopedController
import com.cultureoftech.easyexpensetracker.dagger.ForReportList
import com.cultureoftech.easyexpensetracker.dao.ExpenseReport
import com.cultureoftech.easyexpensetracker.expense.detail.DetailController
import com.cultureoftech.easyexpensetracker.report.detail.ReportDetailController
import com.cultureoftech.easyexpensetracker.ui.RecyclerSwipeListener
import com.cultureoftech.easyexpensetracker.ui.VerticalSpaceDecoration
import com.cultureoftech.easyexpensetracker.utils.pushNormal
import com.github.clans.fab.FloatingActionMenu
import javax.inject.Inject


class ReportListController(
        val bundle: Bundle
) : ScopedController(), ReportListView, ReportAdapter.ReportClickedListener {

    constructor() : this(Bundle())

    lateinit var component: ReportListComponent
    @Inject lateinit var presenter: ReportListPresenter
    @Inject lateinit var swipeListener: RecyclerSwipeListener

    @BindView(R.id.tv_running_total) lateinit var runningTotalText: TextView
    @BindView(R.id.fab_menu) lateinit var fabMenu: FloatingActionMenu
    @BindView(R.id.rv_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.controller_list_root) lateinit var parentLayout: CoordinatorLayout

    var itemTouchHelper: ItemTouchHelper? = null

    init {
        setHasOptionsMenu(true)
    }

    override fun onViewBound(view: View) {
        recyclerView.addItemDecoration(VerticalSpaceDecoration(view.context.resources.getDimension(R.dimen.recycler_view_item_spacing)))
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        component.inject(this)
        presenter.attach(this)
        itemTouchHelper = ItemTouchHelper(swipeListener)
    }

    override fun setReports(reports: List<ExpenseReport>) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = ReportAdapter(reports, this)
            itemTouchHelper?.attachToRecyclerView(recyclerView)
        } else {
            recyclerView.adapter.notifyDataSetChanged()
        }
    }

    override fun reportRemoved() {
        recyclerView.adapter?.notifyDataSetChanged()
        val undoSnackbar = Snackbar.make(view, R.string.report_removed, Snackbar.LENGTH_LONG)
        undoSnackbar.setAction(R.string.undo, {
            presenter.undoLastRemovedReport()
        })
        undoSnackbar.show()
    }

    override fun reportClicked(report: ExpenseReport) {
        val bundle = Bundle()
        bundle.putLong("reportId", report.id)
        router.pushNormal(ReportDetailController(bundle))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.menu_report_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_expense_list -> {
                goToExpenseList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.fab_menu_item_expense) fun addExpense() {
        fabMenu.close(true)
        router.pushNormal(DetailController())
    }

    @OnClick(R.id.fab_menu_item_report) fun addReport() {
        fabMenu.close(true)
        router.pushNormal(ReportDetailController())
    }

    private fun goToExpenseList() {
        router.popToRoot()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_list, container, false)
    }

    override fun initComponent(): Any {
        component = (router.activity.application as MyApplication).component.plus(ReportListModule())
        return component
    }

    override fun getTitle(): String? {
        return view.context.getString(R.string.report_list)
    }

    override fun getScope(): String {
        return ForReportList::class.java.simpleName
    }
}