package com.poker.deuceswild.ai

import android.content.Context
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Deck
import com.poker.deuceswild.cardgame.Evaluate
import com.poker.deuceswild.payout.PayTableManager
import timber.log.Timber

data class AIDecision (
        var numTrials: Int,
        var bet: Int,
        var hand: List<Card>,
        var sortedRankedHands: List<Pair<List<Card>, Double>>
)

object AIPlayer {

    const val DEBUG = false

    fun calculateBestHands(context: Context, bet: Int, hand: List<Card>, numTrials: Int) : AIDecision {

        val hands = hand.powerset()
        val handsEvaluated = mutableListOf<Pair<List<Card>, Double>>()


        for (h in hands) {
            val expectedValue =
                monteCarloEvaluation(
                    context,
                    bet,
                    h.toList(),
                    numTrials
                )
            handsEvaluated.add(Pair(h.toMutableList(), expectedValue))
        }

        handsEvaluated.sortByDescending { it.second }

        for((idx,h) in handsEvaluated.take(if(DEBUG) 32 else 5).withIndex()) {
            Timber.d("$idx) hand ${h.first} score ${h.second}")
        }
        return AIDecision(
            numTrials,
            bet,
            hand,
            handsEvaluated
        )
    }

    private fun monteCarloEvaluation(context: Context, bet: Int, hand: List<Card>, numTrials: Int) : Double {
        if(DEBUG) {
            Timber.d("====== Monte Carlo Simulation ======")
        }

        var trial = 0
        var expectedPayout = 0.0
        val evals = mutableListOf<Evaluate.Hand>()
        while (trial < numTrials) {
            val tempHand = Deck.draw5Random(hand.toMutableList())
            val eval = Evaluate.evaluate(tempHand)
            evals.add(eval.first)
            val payout = PayTableManager.getPayOut(eval.first)
            expectedPayout += payout
            trial += 1
        }

        if(DEBUG) {
            val evalCounts = evals.groupingBy { it }.eachCount()
            Timber.d("${hand.joinToString { it.toString() }} $evalCounts ${expectedPayout/numTrials}")
            Timber.d("====================================")
        }

        return expectedPayout/numTrials
    }

    private fun <T> Collection<T>.powerset(): Set<Set<T>> = when {
        isEmpty() -> setOf(setOf())
        else -> drop(1).powerset().let { it + it.map { it + first() } }
    }
}