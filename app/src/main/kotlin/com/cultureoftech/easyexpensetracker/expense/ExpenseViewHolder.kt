package com.cultureoftech.easyexpensetracker.expense

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.dao.Expense
import com.cultureoftech.easyexpensetracker.utils.toCurrency

class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mode: ExpenseListPresenter.ListMode = ExpenseListPresenter.ListMode.NORMAL
        set(value) {
            when (value) {
                ExpenseListPresenter.ListMode.NORMAL -> setUpDefaultState()
                ExpenseListPresenter.ListMode.ASSIGN_REPORT -> setUpSelectableState()
            }
        }

    var selected: Boolean = false

    @BindView(R.id.tv_expense_title) lateinit var expenseTitle: TextView
    @BindView(R.id.tv_payee) lateinit var expensePayee: TextView
    @BindView(R.id.tv_expense_amount) lateinit var expenseAmount: TextView
    @BindView(R.id.tv_report_title) lateinit var reportTitle: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(expense: Expense, mode: ExpenseListPresenter.ListMode, selected: Boolean) {
        this.selected = selected
        expenseTitle.text = expense.title
        expensePayee.text = expense.payee?.name
        expenseAmount.text = expense.amount.toCurrency()
        reportTitle.text = expense.expenseReport?.title
        this.mode = mode
    }

    fun setUpDefaultState() {
        itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.selector_ripple)
        itemView.setBackgroundColor(Color.WHITE)
        val elevation = itemView.context.resources.getDimension(R.dimen.default_elevation)
        ViewCompat.setElevation(itemView, elevation)
    }

    fun setUpSelectableState() {
        itemView.background = null
        val color = if (selected) ContextCompat.getColor(itemView.context, R.color.colorAccent) else Color.WHITE
        itemView.setBackgroundColor(color)
        val elevation = itemView.context.resources.getDimension(if (selected) R.dimen.selected_elevation else R.dimen.unselected_elevation)
        ViewCompat.setElevation(itemView, elevation)
    }
}