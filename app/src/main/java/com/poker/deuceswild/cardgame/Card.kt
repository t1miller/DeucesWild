package com.poker.deuceswild.cardgame

import java.util.*

class Card(var rank: Int, var suit: Char) {

    enum class ParseError(val humanReadableError: String) {
        NOT_5_CARDS("Need a 5 card hand"),
        CARD_NOT_2_CHARS("Cards have two chars (e.g 3c th tc jh kh)"),
        RANK_WRONG("The rank is incorrect (e.g 23456789tjqka)"),
        SUIT_WRONG("The suit is incorrect (e.g shdc)"),
        DUPLICATE_CARDS("Cards should be unique"),
        NONE("None")
    }

    companion object {
        const val RANK = "23456789tjqka"
        const val SUITS = "shdc"

        fun parseHand(hand: String) : Pair<List<Card>, ParseError>{
            val cards = hand.split(" ")

            if(cards.size != 5){
                return Pair(emptyList(), ParseError.NOT_5_CARDS)
            }

            val handParsed = cards.map {
                val txt = it.toLowerCase(Locale.ROOT)
                if (txt.length != 2) {
                    return Pair(emptyList(), ParseError.CARD_NOT_2_CHARS)
                }
                val rank = txt[0]
                val suit = txt[1]
                if(rank !in RANK) {
                    return Pair(emptyList(), ParseError.RANK_WRONG)
                }
                if(suit !in SUITS) {
                    return Pair(emptyList(), ParseError.SUIT_WRONG)
                }
                Card(RANK.indexOf(rank) + 2, suit)
            }

            val isUniqueCards = handParsed.toSet().toList().size == 5

            if(!isUniqueCards) {
                return Pair(emptyList(), ParseError.DUPLICATE_CARDS)
            }

            return Pair(handParsed, ParseError.NONE)
        }
    }

    @Override
    override fun toString(): String {
        val tempRank =  if (rank == 1) 14 else rank
        return "${RANK.toCharArray()[tempRank-2]}${suit}"
    }

    @Override
    override fun equals(other: Any?): Boolean =
        (other is Card) &&
                rank == other.rank &&
                suit == other.suit

    override fun hashCode(): Int {
        var result = rank
        result = 31 * result + suit.hashCode()
        return result
    }
}