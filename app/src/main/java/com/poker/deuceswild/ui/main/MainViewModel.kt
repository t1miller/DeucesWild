package com.poker.deuceswild.ui.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.poker.deuceswild.ai.AIDecision
import com.poker.deuceswild.ai.AIPlayer
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Deck
import com.poker.deuceswild.cardgame.Evaluate
import com.poker.deuceswild.stats.Game
import com.poker.deuceswild.stats.StatisticsManager
import com.poker.deuceswild.payout.PayTableManager
import com.poker.deuceswild.settings.SettingsUtils
import com.poker.deuceswild.sound.SoundManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.system.measureTimeMillis

class MainViewModel(application: Application) : AndroidViewModel(application) {

    enum class GameState {
        START,
        DEAL,
        EVALUATE_WIN,
        EVALUATE_NO_WIN,
        BONUS,
    }

    companion object {
        const val DEFAULT_BET = 1
    }

    var aiDecision : MutableLiveData<AIDecision> = MutableLiveData()

    val hand: MutableLiveData<MutableList<Card>> = MutableLiveData()

    val eval: MutableLiveData<Pair<Evaluate.Hand,List<Card>>> = MutableLiveData()

    val bet: MutableLiveData<Int> by lazy {
        MutableLiveData(DEFAULT_BET)
    }

    val totalMoney: MutableLiveData<Int> by lazy {
        MutableLiveData(SettingsUtils.getMoney(application))
    }

    val wonLoss: MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }

    val gameState: MutableLiveData<GameState> by lazy {
        MutableLiveData(GameState.START)
    }

    var keptIndeces: BooleanArray = listOf(false,false,false,false,false).toBooleanArray()

    var lastCardsKept: List<Card> = listOf()

    var originalHand: List<Card> = listOf()


    fun deal() {
        Deck.newDeck()
        hand.value = Deck.draw5()
        gameState.value = GameState.DEAL
        getBestHand(hand.value, 2000)
    }

    fun evaluateHand(cardsToKeep: BooleanArray, cards: List<Card>) {
        keptIndeces = cardsToKeep
        lastCardsKept = cards
        originalHand = hand.value?.toList() ?: emptyList()
        if(cardsToKeep.toList().contains(false)) {
            val tempHand = mutableListOf<Card>()
            for (i in 0 until 5) {
                if (!cardsToKeep[i]) {
                    tempHand.add(Deck.draw1())
                } else {
                    tempHand.add(hand.value?.get(i) ?: Deck.draw1())
                }
            }
            hand.value = tempHand
        }

        eval.value = Evaluate.evaluate(hand.value ?: emptyList())
        if(eval.value?.first == Evaluate.Hand.NOTHING){
            // user won nothing, collect and log
            val wonLost = PayTableManager.getPayOut(SettingsUtils.getPayoutTable(getApplication()),eval.value?.first, bet.value)
            StatisticsManager.addStatistic(Game(bet.value, wonLost, eval.value?.first?.readableName, originalHand, lastCardsKept, hand.value))
            collect(wonLost)
            gameState.value = GameState.EVALUATE_NO_WIN
        } else {
            gameState.value = GameState.EVALUATE_WIN
        }
    }

    private fun collect(wonLost: Int) {
        wonLoss.value = wonLost
        totalMoney.value = (totalMoney.value ?: 0) + wonLost
        SettingsUtils.setMoney(totalMoney.value ?: SettingsUtils.Defaults.MONEY, getApplication())
    }

    fun collectNoBonus() {
        val wonLost = PayTableManager.getPayOut(SettingsUtils.getPayoutTable(getApplication()),eval.value?.first, bet.value)
        StatisticsManager.addStatistic(Game(bet.value, wonLost, eval.value?.first?.readableName, originalHand, lastCardsKept, hand.value))
        collect(wonLost)
    }

    /**
     *  C[2] is the index of the card to guess
     */
    fun collectBonus(isGuessRed: Boolean) {
        Deck.newDeck()
        val handFinal = hand.value?.toMutableList()
        hand.value = Deck.draw5()

        val tempPot = PayTableManager.getPayOut( SettingsUtils.getPayoutTable(getApplication()), eval.value?.first, bet.value)
        val money = PayTableManager.calculateBonusPayout(tempPot, hand.value?.get(2), isGuessRed)

        if(SettingsUtils.isBonusSoundEnabled(getApplication())){
            if (money > 0) {
                SoundManager.playSound(getApplication(), SoundManager.SoundType.ROOSTER_CROWING)
            } else {
                SoundManager.playSound(getApplication(), SoundManager.SoundType.SAD_TROMBONE_4_WOMP)
            }
        }

        StatisticsManager.addStatistic(Game(bet.value, money, eval.value?.first?.readableName, originalHand, lastCardsKept, handFinal))
        collect(money)
    }

    fun betOne() {
        bet.value = (bet.value ?: 0) % 5 + 1
    }

    fun betMax() {
        bet.value = 5
    }

    fun getBestHand(cards: List<Card>?, numTrials: Int=4000) {
        viewModelScope.launch {
            runGetBestHand(cards, numTrials)
        }
    }

    private suspend fun runGetBestHand(cards: List<Card>?, numTrials: Int) {
        if(cards == null || cards.size < 5){
            Toast.makeText(getApplication(), "Need 5 cards for simulation", Toast.LENGTH_LONG).show()
            aiDecision.postValue(null)
            return
        }
        withContext(Dispatchers.IO) {
            Timber.d("Running simulation")
            val timeInMillis = measureTimeMillis {
                aiDecision.postValue(AIPlayer.calculateBestHands(getApplication(), bet.value ?: DEFAULT_BET,cards, numTrials))
            }
            Timber.d("MonteCarlo simulation took $timeInMillis ms")
        }
    }

    fun lookupExpectedValue(hand: List<Card>) : Double{
        val foundIt = aiDecision.value?.sortedRankedHands?.find {
            it.first.toMutableSet() == hand.toMutableSet()
        }
        Timber.d("hand $hand")
        Timber.d("all ${aiDecision.value?.sortedRankedHands}")
        Timber.d("lookup value $foundIt")
        return foundIt?.second ?: 0.0
    }
}