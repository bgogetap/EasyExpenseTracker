package com.cultureoftech.easyexpensetracker.report

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.dao.ExpenseReport

class ReportAdapter(
        val reports: List<ExpenseReport>,
        val listener: ReportClickedListener
): RecyclerView.Adapter<ReportViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_view, parent, false)
        val holder = ReportViewHolder(view)
        holder.itemView.setOnClickListener {
            listener.reportClicked(reports[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.titleTextView.text = reports[position].title
        reports[position].resetExpenses()
        holder.expenseCountTextView.text = reports[position].expenses.size.toString()
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun getItemId(position: Int): Long {
        return reports[position].id
    }

    interface ReportClickedListener {

        fun reportClicked(report: ExpenseReport)
    }
}