
package com.poker.deuceswild.cardgame



object Evaluate {

    enum class Hand(val readableName: String){
        SEQUENTIAL_ROYAL_FLUSH("Sequential Royal Flush"),
        NATURAL_ROYAL_FLUSH("Natural Royal Flush"),
        FOUR_DEUCES("Four Deuces"),
        WILD_ROYAL_FLUSH("Wild Royal Flush"),
        FIVE_OF_A_KIND("Five of a Kind"),
        STRAIGHT_FLUSH("Straight Flush"),
        FOUR_OF_A_KIND("Four of a Kind"),
        FULL_HOUSE("Full House"),
        FLUSH("Flush"),
        STRAIGHT("Straight"),
        THREE_OF_A_KIND("Three of a kind"),
        NOTHING("Nothing");
        // Dont change order

        companion object {
            fun handFromReadableName(name: String?) : Hand {
                for (mune in values()) {
                    if(name == mune.readableName) {
                        return mune
                    }
                }
                return NOTHING
            }
        }
    }

    fun evaluate(hand: List<Card>) : Pair<Hand,List<Card>>{

        val isRoyalFlush = isRoyalFlush(hand)

        if (isRoyalFlush && isSequential(hand)){
            return Pair(Hand.SEQUENTIAL_ROYAL_FLUSH, hand)
        }

        val deuceCount = deuceCount(hand)
        if(isRoyalFlush &&  deuceCount == 0){
            return Pair(Hand.NATURAL_ROYAL_FLUSH, hand)
        }

        if(deuceCount == 4){
            return Pair(Hand.FOUR_DEUCES, hand)
        }

        if(isRoyalFlush && deuceCount > 0){
            return Pair(Hand.WILD_ROYAL_FLUSH, hand)
        }

        if(isFiveOfKind(hand)){
            return Pair(Hand.FIVE_OF_A_KIND, hand)
        }

        if(isStraightFlush(hand)){
            return Pair(Hand.STRAIGHT_FLUSH, hand)
        }

        val (winningCards,isFourOfAKind) = isFourOfAKind(hand)
        if(isFourOfAKind){
            return Pair(Hand.FOUR_OF_A_KIND, winningCards)
        }

        if(isFullHouse(hand)){
            return Pair(Hand.FULL_HOUSE, hand)
        }

        if(isFlush(hand)){
            return Pair(Hand.FLUSH, winningCards)
        }

        if(isStraight(hand)){
            return Pair(Hand.STRAIGHT, winningCards)
        }

        val (winningCards2,isThreeOfAkind) = isThreeOfAKind(hand)
        if(isThreeOfAkind){
            return Pair(Hand.THREE_OF_A_KIND, winningCards2)
        }

        return Pair(Hand.NOTHING, emptyList())
    }

    private fun isSequential(cards: List<Card>) : Boolean{
        for (i in 0..4){
            if(i < cards.size -1 && (cards[i].rank >= cards[i+1].rank)){
                // cards not in increasing order
                return false
            }
        }
        return true
    }

    fun isRoyalFlush(cards: List<Card>) : Boolean {
        return isFlush(cards) && isStraight(cards) && isRoyal(getNonDeuceCards(cards))
    }

