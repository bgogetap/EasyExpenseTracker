package com.cultureoftech.easyexpensetracker.expense

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
import com.bluelinelabs.conductor.ChildControllerTransaction
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.MyApplication
import com.cultureoftech.easyexpensetracker.controllers.ScopedController
import com.cultureoftech.easyexpensetracker.dagger.ForExpenseList
import com.cultureoftech.easyexpensetracker.dao.Expense
import com.cultureoftech.easyexpensetracker.expense.detail.DetailController
import com.cultureoftech.easyexpensetracker.expense.selectReport.SelectReportController
import com.cultureoftech.easyexpensetracker.report.ReportListController
import com.cultureoftech.easyexpensetracker.report.detail.ReportDetailController
import com.cultureoftech.easyexpensetracker.ui.RecyclerSwipeListener
import com.cultureoftech.easyexpensetracker.ui.VerticalSpaceDecoration
import com.cultureoftech.easyexpensetracker.utils.pushNormal
import com.cultureoftech.easyexpensetracker.utils.toCurrency
import com.github.clans.fab.FloatingActionMenu
import javax.inject.Inject

class ExpenseListController(
        val bundle: Bundle
) : ScopedController(), ExpenseListView, ExpenseAdapter.ExpenseClickedListener {


    constructor() : this(Bundle())

    @BindView(R.id.tv_running_total) lateinit var runningTotalText: TextView
    @BindView(R.id.rv_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.fab_menu) lateinit var fabMenu: FloatingActionMenu
    @BindView(R.id.controller_list_root) lateinit var parentLayout: CoordinatorLayout
    @BindView(R.id.ll_header_container) lateinit var headerContainer: View
    @BindView(R.id.ll_selection_mode_container) lateinit var selectionModeContainer: View

    @Inject lateinit var presenter: ExpenseListPresenter
    @Inject lateinit var swipeListener: RecyclerSwipeListener

    private var selectReportController: SelectReportController? = null

    lateinit var component: ExpenseListComponent

    var itemTouchHelper: ItemTouchHelper? = null

    init {
        setHasOptionsMenu(true)
    }

    private fun goToReportList() {
        router.pushController(RouterTransaction.builder(ReportListController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler())
                .build())
    }

    override fun onViewBound(view: View) {
        recyclerView.addItemDecoration(VerticalSpaceDecoration(view.context.resources.getDimension(R.dimen.recycler_view_item_spacing)))
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        component.inject(this)
        presenter.attach(this)
        itemTouchHelper = ItemTouchHelper(swipeListener)
    }

    override fun setData(expenses: List<Expense>, title: String?, mode: ExpenseListPresenter.ListMode) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = ExpenseAdapter(expenses, this, mode, presenter.selectionHolder)
            itemTouchHelper?.attachToRecyclerView(recyclerView)
        } else {
            recyclerView.adapter.notifyDataSetChanged()
        }
        updateTitle(title)
    }

    override fun expenseClicked(position: Int) {
        presenter.expenseClicked(position)
    }

    override fun expenseLongClicked(position: Int) {
        presenter.expenseLongClicked(position)
    }

    override fun expenseRemoved() {
        recyclerView.adapter?.notifyDataSetChanged()
        val undoSnackbar = Snackbar.make(view, R.string.expense_removed, Snackbar.LENGTH_LONG)
        undoSnackbar.setAction(R.string.undo, {
            presenter.undoLastRemovedExpense()
        })
        undoSnackbar.show()
    }

    override fun setRunningTotal(total: Double) {
        runningTotalText.text = total.toCurrency()
    }

    override fun switchMode(mode: ExpenseListPresenter.ListMode) {
        val selectionMode = mode == ExpenseListPresenter.ListMode.ASSIGN_REPORT
        headerContainer.visibility = if (selectionMode) View.GONE else View.VISIBLE
        selectionModeContainer.visibility = if (selectionMode) View.VISIBLE else View.GONE
        (recyclerView.adapter as ExpenseAdapter).mode = mode
    }

    override fun selectionUpdated() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun goToDetails(expense: Expense) {
        goToDetails(expense.id, 0)
    }

    @OnClick(R.id.fab_menu_item_expense) fun addExpense() {
        fabMenu.close(true)
        goToDetails(0, presenter.reportId)
    }

    @OnClick(R.id.fab_menu_item_report) fun addReport() {
        fabMenu.close(true)
        goToReportDetails()
    }

    @OnClick(R.id.btn_done) fun doneWithSelection() {
        presenter.doneWithSelection()
    }

    @OnClick(R.id.btn_cancel) fun cancelSelection() {
        presenter.cancelSelection()
    }

    fun goToDetails(expenseId: Long, reportId: Long) {
        val bundle = Bundle()
        bundle.putLong("expenseId", expenseId)
        bundle.putLong("reportId", reportId)
        router.pushNormal(DetailController(bundle))
    }

    override fun showSelectReportController() {
        addChildController(ChildControllerTransaction.builder(getSelectReportController(), R.id.controller_list_root)
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler())
                .addToLocalBackstack(true)
                .build())
    }

    private fun getSelectReportController(): SelectReportController {
        selectReportController = SelectReportController()
        return selectReportController!!
    }

    override fun expensesAttached() {
        removeChildController(selectReportController)
        selectReportController = null
        Snackbar.make(view, R.string.expenses_attached, Snackbar.LENGTH_LONG).show()
        presenter.refreshExpenses()
    }

    override fun goToReportDetails(reportId: Long, attachExpenses: LongArray) {
        if (selectReportController != null) {
            removeChildController(selectReportController)
            selectReportController = null
        }
        val bundle = Bundle()
        bundle.putLong("reportId", reportId)
        bundle.putLongArray("expenseIds", attachExpenses)
        router.pushNormal(ReportDetailController(bundle))
    }

    fun attachExpensesToReport(reportId: Long) {
        presenter.attachExpensesToReport(reportId)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuResource = if (presenter.forReport())
            R.menu.menu_report_expenses else R.menu.menu_expense_list
        return inflater.inflate(menuResource, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menu_action_assign_report -> {
                presenter.goToSelectMode()
                return true
            }
            R.id.menu_action_report_list -> {
                goToReportList()
                return true
            }
            R.id.menu_action_report_info -> {
                goToReportDetails(presenter.reportId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initComponent(): Any {
        val reportId = bundle.getLong("reportId", 0)
        component = (router.activity.application as MyApplication).component.plus(ExpenseListModule(reportId))
        return component
    }

    override fun onDestroyView(view: View?) {
        super.onDestroyView(view)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun getTitle(): String? {
        return presenter.getTitle() ?: view.context.getString(R.string.expense_list)
    }

    override fun getScope(): String {
        return ForExpenseList::class.java.name
    }

    override fun handleBack(): Boolean {
        return presenter.handleBack()
    }
}
