package com.poker.deuceswild.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.poker.deuceswild.payout.PayTableType

object SettingsUtils {

    object Defaults{
        const val MONEY = 4000
        const val MONTE_CARLO_TRIALS = 5000
        const val SOUND = true
        const val SHEEP_MODE = false
        const val PAYOUT_TABLE = "9/6 – 99.54%"
        const val TRAINING_STRICTNESS = 20
    }

    object Keys{
        const val MONTE_CARLO_TRIALS = "montecarlo_trials"
        const val PAYOUT_TABLE = "payout_table"
        const val RESET_MONEY = "reset_money"
        const val RESET_STATS = "reset_stats"
        const val SHARE_STATS = "share_stats"
        const val TOTAL_MONEY = "money"
        const val SOUND = "sound"
        const val SHEEP_MODE = "sheep_mode"
        const val TRAINING_STRICTNESS = "training_strictness"
    }

    fun getNumTrials(context: Context?) : Int{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt(
            Keys.MONTE_CARLO_TRIALS,
            Defaults.MONTE_CARLO_TRIALS
        )
    }

    fun resetNumTrials(context: Context?) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with (preferences.edit()) {
            putInt(Keys.MONTE_CARLO_TRIALS, Defaults.MONTE_CARLO_TRIALS)
            apply()
        }
    }

    fun getStrictness(context: Context?) : Int{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt(
            Keys.TRAINING_STRICTNESS,
            Defaults.TRAINING_STRICTNESS
        )
    }

    fun setMoney(money: Int, context: Context) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with (preferences.edit()) {
            putInt(Keys.TOTAL_MONEY, money)
            apply()
        }
    }

    fun isSoundEnabled(context: Context) : Boolean{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(
            Keys.SOUND,
            Defaults.SOUND
        )
    }

    fun isSheepModeEnabled(context: Context) : Boolean{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(
            Keys.SHEEP_MODE,
            Defaults.SHEEP_MODE
        )
    }

    fun resetMoney(context: Context) {
        setMoney(Defaults.MONEY, context)
    }

    fun getMoney(context: Context) : Int {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt(Keys.TOTAL_MONEY, Defaults.MONEY)
    }

    fun getPayoutTable(context: Context?) : PayTableType{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return when(preferences.getString(Keys.PAYOUT_TABLE, Defaults.PAYOUT_TABLE)){
            "101.28% Sequential Royal Flush" -> PayTableType._101_28
            "100.76% Full Pay" -> PayTableType._100_76
            "100.36% 1-2-2-3-5-8-15-25-200-800" -> PayTableType._100_36
            "99.89% 1-2-2-3-5-9-15-20-200-800" -> PayTableType._99_89
            "99.81% 1-2-2-3-5-9-12-25-200-800" -> PayTableType._99_81
            "98.94% 1-2-2-3-5-9-12-20-200-800" -> PayTableType._98_94
            "96.77% Colorado Deuces" -> PayTableType._96_77
            "94.82% 1-2-2-3-4-10-15-25-200-800" -> PayTableType._94_82
            "94.34% 1-2-2-3-4-9-15-25-200-800" -> PayTableType._94_34
            "92.05% 1-2-2-3-4-8-12-20-200-800" -> PayTableType._92_05
            else -> {
                PayTableType._100_76
            }
        }
    }
}