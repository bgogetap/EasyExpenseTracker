package com.cultureoftech.easyexpensetracker

import de.greenrobot.daogenerator.DaoGenerator
import java.io.File


fun main(args: Array<String>) {
    val schema = de.greenrobot.daogenerator.Schema(1, "com.cultureoftech.easyexpensetracker.dao")
    schema.enableKeepSectionsByDefault()

    // Expense
    val expense = schema.addEntity("Expense")
    expense.addIdProperty()
    expense.addStringProperty("title")
    val dateProperty = expense.addDateProperty("date").property
    expense.addDoubleProperty("amount")
    expense.addStringProperty("comments")
    expense.addStringProperty("imagePath")

    // Payee
    val payee = schema.addEntity("Payee")
    payee.addIdProperty()
    payee.addStringProperty("name")
    payee.addStringProperty("comments")

    // Expense Report
    val report = schema.addEntity("ExpenseReport")
    report.addIdProperty()
    report.addStringProperty("title")
    report.addDateProperty("dateSubmitted")
    report.addDateProperty("dateReimbursed")
    report.addStringProperty("comments")

    // Expense has Payee
    val payeeId = expense.addLongProperty("payeeId").notNull().property
    expense.addToOne(payee, payeeId)
    // Payee has many Expenses
    val payeeToExpenses = payee.addToMany(expense, payeeId)
    payeeToExpenses.orderAsc(dateProperty)
    payeeToExpenses.name = "expenses"

    // Expense has Expense Report
    val reportId = expense.addLongProperty("reportId").notNull().property
    expense.addToOne(report, reportId)
    // Expense Report has many Expenses
    val reportToExpenses = report.addToMany(expense, reportId)
    reportToExpenses.name = "expenses"

    val file = File("app/src/main/kotlin/com/cultureoftech/easyexpensetracker/")
    System.out.println(file.absolutePath)
    DaoGenerator().generateAll(schema, "app/src/main/kotlin")
}
