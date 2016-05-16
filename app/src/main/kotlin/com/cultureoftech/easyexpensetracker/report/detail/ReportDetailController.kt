package com.cultureoftech.easyexpensetracker.report.detail

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.MyApplication
import com.cultureoftech.easyexpensetracker.controllers.ScopedController
import com.cultureoftech.easyexpensetracker.dagger.ForReportDetail
import com.cultureoftech.easyexpensetracker.dao.ExpenseReport
import com.cultureoftech.easyexpensetracker.expense.ExpenseListController
import com.cultureoftech.easyexpensetracker.expense.detail.DetailController
import com.cultureoftech.easyexpensetracker.utils.pushNormal
import com.cultureoftech.easyexpensetracker.utils.replaceNormal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ReportDetailController(val bundle: Bundle) : ScopedController(), ReportDetailView {

    constructor() : this(Bundle())

    @BindView(R.id.et_report_title) lateinit var titleEditText: TextInputEditText
    @BindView(R.id.et_comments) lateinit var commentsEditText: TextInputEditText
    @BindView(R.id.btn_add_submitted_date) lateinit var addDateButton: TextView
    @BindView(R.id.tv_submitted_date) lateinit var submittedDateTextView: TextView
    @BindView(R.id.ll_submitted_date_container) lateinit var submittedDateContainer: View
    @BindView(R.id.btn_view_expenses) lateinit var viewExpensesButton: View
    @BindView(R.id.btn_add_expense) lateinit var addExpenseButton: View

    var component: ReportDetailComponent? = null
    var firstBind = true
    @Inject lateinit var presenter: ReportDetailPresenter
    @Inject lateinit var simpleDateFormat: SimpleDateFormat

    override fun onViewBound(view: View) {
        component?.inject(this)
        presenter.attach(this)
    }

    override fun setData(report: ExpenseReport, hideButtons: Boolean) {
        if (firstBind) {
            titleEditText.setText(report.title)
            commentsEditText.setText(report.comments)
            firstBind = false
        }
        dateUpdated(report.dateSubmitted)
        updateUi(hideButtons)
    }

    private fun updateUi(hideButtons: Boolean) {
        if (hideButtons) {
            addExpenseButton.visibility = View.GONE
            viewExpensesButton.visibility = View.GONE
        }
    }

    override fun showDatePicker(date: Date?) {
        val calendar = Calendar.getInstance(Locale.getDefault())
        if (date != null) {
            calendar.time = date
        }
        DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener {
            datePicker, year, month, day ->
            presenter.submittedDateSelected(GregorianCalendar(year, month, day).time)
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    override fun dateUpdated(date: Date?) {
        if (date == null) {
            submittedDateContainer.visibility = View.GONE
        } else {
            submittedDateContainer.visibility = View.VISIBLE
            submittedDateTextView.text = simpleDateFormat.format(date)
        }
        updateButtonText(date != null)
    }

    private fun updateButtonText(hasDate: Boolean) {
        addDateButton.text = view.context.getString(
                if (hasDate) R.string.remove_submitted_date else R.string.add_submitted_date)

    }

    @OnClick(R.id.btn_save) fun save() {
        val title = titleEditText.text.toString()
        val comments = commentsEditText.text.toString()
        presenter.save(title, comments)
    }

    @OnClick(R.id.btn_add_submitted_date) fun updateDate() {
        presenter.updateDateClicked();
    }

    @OnClick(R.id.btn_view_expenses) fun goToReportExpenses() {
        val bundle = Bundle()
        bundle.putLong("reportId", presenter.report.id)
        router.pushNormal(ExpenseListController(bundle))
    }

    @OnClick(R.id.btn_add_expense) fun addExpense() {
        val bundle = Bundle()
        bundle.putLong("reportId", presenter.report.id)
        router.replaceNormal(DetailController(bundle))
    }

    override fun reportSaved() {
        router.popCurrentController()
    }

    override fun expensesAttached() {
        Snackbar.make(view, R.string.expenses_attached, Snackbar.LENGTH_LONG).show()
    }

    override fun initComponent(): Any {
        val reportId = bundle.getLong("reportId", 0)
        val addedExpenses = bundle.getLongArray("expenseIds") ?: LongArray(0)
        component = (router.activity.application as MyApplication).component.plus(ReportDetailModule(reportId, addedExpenses))
        return component!!
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_report_detail, container, false)
    }

    override fun getScope(): String {
        return ForReportDetail::class.java.name
    }
}