package com.cultureoftech.easyexpensetracker.image.viewer

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.base.MyApplication
import com.cultureoftech.easyexpensetracker.controllers.ScopedController
import com.cultureoftech.easyexpensetracker.dagger.ForImageViewer
import javax.inject.Inject


class ImageViewerController(val bundle: Bundle): ScopedController(), ImageViewerView {

    lateinit var component: ImageViewerComponent

    @BindView(R.id.iv_full_image) lateinit var imageView: ImageView

    @Inject lateinit var presenter: ImageViewerPresenter

    init {
        setHasOptionsMenu(true)
    }

    override fun onViewBound(view: View) {
        component.inject(this)
        presenter.attach(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_image_viewer, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_action_share_image -> {
                shareImage()
                return true
            }
            else -> return false
        }
    }

    private fun shareImage() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, presenter.getImageUri())
        shareIntent.type = "image/*"
        startActivity(Intent.createChooser(shareIntent, view.context.getString(R.string.share_image)))
    }

    override fun setImagePath(imagePath: String) {
        Glide.with(view.context).load(imagePath).into(imageView)
    }

    override fun initComponent(): Any {
        val imagePath = bundle.getString("imagePath")
        component = (router.activity.application as MyApplication).component.plus(ImageViewerModule(imagePath))
        return component
    }

    override fun getScope(): String {
        return ForImageViewer::class.java.name
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_image_viewer, container, false)
    }
}