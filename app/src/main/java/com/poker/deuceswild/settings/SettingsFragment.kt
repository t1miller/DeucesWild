package com.poker.deuceswild.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import com.poker.deuceswild.R


class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        val NAME = SettingsFragment::class.java.simpleName
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val resetMoneyButton: Preference? = findPreference(SettingsUtils.Keys.RESET_MONEY)
//        resetMoneyButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//            Toast.makeText(context, "Money reset to ${SettingsUtils.Defaults.MONEY}", Toast.LENGTH_LONG).show()
//            SettingsUtils.resetMoney(requireContext())
//            true
//        }
//
//        val resetStatsButton: Preference? = findPreference(SettingsUtils.Keys.RESET_STATS)
//        resetStatsButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//            StatisticsManager.deleteStatisticsOnDisk()
//            Toast.makeText(requireContext(),"Reset stats", Toast.LENGTH_LONG).show()
//            true
//        }
//
//        val shareStats: Preference? = findPreference(SettingsUtils.Keys.SHARE_STATS)
//        shareStats?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//            StatisticsManager.shareStatistics(requireContext())
//            Toast.makeText(requireContext(),"Share stats", Toast.LENGTH_LONG).show()
//            true
//        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}