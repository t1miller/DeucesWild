package com.poker.deuceswild.handstatui

import com.poker.deuceswild.cardgame.Card

data class HandStat(val recommendedHand: List<Card>, val fullHand: List<Card>, val expectedValue: Double)