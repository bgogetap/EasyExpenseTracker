package com.cultureoftech.easyexpensetracker.expense.selectReport

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView


class SelectReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindReport(report: String) {
        (itemView as TextView).text = report
    }
}