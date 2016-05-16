package com.cultureoftech.easyexpensetracker.expense.detail

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatSpinner
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.bluelinelabs.conductor.ChildControllerTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bumptech.glide.Glide
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.DEFAULT_PAYEE
import com.cultureoftech.easyexpensetracker.base.MyApplication
import com.cultureoftech.easyexpensetracker.controllers.ScopedController
import com.cultureoftech.easyexpensetracker.dagger.ForDetail
import com.cultureoftech.easyexpensetracker.dao.Expense
import com.cultureoftech.easyexpensetracker.image.viewer.ImageViewerController
import com.cultureoftech.easyexpensetracker.model.ImageHelper
import com.cultureoftech.easyexpensetracker.photo.AddPhotoDialog
import com.cultureoftech.easyexpensetracker.utils.alert
import com.cultureoftech.easyexpensetracker.utils.pushNormal
import com.cultureoftech.easyexpensetracker.utils.toSafeDouble
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class DetailController(val bundle: Bundle) : ScopedController(), AddPhotoDialog.PhotoDialogClickListener, DetailView {

    constructor() : this(Bundle())

    companion object {
        val TAKE_PHOTO_REQUEST_CODE = 10
        val CHOOSE_PHOTO_REQUEST_CODE = 20
    }

    @BindView(R.id.detail_parent_layout) lateinit var parent: View
    @BindView(R.id.iv_expense_photo) lateinit var imageView: ImageView
    @BindView(R.id.ll_image_container) lateinit var imageContainer: View
    @BindView(R.id.et_comments) lateinit var commentsEditText: EditText
    @BindView(R.id.et_expense_title) lateinit var titleEditText: EditText
    @BindView(R.id.btn_add_photo) lateinit var addPhotoButton: View
    @BindView(R.id.et_expense_amount) lateinit var amountEditText: EditText
    @BindView(R.id.et_payee) lateinit var payeeEditText: AutoCompleteTextView
    @BindView(R.id.spinner_reports) lateinit var reportSpinner: AppCompatSpinner

    var component: DetailComponent? = null
    var firstBind: Boolean = true

    @Inject lateinit var presenter: DetailPresenter
    @Inject lateinit var imageHelper: ImageHelper

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_detail, container, false)
    }

    override fun onViewBound(view: View) {
        component?.inject(this)
        presenter.attach(this)
    }

    override fun getScope(): String {
        return ForDetail::class.java.name
    }

    override fun setData(expense: Expense, payees: Array<String>, expenseReports: Array<String>, reportTitle: String?) {
        updateViewVisibility(presenter.getImagePath())

        val payeeAdapter = ArrayAdapter<String>(view.context, R.layout.spinner_item, payees)
        payeeEditText.setAdapter(payeeAdapter)

        val reportAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_selectable_list_item, expenseReports)
        reportSpinner.adapter = reportAdapter

        if (firstBind) {
            titleEditText.setText(expense.title)
            amountEditText.setText(expense.amount.toString())
            commentsEditText.setText(expense.comments)
            payeeEditText.setText(if (expense.isNewExpense) DEFAULT_PAYEE else expense.payee.name)

            val selectedReport = expenseReports.indexOf(reportTitle)
            reportSpinner.setSelection(selectedReport)
        } else {
            reportSpinner.setSelection(presenter.selectedReportPosition)
        }

        amountEditText.onFocusChangeListener = View.OnFocusChangeListener { view, gain ->
            run {
                val text = amountEditText.text.toString()
                if (gain) {
                    val value = text.toSafeDouble()
                    if (value == 0.0) {
                        amountEditText.setText("")
                    }
                } else {
                    try {
                        text.toDouble()
                    } catch (e: NumberFormatException) {
                        Timber.e("Focus lost with non-number data, reverting to 0.0", e)
                        amountEditText.setText("0.0")
                    }
                    if (text.isEmpty()) {
                        amountEditText.setText("0.0")
                    }
                }
            }
        }

        if (expense.hasImage()) {
            Glide.with(activity).load(File(expense.imagePath)).into(imageView)
        }
        firstBind = false
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        presenter.selectedReportPosition = reportSpinner.selectedItemPosition
        super.onSaveViewState(view, outState)
    }

    override fun photoUpdated(imagePath: String?) {
        if (TextUtils.isEmpty(imagePath)) {
            imageView.setImageDrawable(null)
        } else {
            Glide.with(activity).load(File(imagePath)).into(imageView)
        }
        updateViewVisibility(imagePath)
    }

    private fun updateViewVisibility(imagePath: String?) {
        imageContainer.visibility = if (!TextUtils.isEmpty(imagePath)) View.VISIBLE else View.GONE
        addPhotoButton.visibility = if (!TextUtils.isEmpty(imagePath)) View.GONE else View.VISIBLE
    }

    @OnClick(R.id.btn_add_photo, R.id.btn_replace_photo) fun addPhoto() {
        parent.requestFocus()
        addChildController(ChildControllerTransaction.builder(AddPhotoDialog(), R.id.detail_parent_layout)
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler())
                .addToLocalBackstack(true)
                .build())
    }

    @OnClick(R.id.btn_delete_photo) fun deletePhoto() {
        alert(activity, R.string.delete_attached_image, R.string.delete,
                DialogInterface.OnClickListener { dialogInterface, i -> presenter.deletePhoto() })
                .show()
    }

    @OnClick(R.id.ll_image_container) fun openImage() {
        val bundle = Bundle()
        bundle.putString("imagePath", presenter.getImagePath())
        router.pushNormal(ImageViewerController(bundle))
    }

    @OnClick(R.id.btn_save) fun saveExpense() {
        val title = titleEditText.text.toString()
        val amount = amountEditText.text.toString().toDouble()
        val comments = commentsEditText.text.toString()
        val payee = payeeEditText.text.toString()
        presenter.saveExpense(title, amount, comments, payee, reportSpinner.selectedItem as String)
    }

    override fun expenseSaved() {
        router.popCurrentController()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TAKE_PHOTO_REQUEST_CODE -> presenter.photoTaken(data)
                CHOOSE_PHOTO_REQUEST_CODE -> presenter.photoChosen(data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initComponent(): Any {
        val expenseId = bundle.getLong("expenseId", 0)
        val reportId = bundle.getLong("reportId", 1)
        component = (router.activity.application as MyApplication).component.plus(DetailModule(expenseId, reportId))
        return component!!
    }

    override fun takePhoto() {
        startActivityForResult(imageHelper.getCameraIntent(), TAKE_PHOTO_REQUEST_CODE)
    }

    override fun choosePhoto() {
        startActivityForResult(imageHelper.getImageChooserIntent(), CHOOSE_PHOTO_REQUEST_CODE)
    }
}
