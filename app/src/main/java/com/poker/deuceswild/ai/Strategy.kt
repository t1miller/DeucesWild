package com.poker.deuceswild.ai

import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Evaluate

object Strategy {

    data class StrategyResponse(
        var fullCards: List<Card>,
        var winningCards: List<Card>,
        var tipsRuledOut: List<String>,
        var tip: String,
    )

    fun bestStrategy(cards: List<Card>) : StrategyResponse {
        when(Evaluate.deuceCount(cards)) {
            4 -> {
               return deuces4Strat(cards)
            }
            3 -> {
                return deuces3Strat(cards)
            }
            2 -> {
                return deuces2Strat(cards)
            }
            1 -> {
                return deuces1Strat(cards)
            }
            0 -> {
                return deuces0Strat(cards)
            }
        }
        return StrategyResponse(cards, emptyList(), listOf("no strategy :("),"no strategy :(")
    }

    /**
     *  4 Deuces Strategy:
     *  1) 4 deuces
     */
    private fun deuces4Strat(cards: List<Card>) : StrategyResponse{
        return StrategyResponse(
                cards,
                cards.filter { it.rank == 2 },
                listOf("four deuces"),
                "four deuces"
        )
    }

    /**
     *  3 Deuces Strategy:
     *  1) Pat royal flush
     *  2) 3 deuces
     */
    private fun deuces3Strat(cards: List<Card>) : StrategyResponse{
        // todo add sequential royal, natural royal, wild royal
        val tipsRuledOut = mutableListOf<String>()
        tipsRuledOut.add("royal flush")
        return if(Evaluate.isRoyalFlush(cards)){
            StrategyResponse(cards, cards, tipsRuledOut,"royal flush")
        } else {
            tipsRuledOut.add("three deuces")
            StrategyResponse(cards, cards.filter { it.rank == 2 }, tipsRuledOut,"three deuces")
        }
    }

    /**
     *  2 Deuces Strategy:
     *  1) Any pat four of a kind or higher (Royal, Straight Flush, Four of a Kind)
     *  2) 4 to a royal flush
     *  3) 4 to a straight flush with 2 consecutive singletons, 6-7 or higher
     *  4) 2 deuces only
     */
    private fun deuces2Strat(cards: List<Card>) : StrategyResponse{

        val tipsRuledOut = mutableListOf<String>()

        // todo add sequential royal, natural royal, wild royal
        tipsRuledOut.add("royal flush")
        if(Evaluate.isRoyalFlush(cards)){
            return StrategyResponse(cards, cards, tipsRuledOut,"royal flush")
        }

        tipsRuledOut.add("five of a kind")
        if(Evaluate.isFiveOfKind(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"five of a kind")
        }

        tipsRuledOut.add("straight flush")
        if(Evaluate.isStraightFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight flush")
        }

        tipsRuledOut.add("four of a kind")
        val (winCards, isFourOfAKind) = Evaluate.isFourOfAKind(cards)
        if(isFourOfAKind){
            return StrategyResponse(cards, winCards,tipsRuledOut,"four of a kind")
        }

        tipsRuledOut.add("four to a royal")
        val (winCards2, isFourToRoyal) = Evaluate.isFourToARoyal(cards)
        if(isFourToRoyal){
            return StrategyResponse(cards, winCards2,tipsRuledOut,"four to a royal")
        }

        // todo add check for 2 consecutive singletons 6-7 or higher
        tipsRuledOut.add("four to a straight flush")
        val (winCards3, isFourToStraight) = Evaluate.isFourToStraightFlush(cards)
        if(isFourToStraight) {
            return StrategyResponse(cards, winCards3,tipsRuledOut,"four to a straight flush")
        }

        // Last resort return 2 deuces
        tipsRuledOut.add("two deuces")
        return StrategyResponse(cards, cards.filter { it.rank == 2 },tipsRuledOut,"two deuces")
    }

