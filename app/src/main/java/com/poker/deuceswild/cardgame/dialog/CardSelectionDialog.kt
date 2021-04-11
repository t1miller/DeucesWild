package com.poker.deuceswild.cardgame.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.poker.deuceswild.PokerApplication
import com.poker.deuceswild.R
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.ui.CardUiUtils
import timber.log.Timber


object CardSelectionDialog {

    interface CardTouched {
        fun onCardSelected(card: Card)
    }

    fun showCardSelectionDialog(context: Context, cardTouched: CardTouched, cardsVisible: List<Card>) : Dialog{
        val dialog = Dialog(context)
        var suit: Char
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_card_selector)

        val cardViews = mutableListOf<ImageView>()
        cardViews.add(dialog.findViewById(R.id.ace))
        cardViews.add(dialog.findViewById(R.id.king))
        cardViews.add(dialog.findViewById(R.id.queen))
        cardViews.add(dialog.findViewById(R.id.jack))
        cardViews.add(dialog.findViewById(R.id.ten))
        cardViews.add(dialog.findViewById(R.id.nine))
        cardViews.add(dialog.findViewById(R.id.eight))
        cardViews.add(dialog.findViewById(R.id.seven))
        cardViews.add(dialog.findViewById(R.id.six))
        cardViews.add(dialog.findViewById(R.id.five))
        cardViews.add(dialog.findViewById(R.id.four))
        cardViews.add(dialog.findViewById(R.id.three))
        cardViews.add(dialog.findViewById(R.id.two))
        cardViews.add(dialog.findViewById(R.id.cardback))


        val heart = dialog.findViewById(R.id.heart) as ImageView
        val spade = dialog.findViewById(R.id.spade) as ImageView
        val clubs = dialog.findViewById(R.id.clubs) as ImageView
        val diamond = dialog.findViewById(R.id.diamond) as ImageView

        populateCards('h', cardViews, cardTouched, cardsVisible)
        heart.setOnClickListener {
            suit = 'h'
            highlightSuit(suit, heart, clubs, spade, diamond)
            populateCards(suit, cardViews, cardTouched, cardsVisible)
        }
        spade.setOnClickListener {
            suit = 's'
            highlightSuit(suit, heart, clubs, spade, diamond)
            populateCards(suit, cardViews, cardTouched, cardsVisible)
        }
        clubs.setOnClickListener {
            suit = 'c'
            highlightSuit(suit, heart, clubs, spade, diamond)
            populateCards(suit, cardViews, cardTouched, cardsVisible)
        }
        diamond.setOnClickListener {
            suit = 'd'
            highlightSuit(suit, heart, clubs, spade, diamond)
            populateCards(suit, cardViews, cardTouched, cardsVisible)
        }
        CardUiUtils.showCardBacks(listOf(dialog.findViewById(R.id.cardback)))

        Timber.d("showing card selection dialog")
        dialog.show()
        return dialog
    }

    private fun populateCards(suit: Char, cardViews: List<ImageView>, cardTouched: CardTouched, visibleCards: List<Card>, ){
        val cardsToShow = mutableListOf<Card>()
        for(i in 2..14){
            val card = Card(i,suit)
            cardsToShow.add(card)
        }

        cardsToShow.reverse()

        for((card,view) in cardsToShow.zip(cardViews)){
            view.setOnClickListener {
                view.setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                if(card in visibleCards){
                    Toast.makeText(PokerApplication.applicationContext(), "unselect first", Toast.LENGTH_SHORT).show()
                } else {
                    cardTouched.onCardSelected(card)
                }
            }
        }
        // card back
        cardViews.last().setOnClickListener { cardTouched.onCardSelected(Card()) }

        CardUiUtils.showCards(cardViews, cardsToShow)
        CardUiUtils.unTintCards(cardViews, cardsToShow)
        CardUiUtils.tintCards(cardViews, cardsToShow, visibleCards)
    }

    private fun highlightSuit(suit: Char,
                              heart: ImageView,
                              clubs: ImageView,
                              spade: ImageView,
                              diamond: ImageView
    ) {
        heart.background = null
        clubs.background = null
        spade.background = null
        diamond.background = null

        val color =  ContextCompat.getColor(PokerApplication.applicationContext(), R.color.colorYellow)
        when(suit) {
            'h' -> heart.setBackgroundColor(color)
            's' -> spade.setBackgroundColor(color)
            'd' -> diamond.setBackgroundColor(color)
            'c' -> clubs.setBackgroundColor(color)
        }
    }
}