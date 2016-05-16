package com.cultureoftech.easyexpensetracker.report

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.cultureoftech.easyexpensetracker.R


class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.tv_report_title) lateinit var titleTextView: TextView
    @BindView(R.id.tv_expense_count) lateinit var expenseCountTextView: TextView
    @BindView(R.id.tv_submitted_date) lateinit var submittedDateTextView: TextView

    init {
        ButterKnife.bind(this, itemView)
    }
}