package com.mvvm.contactlist.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.mvvm.contactlist.R
import com.mvvm.contactlist.databinding.ActivityMainBinding
import com.mvvm.contactlist.utilities.onBackButtonPressed

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()

        onBackButtonPressed {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        }
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)

        binding.navigationView.setupWithNavController(navController)
        mAppBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration)

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (NavigationUI.navigateUp(
            navController,
            mAppBarConfiguration
        ) || super.onSupportNavigateUp())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_nav_add_contact -> navController.navigate(R.id.action_nav_contact_list_to_nav_add_contact)
            R.id.drawer_nav_category_list -> navController.navigate(R.id.action_nav_contact_list_to_nav_add_category)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}