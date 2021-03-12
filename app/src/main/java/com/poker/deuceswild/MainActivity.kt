package com.poker.deuceswild

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.poker.deuceswild.settings.SettingsFragment
import com.poker.deuceswild.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.simulations -> {
//            supportFragmentManager.beginTransaction().replace(R.id.container, SimulatorFragment(), SimulatorFragment.NAME).commitNow()
//            true
//        }
//
//        R.id.stats -> {
//            loadStatFragment()
//            true
//        }

        R.id.settings -> {
            supportFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment(), SettingsFragment.NAME).commitNow()
            true
        }
        android.R.id.home -> {
//            if (isSettingsVisible() || isSimulatorVisible() || isStatsVisible()){
//                loadMainFragment()
//            }
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}