    fun isFourToARoyal(cards: List<Card>) : Pair<List<Card>,Boolean> {
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

    fun isTwoToARoyalFlushJQHigh(cards: List<Card>): Pair<List<Card>,Boolean> {
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

    fun makeAceLow(cards: List<Card>) : List<Card>{
        for(c in cards) {
            if (c.rank == 14){
                c.rank = 1
            }
        }
        return cards
    }

    fun makeAceHigh(cards: List<Card>) : List<Card>{
        for(c in cards) {
            if (c.rank == 1){
                c.rank = 14
            }
        }
        return cards
    }

    fun isStraightFlush(cards: List<Card>) : Boolean {
        return isStraight(cards) && isFlush(cards)
    }

    fun isFourToStraightFlush(cards: List<Card>) : Pair<List<Card>,Boolean> {
//        var debug = ""
        for(i in 0..4) {
            val cardCopy = cards.toMutableList()
            cardCopy[i] = Card(2,'s')
            if (isStraightFlush(cardCopy)) {
//                debug += "cards: $cardCopy\n"
                cardCopy.remove(Card(2,'s'))
//                debug += "isflush: ${isFlush(cardCopy)}\n"
//                debug += "isStraight: ${isFlush(cardCopy)}\n"
//                Timber.d(debug)
                return Pair(cardCopy, true)
            }
        }
        return Pair(emptyList(),false)
    }

    fun isThreeToStraightFlush(cards: List<Card>): Pair<List<Card>,Boolean> {
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

    fun isFiveOfKind(cards: List<Card>) : Boolean {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        val deuceCount = deuceCount(cards)
        return groups.any { (it.value.size + deuceCount) ==  5}
    }

    fun isFourOfAKind(cards: List<Card>) : Pair<List<Card>,Boolean> {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        val deuceCount = deuceCount(cards)
        val isFourOfAKind = groups.any { (it.value.size + deuceCount) ==  4}
        return if(isFourOfAKind){
            val nonDeuces = groups.maxByOrNull { it.value.size }?.value?.toMutableList()
            nonDeuces?.addAll(getDeuceCards(cards))
            Pair(nonDeuces ?: emptyList(),true)
        } else {
            Pair(emptyList(), false)
        }
    }

    fun isFullHouse(cards: List<Card>) : Boolean {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        return when(groups.size){
            1,
            2 -> { groups.any { it.value.size != 4 } }
            else -> false
        }
    }

    fun isFlush(cards: List<Card>): Boolean {
        val noDeuce = getNonDeuceCards(cards)
        val suit = noDeuce[0].suit
        if (noDeuce.drop(1).all { it.suit == suit }) return true
        return false
    }

    fun isFourToFlush(cards: List<Card>): Pair<List<Card>,Boolean> {
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

    fun removePairs(cards: List<Card>) : List<Card>{
        return cards.distinctBy { it.rank }
    }

    fun isStraightHelper(cards: List<Card>): Boolean {
        val sorted = removePairs(getNonDeuceCards(cards).sortedBy { it.rank })
        var deuceCount = deuceCount(cards)
        var index = 0
        var count = 0
        var lowerBoundCard = sorted[index].rank

        while (count < 5) {
            if(index < sorted.size - 1 && lowerBoundCard == sorted[index+1].rank-1){
                // next card is part of straight
                lowerBoundCard = sorted[index+1].rank
                index += 1
            } else{
                // use a deuce
                deuceCount -= 1
                if(deuceCount<0)
                    break
                lowerBoundCard += 1
            }
            count += 1
        }
        return count == 4
    }

    fun isStraight(cards: List<Card>) : Boolean{
        val straight = isStraightHelper(cards)
        var aceLowStraight = false
        if(!straight){
            aceLowStraight = isStraightHelper(makeAceLow(cards))
            makeAceHigh(cards)
        }
        return straight || aceLowStraight
    }

    fun isFourToStraight(cards: List<Card>): Pair<List<Card>,Boolean> {
        val sorted = cards.sortedBy { it.rank }
        for(i in 0..4) {
            val cardCopy = sorted.toMutableList()
            cardCopy[i] = Card(2,'c')
            if(isStraight(cardCopy)){
                cardCopy.remove(Card(2,'c'))
                return Pair(cardCopy, true)
            }
        }
        return Pair(emptyList(),false)
    }

    fun isFourToOutsideStraight(cards: List<Card>): Pair<List<Card>,Boolean> {
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
        //todo this doesnt work
        return Pair(emptyList(),false)
    }

    fun isFourToInsideStraightAndDontNeedDeuce(cards: List<Card>): Pair<List<Card>,Boolean> {
        val sorted = cards.sortedBy { it.rank }
        for(i in 0..4) {
            val cardCopy = sorted.toMutableList()
            cardCopy[i] = Card(2,'c')
            if(isStraight(cardCopy) &&
                    (i != 0 || i != 4) &&
                    (i != 1)){
                cardCopy.remove(Card(2,'c'))
                return Pair(cardCopy,true)
            }
        }
        // todo this doesnt work
        return Pair(emptyList(),false)
    }

    fun isThreeOfAKind(cards: List<Card>): Pair<List<Card>,Boolean> {
        val groups = getNonDeuceCards(cards).groupBy { it.rank }
        val isThreeOfAKind = groups.any { it.value.size == 3 || (it.value.size + deuceCount(cards) == 3) }
        return if(isThreeOfAKind){
            val nonDeuces = groups.maxByOrNull { it.value.size }?.value?.toMutableList()
            nonDeuces?.addAll(getDeuceCards(cards))
            Pair(nonDeuces ?: emptyList(),true)
        } else {
            Pair(emptyList(),false)
        }
    }

    fun isPair(cards: List<Card>): Pair<List<Card>,Boolean>{
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
}