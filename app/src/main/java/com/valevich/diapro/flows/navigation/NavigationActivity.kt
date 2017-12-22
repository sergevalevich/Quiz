package com.valevich.diapro.flows.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.firebase.ui.auth.AuthUI
import com.valevich.diapro.R
import com.valevich.diapro.base.view.BaseActivity
import com.valevich.diapro.firebase.MainActivity
import com.valevich.diapro.flows.notes.view.all.NotesFragment
import com.valevich.diapro.flows.stats.view.StatsFragment
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*

class NavigationActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceFragment(NotesFragment())
        }

        setupActionBar()
        setupDrawerLayout()
        setupFragmentManager()
    }

    override fun onBackPressed() {
        when {
            drawerView.isDrawerOpen(GravityCompat.START) -> drawerView.closeDrawer(GravityCompat.START)
            supportFragmentManager.backStackEntryCount == 1 -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun onBackStackChanged() {
        supportFragmentManager.findFragmentById(R.id.main_container)?.apply { changeToolbarTitle(javaClass.name) }
    }

    private fun setupNavigationContent() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerView?.apply { closeDrawer(GravityCompat.START) }
            val itemId = item.itemId
            when (itemId) {
                R.id.drawer_notes -> replaceFragment(NotesFragment())
                R.id.drawer_statistics -> replaceFragment(StatsFragment())
                R.id.drawer_exit -> logout()
            }
            true
        }
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener({
                    startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                })
    }

    private fun changeToolbarTitle(backStackEntryName: String) {
        if (backStackEntryName == NotesFragment::class.java.name) {
            title = getString(R.string.notes)
            navigationView.setCheckedItem(R.id.drawer_notes)
        } else if (backStackEntryName == StatsFragment::class.java.name) {
            title = getString(R.string.stats)
            navigationView.setCheckedItem(R.id.drawer_statistics)
        }
    }

    private fun setupDrawerLayout() {
        setupNavigationContent()
        val toggle = ActionBarDrawerToggle(
                this,
                drawerView,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        toggle.syncState()
        drawerView.addDrawerListener(toggle)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun replaceFragment(fragment: Fragment) {
        val backStackName = fragment.javaClass.name

        val isFragmentPopped = supportFragmentManager.popBackStackImmediate(backStackName, 0)

        if (!isFragmentPopped && supportFragmentManager.findFragmentByTag(backStackName) == null) {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_container, fragment, backStackName)
                addToBackStack(backStackName)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                commit()
            }
        }
    }

    private fun setupFragmentManager() {
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun layoutId(): Int = R.layout.activity_navigation
}
