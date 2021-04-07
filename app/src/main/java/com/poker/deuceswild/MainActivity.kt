package com.poker.deuceswild

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.gms.ads.MobileAds
import com.poker.deuceswild.about.AboutFragment
import com.poker.deuceswild.stats.StatisticsManager
import com.poker.deuceswild.settings.SettingsFragment
import com.poker.deuceswild.stats.StatsFragment
import com.poker.deuceswild.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        MobileAds.initialize(this) {}
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onStart() {
        // save pending statistics (won, loss, etc) to disk
        StatisticsManager.readStatisticsFromDisk()
        super.onStart()
    }
    override fun onPause() {
        // save pending statistics (won, loss, etc) to disk
        StatisticsManager.writeStatisticsToDisk()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.stats -> {
            supportFragmentManager.beginTransaction().replace(R.id.container, StatsFragment.newInstance(), StatsFragment.NAME).commitNow()
            true
        }
        R.id.about -> {
            supportFragmentManager.beginTransaction().replace(R.id.container, AboutFragment(), AboutFragment.NAME).commitNow()
            true
        }
        R.id.settings -> {
            supportFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment(), SettingsFragment.NAME).commitNow()
            true
        }
        android.R.id.home -> {
//            if (isSettingsVisible() || isSimulatorVisible() || isStatsVisible()){
//                loadMainFragment()
//            }
            loadMainFragment()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun loadMainFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment(), MainFragment.NAME).commitNow()
    }
}