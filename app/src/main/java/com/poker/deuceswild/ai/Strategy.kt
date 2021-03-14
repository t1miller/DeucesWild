package com.poker.deuceswild.ai

import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Deck
import timber.log.Timber

object Strategy {

    data class StrategyResponse(
        var fullCards: List<Card>,
        var winningCards: List<Card>,
        var tipsRuledOut: List<String>,
        var tip: String,
    )

    fun bestStrategy(cards: List<Card>) : StrategyResponse {
        when(deuceCount(cards)) {
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


    private fun isRoyalFlush(cards: List<Card>) : Boolean {
        return isFlush(cards) && isStraight(cards) && isRoyal(getNonDeuceCards(cards))
    }

    private fun isFourToARoyal(cards: List<Card>) : Pair<List<Card>,Boolean> {
        for(i in 0..4) {
            val cardCopy = cards.toMutableList()
            cardCopy[i] = Card(2,'s')
            if(isRoyalFlush(cards)){
                cardCopy.remove(Card(2,'s'))
                return Pair(cardCopy,true)
            }
        }
        return Pair(emptyList(),false)
    }

    fun isThreeToRoyalFlush(cards: List<Card>): Pair<List<Card>,Boolean>  {
        for (i in 0..4){
            for (j in 0..4){
                if(i != j){
                    val cardsCopy = cards.toMutableList()
                    cardsCopy[i] = Card(2,'s')
                    cardsCopy[j] = Card(2,'h')
                    if (isRoyalFlush(cardsCopy)) {
                        cardsCopy.remove(Card(2,'s'))
                        cardsCopy.remove(Card(2,'h'))
                        return Pair(cardsCopy,true)
                    }
                }
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isTwoToARoyalFlushJQHigh(cards: List<Card>): Pair<List<Card>,Boolean> {
        for (i in 0..4){
            for (j in 0..4){
                for (k in 0..4){
                    if(i != j && j != k && i != k) {
                        val cardsCopy = cards.toMutableList()
                        cardsCopy[i] = Card(2, 's')
                        cardsCopy[j] = Card(2, 'h')
                        cardsCopy[k] = Card(2, 'c')
                        if (isRoyalFlush(cardsCopy)) {
                            cardsCopy.remove(Card(2, 's'))
                            cardsCopy.remove(Card(2, 'h'))
                            cardsCopy.remove(Card(2, 'c'))
                            return Pair(cardsCopy,true)
                        }
                    }
                }
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isRoyal(cards: List<Card>) : Boolean {
        return cards.filter {
            it.rank == 10 ||
            it.rank == 11 ||
            it.rank == 12 ||
            it.rank == 13 ||
            it.rank == 14 }.size == cards.size
    }

    private fun makeAceLow(cards: List<Card>) : List<Card>{
        for(c in cards) {
            if (c.rank == 14){
                c.rank = 1
            }
        }
        return cards
    }

    private fun isStraightFlush(cards: List<Card>) : Boolean {
        return isStraight(cards) && isFlush(cards)
    }

    private fun isFourToStraightFlush(cards: List<Card>) : Pair<List<Card>,Boolean> {
        for(i in 0..4) {
            val cardCopy = cards.toMutableList()
            cardCopy[i] = Card(2,'s')
            if (isStraightFlush(cardCopy)) {
                cardCopy.remove(Card(2,'s'))
                return Pair(cardCopy, true)
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isThreeToStraightFlush(cards: List<Card>): Pair<List<Card>,Boolean> {
        for (i in 0..4){
            for (j in 0..4){
                if(i != j){
                    val cardsCopy = cards.toMutableList()
                    cardsCopy[i] = Card(2,'s')
                    cardsCopy[j] = Card(2,'h')
                    if (isStraightFlush(cardsCopy)) {
                        cardsCopy.remove(Card(2,'s'))
                        cardsCopy.remove(Card(2,'h'))
                        return Pair(cardsCopy, true)
                    }
                }
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isFiveOfKind(cards: List<Card>) : Boolean {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        val deuceCount = deuceCount(cards)
        return groups.any { (it.value.size + deuceCount) ==  5}
    }

    private fun isFourOfAKind(cards: List<Card>) : Pair<List<Card>,Boolean> {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        val deuceCount = deuceCount(cards)
        val isFourOfAKind = groups.any { (it.value.size + deuceCount) ==  4}
        return if(isFourOfAKind){
            val largestGroup = (groups[4-deuceCount]?.toMutableList() ?: mutableListOf())
            largestGroup.addAll(getDeuceCards(cards))
            Pair(largestGroup, true)
        } else {
            Pair(emptyList(), false)
        }
    }

    private fun isFullHouse(cards: List<Card>) : Boolean {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        return when(groups.size){
            1,
            2 -> { groups.any { it.value.size != 4 } }
            else -> false
        }
    }

    private fun isFlush(cards: List<Card>): Boolean {
        val noDeuce = getNonDeuceCards(cards)
        val suit = noDeuce[0].suit
        if (noDeuce.drop(1).all { it.suit == suit }) return true
        return false
    }

//    private fun isFlush(cards: List<Card>): Boolean {
//        val suit = cards[0].suit
//        if (cards.drop(1).all { it.suit == suit }) return true
//        return false
//    }

    private fun isFourToFlush(cards: List<Card>): Pair<List<Card>,Boolean> {
        val suit = cards.groupBy { it.suit }.toSortedMap().firstKey()
        for(i in 0..4) {
            val cardCopy = cards.toMutableList()
            cardCopy[i] = Card(2,suit)
            if (isFlush(cardCopy)) {
                cardCopy.remove(Card(2,suit))
                return Pair(cardCopy, true)
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isStraight(cards: List<Card>): Boolean {
        val sorted = getNonDeuceCards(cards).sortedBy { it.rank }
        var deuceCount = deuceCount(cards)
        var index = 0
        var count = 0
        var lowerBoundCard = sorted[index].rank
        while (deuceCount >= 0 && count < 4) {//23456
            if(index < cards.size - 1 && lowerBoundCard == cards[index+1].rank + 1){
                // next card is part of straight
                index += 1
                lowerBoundCard = cards[index+1].rank
            } else{
                // use a deuce
                deuceCount -= 1
                lowerBoundCard += 1
            }
            count += 1
        }
        if(count == 4){
            Timber.d("straight = ${sorted}")
        }
        // todo this is wrong and doesnt check for ace low straight
//                || (count == 3 && isStraight(makeAceLow(cards.toList())))
        return count == 4
    }

    private fun isFourToOutsideStraight(cards: List<Card>): Pair<List<Card>,Boolean> {
        val sorted = cards.sortedBy { it.rank }
        for(i in 0..4) {
            val cardCopy = sorted.toMutableList()
            cardCopy[i] = Card(2,'c')
            if(isStraight(cardCopy)
                    && cardCopy[0].rank > 2
                    && cardCopy[4].rank < 14
                    && (i == 0 || i == 4) ){
                cardCopy.remove(Card(2,'c'))
                return Pair(cardCopy, true)
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isFourToInsideStraightAndDontNeedDeuce(cards: List<Card>): Pair<List<Card>,Boolean> {
        val sorted = cards.sortedBy { it.rank }
        for(i in 0..4) {
            val cardCopy = sorted.toMutableList()
            cardCopy[i] = Card(2,'c')
            /**
             *  AD345
             *  34567
             */
            if(isStraight(cardCopy) &&
                    (i != 0 || i != 4) &&
                    (i != 1)){
                cardCopy.remove(Card(2,'c'))
                return Pair(cardCopy,true)
            }
        }
        return Pair(emptyList(),false)
    }

    private fun isThreeOfAKind(cards: List<Card>): Pair<List<Card>,Boolean> {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        val isThreeOfAKind = groups.any { it.value.size == 3 || (it.value.size + deuceCount(cards) == 3) }
        return if(isThreeOfAKind){
            for (i in 0..4){
                for (j in 0..4){
                    for (k in 0..4){
                        if((i != j && j != k && i != k) &&
                                (cards[i].rank == cards[j].rank || cards[i].rank == 2 || cards[j].rank == 2) &&
                                (cards[j].rank == cards[k].rank || cards[j].rank == 2 || cards[k].rank == 2) &&
                                (cards[i].rank == cards[k].rank || cards[i].rank == 2 || cards[k].rank == 2)) {
                            Pair(listOf(cards[i], cards[j], cards[k]), true)
                        }
                    }
                }
            }
            Pair(emptyList(),false)
        } else {
            Pair(emptyList(),false)
        }
    }

    private fun isPair(cards: List<Card>): Pair<List<Card>,Boolean>{
        // ASSUMING NO DEUCES
        // this returns the highest pair in two pairs
        val sorted = cards.sortedByDescending { it.rank }
        for (i in 0..4){
            for (j in 0..4){
                if(i != j && sorted[i].rank == sorted[j].rank){
                    return Pair(listOf(sorted[i],sorted[j]),true)
                }
            }
        }
       return Pair(emptyList(),false)
    }

    private fun getNonDeuceCards(cards: List<Card>) : List<Card> {
        return cards.filter { it.rank != 2 }
    }

    private fun getDeuceCards(cards: List<Card>) : List<Card> {
        return cards.filter { it.rank == 2 }
    }

    fun deuceCount(cards: List<Card>) : Int {
        return cards.filter { it.rank == 2 }.count()
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
        return if(isRoyalFlush(cards)){
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
        if(isRoyalFlush(cards)){
            return StrategyResponse(cards, cards, tipsRuledOut,"royal flush")
        }

        tipsRuledOut.add("five of a kind")
        if(isFiveOfKind(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"five of a kind")
        }

        tipsRuledOut.add("straight flush")
        if(isStraightFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight flush")
        }

        tipsRuledOut.add("four of a kind")
        val (winCards, isFourOfAKind) = isFourOfAKind(cards)
        if(isFourOfAKind){
            return StrategyResponse(cards, winCards,tipsRuledOut,"four of a kind")
        }

        tipsRuledOut.add("four to a royal")
        val (winCards2, isFourToRoyal) = isFourToARoyal(cards)
        if(isFourToRoyal){
            return StrategyResponse(cards, winCards2,tipsRuledOut,"four to a royal")
        }

        // todo add check for 2 consecutive singletons 6-7 or higher
        tipsRuledOut.add("four to a straight flush")
        val (winCards3, isFourToStraight) = isFourToStraightFlush(cards)
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
        if(isRoyalFlush(cards)){
            return StrategyResponse(cards, cards, tipsRuledOut,"royal flush")
        }

        tipsRuledOut.add("five of a kind")
        if(isFiveOfKind(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"five of a kind")
        }

        tipsRuledOut.add("straight flush")
        if(isStraightFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight flush")
        }

        tipsRuledOut.add("four of a kind")
        val (winCards, isFourOfAKind) = isFourOfAKind(cards)
        if(isFourOfAKind) {
            return StrategyResponse(cards, winCards,tipsRuledOut,"four of a kind")
        }

        tipsRuledOut.add("four to a royal")
        val (winCards2, isFourToARoyal) = isFourToARoyal(cards)
        if(isFourToARoyal){
            return StrategyResponse(cards, winCards2,tipsRuledOut,"four to a royal")
        }

        tipsRuledOut.add("full house")
        if(isFullHouse(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"full house")
        }
        // todo implement add check for 3 consecutive singletons 5-7 or higher
        tipsRuledOut.add("four to a straight flush")
        val (winCards4, fourToStraight) = isFourToStraightFlush(cards)
        if(fourToStraight){
            return StrategyResponse(cards, winCards4,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("four to a straight flush")
        if(isFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("straight")
        if(isStraight(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight")
        }

        tipsRuledOut.add("three of a kind")
        val (winCards5, isThreeOfAKind) = isThreeOfAKind(cards)
        if(isThreeOfAKind){
            return StrategyResponse(cards, winCards5,tipsRuledOut,"three of a kind")
        }

        // todo add All other 4 to a straight flush
        tipsRuledOut.add("four to a straight flush")
        val (winCards6, fourToStraight2) = isFourToStraightFlush(cards)
        if(fourToStraight2){
            return StrategyResponse(cards, winCards6,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("three to a royal flush")
        val (winCards7, treeToRoyalFlush) = isThreeToRoyalFlush(cards)
        if(treeToRoyalFlush){
            return StrategyResponse(cards, winCards7,tipsRuledOut,"three to a royal flush")
        }

        tipsRuledOut.add("four to a straight flush")
        val (winCards8, threeToRoyal) = isThreeToStraightFlush(cards)
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
        if(isRoyalFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"royal flush")
        }

        tipsRuledOut.add("four to a royal")
        val (winCards, isFourToARoyal) = isFourToARoyal(cards)
        if(isFourToARoyal){
            return StrategyResponse(cards, winCards,tipsRuledOut,"four to a royal")
        }

        tipsRuledOut.add("straight flush")
        if(isStraightFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight flush")
        }

        tipsRuledOut.add("four of a kind")
        val (winCards1, isFourOfAKind) = isFourOfAKind(cards)
        if(isFourOfAKind){
            return StrategyResponse(cards, winCards1,tipsRuledOut,"four of a kind")
        }

        tipsRuledOut.add("full house")
        if(isFullHouse(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"full house")
        }

        tipsRuledOut.add("flush")
        if(isFlush(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"flush")
        }

        tipsRuledOut.add("straight")
        if(isStraight(cards)){
            return StrategyResponse(cards, cards,tipsRuledOut,"straight")
        }

        tipsRuledOut.add("three of a kind")
        val (winCards2, isThreeOfAKind) = isThreeOfAKind(cards)
        if(isThreeOfAKind){
            return StrategyResponse(cards, winCards2,tipsRuledOut,"three of a kind")
        }

        tipsRuledOut.add("four to a straight flush")
        val (winCards3, isFourToStraightFlush) = isFourToStraightFlush(cards)
        if(isFourToStraightFlush){
            return StrategyResponse(cards, winCards3,tipsRuledOut,"four to a straight flush")
        }

        tipsRuledOut.add("three to a royal flush")
        val (winCards4, isThreeToRoyalFlush) = isThreeToRoyalFlush(cards)
        if(isThreeToRoyalFlush){
            return StrategyResponse(cards, winCards4,tipsRuledOut,"three to a royal flush")
        }

        tipsRuledOut.add("pair")
        val (winCards5, isPair) = isPair(cards)
        if(isPair){
            return StrategyResponse(cards, winCards5,tipsRuledOut,"pair")
        }

        tipsRuledOut.add("four to a flush")
        val (winCards6, isFourToFlush) = isFourToFlush(cards)
        if(isFourToFlush){
            return StrategyResponse(cards, winCards6,tipsRuledOut,"four to a flush")
        }

        tipsRuledOut.add("four to outside straight")
        val (winCards7, isFourToOutsideStraight) = isFourToOutsideStraight(cards)
        if(isFourToOutsideStraight){
            return StrategyResponse(cards, winCards7,tipsRuledOut,"four to outside straight")
        }

        tipsRuledOut.add("three to straight flush")
        val (winCards8, isThreeToStraightFlush) = isThreeToStraightFlush(cards)
        if(isThreeToStraightFlush){
            return StrategyResponse(cards, winCards8,tipsRuledOut,"three to straight flush")
        }

        tipsRuledOut.add("four to inside straight")
        val (winCards9, isFourToInsideStraightAndDontNeedDeuce) = isFourToInsideStraightAndDontNeedDeuce(cards)
        if(isFourToInsideStraightAndDontNeedDeuce){
            return StrategyResponse(cards, winCards9,tipsRuledOut,"four to inside straight")
        }

        tipsRuledOut.add("two to a royal flush")
        val (winCards10, isTwoToARoyalFlushJQHigh) = isTwoToARoyalFlushJQHigh(cards)
        if(isTwoToARoyalFlushJQHigh){
            return StrategyResponse(cards, winCards10,tipsRuledOut,"two to a royal flush")
        }

        tipsRuledOut.add("no strategy :(")
        return StrategyResponse(cards, emptyList(),tipsRuledOut,"no strategy :(")
    }
}

object StrategyTester {

    var decisions = mutableListOf<Strategy.StrategyResponse>()

    fun log(strategy: Strategy.StrategyResponse) {
        decisions.add(strategy)
    }

    fun runSimulation(numTrials: Int = 200){
        for (i in 1..numTrials){
            Deck.newDeck()
            val cards = Deck.draw5()
            val bestStrategy = Strategy.bestStrategy(cards)
            log(bestStrategy)
        }
        decisions.print()
    }

    fun List<Strategy.StrategyResponse>.print() {
        var prettyResponse = "====================================\n"
        for (response in this){
            prettyResponse += "\nfull cards: " + response.fullCards
            prettyResponse += "\nwinning cards: " + response.winningCards
            prettyResponse += "\neval: " + response.tip
            prettyResponse += "\n"
        }
        prettyResponse += "====================================\n"
        Timber.d(prettyResponse)
    }
}



//object StrategyTester {
//
//    val hands = listOf(
//            listOf(Card(13,'c'),Card(12,'h'),Card(12,'c'),Card(14,'c'),Card(4,'s')), // three to royal
////            listOf(Card(13,'c'),Card(12,'h'),Card(12,'c'),Card(14,'c'),Card(4,'s')) // three to royal
//
//    )
//    //  2,2,2,2,4
//    //  1,2,3,4,5
//    //  3,3,4,2,4
//    fun test3ToAFlush() {
//        val result = Strategy.isThreeToRoyalFlush(hands[0])
//    }
//}