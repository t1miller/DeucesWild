package com.poker.deuceswild.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Deck

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    enum class GameState {
        START,
        DEAL,
        EVALUATE_NO_BONUS,
        EVALUATE_WITH_BONUS,
        BONUS,
    }

    companion object {
        const val DEFAULT_BET = 1
    }

    val hand: MutableLiveData<MutableList<Card>> = MutableLiveData()

    val bet: MutableLiveData<Int> by lazy {
        MutableLiveData(DEFAULT_BET)
    }

    val gameState: MutableLiveData<GameState> by lazy {
        MutableLiveData(GameState.START)
    }

//    val cardFLipState: MutableLiveData<CardFlipState> by lazy {
//        MutableLiveData(CardFlipState.FACE_DOWN)
//    }

    fun deal() {
        Deck.newDeck()
        hand.value = Deck.draw5()
        gameState.value = GameState.DEAL
//        cardFLipState.value = CardFlipState.FACE_UP
    }

    fun betOne() {
        bet.value = (bet.value ?: 0) % 5 + 1
    }

    fun betMax() {
        bet.value = 5
    }

    fun getKeptCardIndeces() : List<Boolean>{
        return listOf(false,false,false,false,false)
    }
}