    /**
     *  1 Deuces Strategy:
     *  1) Any pat four of a kind or higher (Royal, Straight Flush, Four of a Kind)
     *  2) 4 to a royal flush
     *  3) Full house
     *  4) 4 to a straight flush with 3 consecutive singletons, 5-7 or higher
     *  5) 3 of a kind, straight, or flush
     *  6) All other 4 to a straight flush
     *  7) 3 to a royal flush
     *  8) 3 to a straight flush with 2 consecutive singletons, 6-7 or higher
     *  9) deuce only
     */
    private fun deuces1Strat(cards: List<Card>) : StrategyResponse{

        val tipsRuledOut = mutableListOf<String>()

        // todo add sequential royal, natural royal, wild royal
        tipsRuledOut.add("royal flush")
        if(Evaluate.isRoyalFlush(cards)){
            return StrategyResponse(cards, cards, tipsRuledOut,"royal flush")
        }

        tipsRuledOut.add("five of a kind")
        if(Evaluate.isFiveOfKind(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"five of a kind")
        }

        tipsRuledOut.add("straight flush")
        if(Evaluate.isStraightFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight flush")
        }

        tipsRuledOut.add("four of a kind")
        val (winCards, isFourOfAKind) = Evaluate.isFourOfAKind(cards)
        if(isFourOfAKind) {
            return StrategyResponse(cards, winCards,tipsRuledOut,"four of a kind")
        }

        tipsRuledOut.add("four to a royal")
        val (winCards2, isFourToARoyal) = Evaluate.isFourToARoyal(cards)
        if(isFourToARoyal){
            return StrategyResponse(cards, winCards2,tipsRuledOut,"four to a royal")
        }

        tipsRuledOut.add("full house")
        if(Evaluate.isFullHouse(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"full house")
        }
        // todo implement add check for 3 consecutive singletons 5-7 or higher
        tipsRuledOut.add("four to a straight flush")
        val (winCards4, fourToStraight) = Evaluate.isFourToStraightFlush(cards)
        if(fourToStraight){
            return StrategyResponse(cards, winCards4,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("four to a straight flush")
        if(Evaluate.isFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("straight")
        if(Evaluate.isStraight(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight")
        }

        tipsRuledOut.add("three of a kind")
        val (winCards5, isThreeOfAKind) = Evaluate.isThreeOfAKind(cards)
        if(isThreeOfAKind){
            return StrategyResponse(cards, winCards5,tipsRuledOut,"three of a kind")
        }

        // todo add All other 4 to a straight flush
        tipsRuledOut.add("four to a straight flush")
        val (winCards6, fourToStraight2) = Evaluate.isFourToStraightFlush(cards)
        if(fourToStraight2){
            return StrategyResponse(cards, winCards6,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("three to a royal flush")
        val (winCards7, treeToRoyalFlush) = Evaluate.isThreeToRoyalFlush(cards)
        if(treeToRoyalFlush){
            return StrategyResponse(cards, winCards7,tipsRuledOut,"three to a royal flush")
        }

        tipsRuledOut.add("four to a straight flush")
        val (winCards8, threeToRoyal) = Evaluate.isThreeToStraightFlush(cards)
        if(threeToRoyal){
            return StrategyResponse(cards, winCards8,tipsRuledOut,"four to a straight flush")
        }

        // Last resort return 1 deuce
        tipsRuledOut.add("one deuce")
        return StrategyResponse(cards, cards.filter { it.rank == 2 },tipsRuledOut,"one deuce")
    }

    /**
     *  0 Deuces Strategy:
     *  1) 4,5 to a royal flush
     *  2) Made three of a kind to straight flush
     *  3) 4 to a straight flush
     *  4) 3 to a royal flush
     *  5) Pair
     *  6) 4 to a flush
     *  7) 4 to an outside straight
     *  8) 3 to a straight flush
     *  9) 4 to an inside straight, except missing deuce
     *  10) 2 to a royal flush, J,Q high
     */
    private fun deuces0Strat(cards: List<Card>) : StrategyResponse{

        val tipsRuledOut = mutableListOf<String>()

        tipsRuledOut.add("royal flush")
        if(Evaluate.isRoyalFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"royal flush")
        }

        tipsRuledOut.add("four to a royal")
        val (winCards, isFourToARoyal) = Evaluate.isFourToARoyal(cards)
        if(isFourToARoyal){
            return StrategyResponse(cards, winCards,tipsRuledOut,"four to a royal")
        }

        tipsRuledOut.add("straight flush")
        if(Evaluate.isStraightFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight flush")
        }

        tipsRuledOut.add("four of a kind")
        val (winCards1, isFourOfAKind) = Evaluate.isFourOfAKind(cards)
        if(isFourOfAKind){
            return StrategyResponse(cards, winCards1,tipsRuledOut,"four of a kind")
        }

        tipsRuledOut.add("full house")
        if(Evaluate.isFullHouse(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"full house")
        }

        tipsRuledOut.add("flush")
        if(Evaluate.isFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"flush")
        }

        tipsRuledOut.add("straight")
        if(Evaluate.isStraight(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight")
        }

        tipsRuledOut.add("three of a kind")
        val (winCards2, isThreeOfAKind) = Evaluate.isThreeOfAKind(cards)
        if(isThreeOfAKind){
            return StrategyResponse(cards, winCards2,tipsRuledOut,"three of a kind")
        }

        tipsRuledOut.add("four to a straight flush")
        val (winCards3, isFourToStraightFlush) = Evaluate.isFourToStraightFlush(cards)
        if(isFourToStraightFlush){
            return StrategyResponse(cards, winCards3,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("three to a royal flush")
        val (winCards4, isThreeToRoyalFlush) = Evaluate.isThreeToRoyalFlush(cards)
        if(isThreeToRoyalFlush){
            return StrategyResponse(cards, winCards4,tipsRuledOut,"three to a royal flush")
        }

        tipsRuledOut.add("pair")
        val (winCards5, isPair) = Evaluate.isPair(cards)
        if(isPair){
            return StrategyResponse(cards, winCards5,tipsRuledOut,"pair")
        }

        tipsRuledOut.add("four to a flush")
        val (winCards6, isFourToFlush) = Evaluate.isFourToFlush(cards)
        if(isFourToFlush){
            return StrategyResponse(cards, winCards6,tipsRuledOut,"four to a flush")
        }

        tipsRuledOut.add("four to outside straight")
        val (winCards7, isFourToOutsideStraight) = Evaluate.isFourToOutsideStraight(cards)
        if(isFourToOutsideStraight){
            return StrategyResponse(cards, winCards7,tipsRuledOut,"four to outside straight")
        }

        tipsRuledOut.add("three to straight flush")
        val (winCards8, isThreeToStraightFlush) = Evaluate.isThreeToStraightFlush(cards)
        if(isThreeToStraightFlush){
            return StrategyResponse(cards, winCards8,tipsRuledOut,"three to straight flush")
        }

        tipsRuledOut.add("four to inside straight")
        val (winCards9, isFourToInsideStraightAndDontNeedDeuce) = Evaluate.isFourToInsideStraightAndDontNeedDeuce(cards)
        if(isFourToInsideStraightAndDontNeedDeuce){
            return StrategyResponse(cards, winCards9,tipsRuledOut,"four to inside straight")
        }

        tipsRuledOut.add("two to a royal flush")
        val (winCards10, isTwoToARoyalFlushJQHigh) = Evaluate.isTwoToARoyalFlushJQHigh(cards)
        if(isTwoToARoyalFlushJQHigh){
            return StrategyResponse(cards, winCards10,tipsRuledOut,"two to a royal flush")
        }

        tipsRuledOut.add("no strategy :(")
        return StrategyResponse(cards, emptyList(),tipsRuledOut,"no strategy :(")
    }
}
