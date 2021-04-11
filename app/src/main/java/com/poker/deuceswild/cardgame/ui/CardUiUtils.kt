package com.poker.deuceswild.cardgame.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.poker.deuceswild.PokerApplication
import com.poker.deuceswild.R
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.settings.SettingsUtils

object CardUiUtils {

    fun tintCards(cardViews: List<ImageView>?, fullHand: List<Card>?, cardsToTint: List<Card>) {
        fullHand?.forEachIndexed { index, c ->
            if (c in cardsToTint) {
                cardViews?.get(index)?.setColorFilter(ContextCompat.getColor(PokerApplication.applicationContext(), R.color.colorGrey), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    fun unTintCards(cardViews: List<ImageView>?, fullHand: List<Card>?) {
        fullHand?.forEachIndexed { index, _ ->
            cardViews?.get(index)?.colorFilter = null
        }
    }

    fun showCards(cardViews: List<ImageView>?, fullHand: List<Card>?) {
        fullHand?.forEachIndexed { index, card ->
            cardViews?.get(index)?.setImageResource(cardToImage(card))
        }
    }

    fun showCardBacks(cardViews: List<ImageView>?) {
        cardViews?.forEach {
            it.setImageResource(SettingsUtils.getCardBack(PokerApplication.applicationContext()))
        }
    }

    fun makeCardsVisibile(cardViews: List<ImageView>?) {
        cardViews?.forEach {
            it.visibility = View.VISIBLE
        }
    }

    fun highlightHeldCards(holdsViews: List<TextView>?, fullHand: List<Card>?, heldHand: List<Card>?) {
        fullHand?.forEachIndexed { index, card ->
            if(heldHand?.contains(card) == true){
                holdsViews?.get(index)?.visibility = View.VISIBLE
            }
        }
    }

    fun unhighlightHeldCards(holdsViews: List<TextView>?) {
        holdsViews?.forEach { it.visibility = View.INVISIBLE }
    }

    fun toggleHighlightHeldCards(holdsViews: List<TextView>?, cardIndex: Int) {
        val view = holdsViews?.get(cardIndex)
        view?.visibility = if(view?.visibility == View.INVISIBLE) View.VISIBLE else View.INVISIBLE
    }

    fun cardToImage(card: Card?) : Int{
        if(card == null) return R.drawable.card_back_default
        return when(card.rank) {
            2 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.two_of_spades
                    }
                    'h' -> {
                        R.drawable.two_of_hearts
                    }
                    'd' -> {
                        R.drawable.two_of_diamonds
                    }
                    'c' -> {
                        R.drawable.two_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            3 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.three_of_spades
                    }
                    'h' -> {
                        R.drawable.three_of_hearts
                    }
                    'd' -> {
                        R.drawable.three_of_diamonds
                    }
                    'c' -> {
                        R.drawable.three_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            4 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.four_of_spades
                    }
                    'h' -> {
                        R.drawable.four_of_hearts
                    }
                    'd' -> {
                        R.drawable.four_of_diamonds
                    }
                    'c' -> {
                        R.drawable.four_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            5 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.five_of_spades
                    }
                    'h' -> {
                        R.drawable.five_of_hearts
                    }
                    'd' -> {
                        R.drawable.five_of_diamonds
                    }
                    'c' -> {
                        R.drawable.five_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            6 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.six_of_spades
                    }
                    'h' -> {
                        R.drawable.six_of_hearts
                    }
                    'd' -> {
                        R.drawable.six_of_diamonds
                    }
                    'c' -> {
                        R.drawable.six_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            7 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.seven_of_spades
                    }
                    'h' -> {
                        R.drawable.seven_of_hearts
                    }
                    'd' -> {
                        R.drawable.seven_of_diamonds
                    }
                    'c' -> {
                        R.drawable.seven_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            8 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.eight_of_spades
                    }
                    'h' -> {
                        R.drawable.eight_of_hearts
                    }
                    'd' -> {
                        R.drawable.eight_of_diamonds
                    }
                    'c' -> {
                        R.drawable.eight_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            9 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.nine_of_spades
                    }
                    'h' -> {
                        R.drawable.nine_of_hearts
                    }
                    'd' -> {
                        R.drawable.nine_of_diamonds
                    }
                    'c' -> {
                        R.drawable.nine_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            10 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.ten_of_spades
                    }
                    'h' -> {
                        R.drawable.ten_of_hearts
                    }
                    'd' -> {
                        R.drawable.ten_of_diamonds
                    }
                    'c' -> {
                        R.drawable.ten_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            11 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.jack_of_spades
                    }
                    'h' -> {
                        R.drawable.jack_of_hearts
                    }
                    'd' -> {
                        R.drawable.jack_of_diamonds
                    }
                    'c' -> {
                        R.drawable.jack_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            12 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.queen_of_spades
                    }
                    'h' -> {
                        R.drawable.queen_of_hearts
                    }
                    'd' -> {
                        R.drawable.queen_of_diamonds
                    }
                    'c' -> {
                        R.drawable.queen_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            13 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.king_of_spades
                    }
                    'h' -> {
                        R.drawable.king_of_hearts
                    }
                    'd' -> {
                        R.drawable.king_of_diamonds
                    }
                    'c' -> {
                        R.drawable.king_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            14 -> {
                when(card.suit) {
                    's' -> {
                        R.drawable.ace_of_spades
                    }
                    'h' -> {
                        R.drawable.ace_of_hearts
                    }
                    'd' -> {
                        R.drawable.ace_of_diamonds
                    }
                    'c' -> {
                        R.drawable.ace_of_clubs
                    }
                    else -> {
                        -1
                    }
                }
            }
            else -> {
                SettingsUtils.getCardBack(PokerApplication.applicationContext())
            }
        }
    }
}

//package com.poker.deuceswild.cardgame.ui
//
//import android.content.Context
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import com.poker.deuceswild.R
//import com.poker.deuceswild.cardgame.Card
//import com.poker.deuceswild.settings.SettingsUtils
//
//
//object CardUiUtils {
//
//    fun showCards(context: Context, cardViews: List<ImageView>?, fullHand: List<Card>?) {
//        fullHand?.forEachIndexed { index, card ->
//            cardViews?.get(index)?.setImageResource(cardToImage(context,card))
//            cardViews?.get(index)?.setBackgroundResource(0)
//        }
//    }
//
//    fun showCardBacks(context: Context, cardViews: List<ImageView>?) {
//        cardViews?.forEach {
//            val cardback = SettingsUtils.getCardBack(context)
//            it.setImageResource(cardback)
//            it.setBackgroundResource(cardback)
//        }
//    }
//
//    fun makeCardsVisibile(cardViews: List<ImageView>?) {
//        cardViews?.forEach {
//            it.visibility = View.VISIBLE
//        }
//    }
//
//    fun highlightHeldCards(holdsViews: List<TextView>, fullHand: List<Card>?, heldHand: List<Card>) {
//        if(fullHand == null) return
//        for (i in 0..4){
//            if(fullHand[i] in heldHand){
//                holdsViews[i].visibility = View.VISIBLE
//            } else {
//                holdsViews[i].visibility = View.INVISIBLE
//            }
//        }
//    }
//
//    fun toggleHighlightHeldCards(holdsViews: List<TextView>?, cardIndex: Int) {
//        val view = holdsViews?.get(cardIndex)
//        view?.visibility = if(view?.visibility == View.INVISIBLE) View.VISIBLE else View.INVISIBLE
//    }
//
//    fun unhighlightHeldCards(holdsViews: List<TextView>?) {
//        holdsViews?.forEach { it.visibility = View.INVISIBLE }
//    }
//
