package com.poker.deuceswild.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

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

//    fun getPayoutTable(context: Context?) : PayOutHelper.PAY_TABLE_TYPES{
//        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//        return when(preferences.getString(Keys.PAYOUT_TABLE, Defaults.PAYOUT_TABLE)){
//            "6/5 – 95.12%" -> PayOutHelper.PAY_TABLE_TYPES._6_5_95
//            "7/5 – 96.17%" -> PayOutHelper.PAY_TABLE_TYPES._7_5_96
//            "8/5 – 97.25%" -> PayOutHelper.PAY_TABLE_TYPES._8_5_97
//            "9/5 – 98.33%" -> PayOutHelper.PAY_TABLE_TYPES._9_5_98
//            "9/6 – 99.54%" -> PayOutHelper.PAY_TABLE_TYPES._9_6_99
//            else -> {
//                PayOutHelper.PAY_TABLE_TYPES._9_6_99
//            }
//        }
//    }
}