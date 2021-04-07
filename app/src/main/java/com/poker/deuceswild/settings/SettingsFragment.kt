package com.poker.deuceswild.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.poker.deuceswild.R
import com.poker.deuceswild.cardgame.dialog.ResetMoneyDialog
import com.poker.deuceswild.stats.StatisticsManager


class SettingsFragment : PreferenceFragmentCompat(), ResetMoneyDialog.MoneyButton {

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

        val resetMoneyButton: Preference? = findPreference(SettingsUtils.Keys.RESET_MONEY)
        resetMoneyButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//            Toast.makeText(context, "Money reset to ${SettingsUtils.Defaults.MONEY}", Toast.LENGTH_LONG).show()
//            SettingsUtils.resetMoney(requireContext())
            ResetMoneyDialog.showDialog(requireContext(), this)
            true
        }

        val resetStatsButton: Preference? = findPreference(SettingsUtils.Keys.RESET_STATS)
        resetStatsButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            StatisticsManager.deleteStatisticsOnDisk()
            Toast.makeText(requireContext(),"Reset statistics", Toast.LENGTH_LONG).show()
            true
        }

        val shareStats: Preference? = findPreference(SettingsUtils.Keys.SHARE_STATS)
        shareStats?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            StatisticsManager.shareStatistics(requireContext())
            Toast.makeText(requireContext(),"Share stats", Toast.LENGTH_LONG).show()
            true
        }

        val cardBack: Preference? = findPreference(SettingsUtils.Keys.CHOOSE_CARDBACK)
        cardBack?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            SettingsUtils.showChangeCardBackDialog(requireContext())
            Toast.makeText(requireContext(),"Cardback changed", Toast.LENGTH_LONG).show()
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setMoney(amount: Int) {
        SettingsUtils.setMoney(amount, requireContext())
        Toast.makeText(requireContext(),"Money set: $$amount", Toast.LENGTH_LONG).show()
    }
}