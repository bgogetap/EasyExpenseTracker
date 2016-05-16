package com.cultureoftech.easyexpensetracker.expense.selectReport

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cultureoftech.easyexpensetracker.R

class SelectReportAdapter(
        val reports: Array<String>,
        val listener: ReportSelectedListener
) : RecyclerView.Adapter<SelectReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectReportViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_view_report, parent, false)
        val holder = SelectReportViewHolder(view)
        holder.itemView.setOnClickListener {
            listener.reportSelected(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: SelectReportViewHolder, position: Int) {
        holder.bindReport(reports[position])
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    interface ReportSelectedListener {

        fun reportSelected(position: Int)
    }
}