package com.cultureoftech.easyexpensetracker.expense

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.dao.Expense

class ExpenseAdapter(
        val expenses: List<Expense>,
        val listener: ExpenseClickedListener,
        mode: ExpenseListPresenter.ListMode,
        val selectionHolder: SparseArray<Boolean>
) : RecyclerView.Adapter<ExpenseViewHolder>() {

    var mode: ExpenseListPresenter.ListMode = mode
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_view, parent, false)
        val holder = ExpenseViewHolder(view)
        holder.itemView.setOnClickListener {
            listener.expenseClicked(holder.adapterPosition)
        }
        holder.itemView.setOnLongClickListener {
            listener.expenseLongClicked(holder.adapterPosition)
            return@setOnLongClickListener true
        }
        return holder
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bindData(expenses[position], mode, selectionHolder.get(position, false))
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    override fun getItemId(position: Int): Long {
        return expenses[position].id
    }

    interface ExpenseClickedListener {

        fun expenseClicked(position: Int)

        fun expenseLongClicked(position: Int)
    }
}
