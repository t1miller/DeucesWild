package com.poker.deuceswild.settings

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poker.deuceswild.R
import com.poker.deuceswild.payout.PayTableType
import timber.log.Timber

object SettingsUtils {

    object Defaults{
        const val MONEY = 4000
        const val SOUND = true
        const val SOUND_FLIP = true
        const val SOUND_WIN = true
        const val SOUND_LOSE = true
        const val SOUND_BONUS = true
        const val PAYOUT_TABLE = "9/6 – 99.54%"
        const val CHOOSE_CARDBACK = 0
    }


    object Keys{
        const val PAYOUT_TABLE = "payout_table"
        const val RESET_MONEY = "reset_money"
        const val RESET_STATS = "reset_stats"
        const val SHARE_STATS = "share_stats"
        const val TOTAL_MONEY = "money"
        const val SOUND = "sound"
        const val SOUND_WIN = "sound_win"
        const val SOUND_LOSE = "sound_lose"
        const val SOUND_FLIP = "sound_flip"
        const val SOUND_BONUS = "sound_bonus"
        const val CHOOSE_CARDBACK = "choose_cardback"
    }

    object CardBacks{
        val cardbacks = listOf(
            R.drawable.card_back_default,
            R.drawable.card_back_electric,
            R.drawable.card_back_flower,
            R.drawable.card_back_foot,
            R.drawable.card_back_gay,
            R.drawable.card_back_olympics,
            R.drawable.card_back_pinstriped,
            R.drawable.card_back_red,
            R.drawable.card_back_t_rex
        )
    }

    fun setCardBack(position: Int, context: Context) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(preferences.edit()) {
            putInt(Keys.CHOOSE_CARDBACK, position)
            apply()
        }
    }

    fun getCardBack(context: Context) : Int {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val position = preferences.getInt(
            Keys.CHOOSE_CARDBACK,
            Defaults.CHOOSE_CARDBACK
        )
        return CardBacks.cardbacks[position]
    }

    fun setMoney(money: Int, context: Context) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(preferences.edit()) {
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

    fun isWinSoundEnabled(context: Context) : Boolean{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(
            Keys.SOUND_WIN,
            Defaults.SOUND_WIN
        )
    }

    fun isLoseSoundEnabled(context: Context) : Boolean{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(
            Keys.SOUND_LOSE,
            Defaults.SOUND_LOSE
        )
    }

    fun isFlipSoundEnabled(context: Context) : Boolean{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(
            Keys.SOUND_FLIP,
            Defaults.SOUND_FLIP
        )
    }

    fun isBonusSoundEnabled(context: Context) : Boolean{
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(
            Keys.SOUND_BONUS,
            Defaults.SOUND_BONUS
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
            "101.28 Sequential Royal Flush" -> PayTableType._101_28
            "100.76 Full Pay" -> PayTableType._100_76
            "100.36 1–2–2–3–5–8–15–25–200–800" -> PayTableType._100_36
            "99.89 1–2–2–3–5–9–15–20–200–800" -> PayTableType._99_89
            "99.81 1–2–2–3–5–9–12–25–200–800" -> PayTableType._99_81
            "98.94 1–2–2–3–5–9–12–20–200–800" -> PayTableType._98_94
            "96.77 Colorado Deuces" -> PayTableType._96_77
            "94.82 1–2–2–3–4–10–15–25–200–800" -> PayTableType._94_82
            "94.34 1–2–2–3–4–9–15–25–200–800" -> PayTableType._94_34
            "92.05 1–2–2–3–4–8–12–20–200–800" -> PayTableType._92_05
            else -> {
                PayTableType._100_76
            }
        }
    }

    fun showChangeCardBackDialog(context: Context)  {

        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.change_card_dialog_layout)

        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }

        val recyclerView = dialog.findViewById(R.id.recyclerView) as RecyclerView
        val adapter = CardBackAdapter(object : CardTapped {
            override fun onCardTapped(position: Int) {
                setCardBack(position, context)
                dialog.dismiss()
                Toast.makeText(context, "cardback selected", Toast.LENGTH_LONG).show()
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        Timber.d("showing cardback dialog")
        dialog.show()
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}