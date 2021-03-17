package com.poker.deuceswild.payout

import com.poker.deuceswild.cardgame.Evaluate


enum class PayTableType(val readableName: String) {
    _101_28("101.28% Sequential Royal Flush"),
    _100_76("100.76% Full Pay"),
    _100_36("100.36% 1-2-2-3-5-8-15-25-200-800"),
    _99_89("99.89% 1-2-2-3-5-9-15-20-200-800"),
    _99_81("99.81% 1-2-2-3-5-9-12-25-200-800"),
    _98_94("98.94% 1-2-2-3-5-9-12-20-200-800"),
    _96_77("96.77% Colorado Deuces"),
    _94_82("94.82% 1-2-2-3-4-10-15-25-200-800"),
    _94_34("94.34% 1-2-2-3-4-9-15-25-200-800"),
    _92_05("92.05% 1-2-2-3-4-8-12-20-200-800")
    //don't change order
}

object PayTableManager {

    private var payTableLookup = mutableMapOf<PayTableType, MutableMap<Evaluate.Hand, List<Int>>>()

    init {
        payTableLookup[PayTableType._101_28] = mutableMapOf(
            Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(12000,24000,36000,48000,60000),
            Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
            Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
            Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
            Evaluate.Hand.FIVE_OF_A_KIND to listOf(15,30,45,60,75),
            Evaluate.Hand.STRAIGHT_FLUSH to listOf(9,18,27,36,45),
            Evaluate.Hand.FOUR_OF_A_KIND to listOf(5,10,15,20,25),
            Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
            Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
            Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
            Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._100_76] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(15,30,45,60,75),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(9,18,27,36,45),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(5,10,15,20,25),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._100_36] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(15,30,45,60,75),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(8,16,24,32,40),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(5,10,15,20,25),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._99_89] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(20,40,60,80,100),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(15,30,45,60,75),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(9,18,27,36,45),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(5,10,15,20,25),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._99_81] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(12,24,36,48,60),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(9,18,27,36,45),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(5,10,15,20,25),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._98_94] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(20,40,60,80,100),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(12,24,36,48,60),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(9,18,27,36,45),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(5,10,15,20,25),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._96_77] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(16,32,48,64,80),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(13,26,39,52,65),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(4,8,12,16,20),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._94_82] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(15,30,45,60,75),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(10,20,30,40,50),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(4,8,12,16,20),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._94_34] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(25,50,75,100,125),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(15,30,45,60,75),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(9,18,27,36,45),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(4,8,12,16,20),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )

        payTableLookup[PayTableType._92_05] = mutableMapOf(
                Evaluate.Hand.SEQUENTIAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.NATURAL_ROYAL_FLUSH to listOf(800,1600,2400,3200,4000),
                Evaluate.Hand.FOUR_DEUCES to listOf(200,400,600,800,1000),
                Evaluate.Hand.WILD_ROYAL_FLUSH to listOf(20,40,60,80,100),
                Evaluate.Hand.FIVE_OF_A_KIND to listOf(12,24,36,48,60),
                Evaluate.Hand.STRAIGHT_FLUSH to listOf(8,16,24,32,40),
                Evaluate.Hand.FOUR_OF_A_KIND to listOf(4,8,12,16,20),
                Evaluate.Hand.FULL_HOUSE to listOf(3,6,9,12,15),
                Evaluate.Hand.FLUSH to listOf(2,4,6,8,10),
                Evaluate.Hand.STRAIGHT to listOf(2,4,6,8,10),
                Evaluate.Hand.THREE_OF_A_KIND to listOf(1,2,3,4,5)
        )
    }

    fun getPayOut(payTableType: PayTableType, eval: Evaluate.Hand?, bet: Int?) : Int{
        val betamount = bet ?: 1
        return payTableLookup[payTableType]?.get(eval)?.get(betamount-1) ?: -1*betamount
    }

    fun getPayOut(eval: Evaluate.Hand?) : Int{
        return payTableLookup[PayTableType._101_28]?.get(eval)?.get(0) ?: -1
    }

    private fun getPayTableRow(payTableType: PayTableType, eval: Evaluate.Hand) : List<Int> {
        return payTableLookup[payTableType]?.get(eval) ?: listOf(0,0,0,0,0)
    }

    fun getPayTableRow(payTableType: PayTableType, index: Int) : List<Int> {
        val hand = Evaluate.Hand.values()[index]
        return getPayTableRow(payTableType, hand)
    }
}