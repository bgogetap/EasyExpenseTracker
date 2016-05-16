package com.cultureoftech.easyexpensetracker.base

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.controllers.ScopedController
import com.cultureoftech.easyexpensetracker.dagger.ForExpenseList
import com.cultureoftech.easyexpensetracker.dagger.Injector
import com.cultureoftech.easyexpensetracker.expense.ExpenseListController
import com.cultureoftech.easyexpensetracker.utils.bindView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ControllerChangeHandler.ControllerChangeListener {

    val container by bindView<FrameLayout>(R.id.fl_main_container)
    val drawer by bindView<DrawerLayout>(R.id.drawer_layout)
    val navView by bindView<NavigationView>(R.id.nav_view)
    val toolbar by bindView<Toolbar>(R.id.toolbar)

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(ExpenseListController(), ForExpenseList::class.java.name)
        }
        router.addChangeListener(this)

        // Set up Drawer
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nav_camera -> {}
            R.id.nav_slideshow -> {}
            R.id.nav_manage -> {}
            R.id.nav_share -> {}
            R.id.nav_send -> {}
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup?, handler: ControllerChangeHandler?) {

    }

    override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup?, handler: ControllerChangeHandler?) {
        if (!isPush) Injector.destroy(this, (from as ScopedController).getScope())
    }

    fun updateTitle(title: String?) {
        if (TextUtils.isEmpty(title)) {
            toolbar.title = getString(R.string.app_name)
        } else {
            toolbar.title = title
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        drawer.removeDrawerListener(toggle)
    }
}
