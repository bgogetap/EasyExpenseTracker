package com.cultureoftech.easyexpensetracker.expense

import com.cultureoftech.easyexpensetracker.dagger.ForExpenseList
import com.cultureoftech.easyexpensetracker.expense.detail.DetailComponent
import com.cultureoftech.easyexpensetracker.expense.detail.DetailModule
import dagger.Subcomponent

@ForExpenseList
@Subcomponent(modules = arrayOf(ExpenseListModule::class))
interface ExpenseListComponent {

    fun inject(listController: ExpenseListController)

    fun plus(module: DetailModule) : DetailComponent
}


