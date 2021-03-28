package com.poker.deuceswild.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poker.deuceswild.R
import com.poker.deuceswild.StrategySuggestionRecyclerViewAdapter
import com.poker.deuceswild.ai.AIDecision
import com.poker.deuceswild.ai.Strategy
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Deck
import com.poker.deuceswild.cardgame.Evaluate
import com.poker.deuceswild.cardgame.ui.AdHelper
import com.poker.deuceswild.cardgame.ui.CardUiUtils
import com.poker.deuceswild.cardgame.ui.PayTableUiUtils
import com.poker.deuceswild.cardgame.ui.TipDialog
import com.poker.deuceswild.handstatui.StatDialogUtils
import com.poker.deuceswild.log.LogManager
import com.poker.deuceswild.payout.PayTableManager
import com.poker.deuceswild.payout.PayTableType
import com.poker.deuceswild.settings.SettingsUtils
import com.poker.deuceswild.sound.SoundManager
import com.wajahatkarim3.easyflipview.EasyFlipView
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        val NAME = MainFragment::class.java.simpleName

        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var payTableLayout: TableLayout
    private var highLightedRow = -1

    private var cardLayouts: MutableList<EasyFlipView> = mutableListOf()

    private var cardViews: MutableList<ImageView> = mutableListOf()

    private var trainingCardViews: MutableList<ImageView> = mutableListOf()

    private var holdViews: MutableList<TextView> = mutableListOf()

    private var trainingHoldViews: MutableList<TextView> = mutableListOf()

    private lateinit var dealButton: Button

    private lateinit var betMaxButton: Button

    private lateinit var betOneButton: Button

    private lateinit var doubleButton: Button

    private lateinit var redButton: Button

    private lateinit var blackButton: Button

    private lateinit var bonusButtonsLayout: ConstraintLayout

    private lateinit var normalButtonsLayout: ConstraintLayout

    private lateinit var handStatsButton: Button

    private lateinit var runButton: Button

    private lateinit var winningHandText: TextView

    private lateinit var strategyTitle: TextView

    private lateinit var totalMoneyText: TextView

    private lateinit var wonLossText: TextView

    private lateinit var betText: TextView

    private lateinit var trainingCorrectText: TextView

    private lateinit var trainingWrongText: TextView

    private lateinit var trainingCorrectCountText: TextView

    private lateinit var trainingWrongCountText: TextView

    private lateinit var accuracyText: TextView

    private lateinit var winningsText: TextView

    private lateinit var optimalText: TextView

    private lateinit var gameInstructions: TextView

    private lateinit var trainingView: ConstraintLayout

    private lateinit var strategyView: ConstraintLayout

    private lateinit var showTip: CheckBox

    private lateinit var autoHold: CheckBox

    private lateinit var training: CheckBox

    private lateinit var handStatsLayout: LinearLayout

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerAdapter: StrategySuggestionRecyclerViewAdapter

    private var strategyTips = mutableListOf<String>()

    private var aiDecision: AIDecision? = null

//    private lateinit var bestEVText: TextView
//
//    private lateinit var currentEVText: TextView

    enum class CardFlipState {
        FACE_UP,
        FACE_DOWN,
        FULL_FLIP
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.main_fragment, container, false)
        payTableLayout = view.findViewById(R.id.payoutTable)
        dealButton = view.findViewById(R.id.Deal)
        betMaxButton = view.findViewById(R.id.betmax)
        betOneButton = view.findViewById(R.id.betone)
        winningHandText = view.findViewById(R.id.winningHandText)
        showTip = view.findViewById(R.id.showTip)
        recyclerView = view.findViewById(R.id.recyclerView)
        strategyTitle = view.findViewById(R.id.strategyTitle)
        autoHold = view.findViewById(R.id.autoHold)
        totalMoneyText = view.findViewById(R.id.totalText)
        wonLossText = view.findViewById(R.id.wonLossText)
        betText = view.findViewById(R.id.betText)
        handStatsButton = view.findViewById(R.id.best)
        handStatsLayout = view.findViewById(R.id.simulateView)
        runButton = view.findViewById(R.id.simulate)
        trainingView = view.findViewById(R.id.trainingView)
        training = view.findViewById(R.id.trainingMode)
        strategyView = view.findViewById(R.id.strategyView)
        trainingCorrectCountText = view.findViewById(R.id.correctCountText)
        trainingWrongCountText = view.findViewById(R.id.wrongCountText)
        trainingCorrectText = view.findViewById(R.id.trainingcorrect)
        trainingWrongText = view.findViewById(R.id.trainingWrong)
        accuracyText = view.findViewById(R.id.accuracyText)
        winningsText = view.findViewById(R.id.trainingWinningText)
        optimalText = view.findViewById(R.id.trainingOptimalText)
        redButton = view.findViewById(R.id.doubleDownRed)
        blackButton = view.findViewById(R.id.doubleDownBlack)
        doubleButton = view.findViewById(R.id.doubleDown)
        bonusButtonsLayout = view.findViewById(R.id.bonusButtons)
        normalButtonsLayout = view.findViewById(R.id.buttons)
        gameInstructions = view.findViewById(R.id.gameInstructions)

        cardLayouts.add(view.findViewById(R.id.card1layout))
        cardLayouts.add(view.findViewById(R.id.card2layout))
        cardLayouts.add(view.findViewById(R.id.card3layout))
        cardLayouts.add(view.findViewById(R.id.card4layout))
        cardLayouts.add(view.findViewById(R.id.card5layout))

        cardViews.add(view.findViewById(R.id.cardfront1))
        cardViews.add(view.findViewById(R.id.cardfront2))
        cardViews.add(view.findViewById(R.id.cardfront3))
        cardViews.add(view.findViewById(R.id.cardfront4))
        cardViews.add(view.findViewById(R.id.cardfront5))

        val cardBackImage = SettingsUtils.getCardBack(requireContext())
        val cardback1: View = view.findViewById(R.id.back1)
        cardback1.findViewById<ImageView>(R.id.cardback1).setImageResource(cardBackImage)
        val cardback2: View = view.findViewById(R.id.back2)
        cardback2.findViewById<ImageView>(R.id.cardback1).setImageResource(cardBackImage)
        val cardback3: View = view.findViewById(R.id.back3)
        cardback3.findViewById<ImageView>(R.id.cardback1).setImageResource(cardBackImage)
        val cardback4: View = view.findViewById(R.id.back4)
        cardback4.findViewById<ImageView>(R.id.cardback1).setImageResource(cardBackImage)
        val cardback5: View = view.findViewById(R.id.back5)
        cardback5.findViewById<ImageView>(R.id.cardback1).setImageResource(cardBackImage)

        holdViews.add(view.findViewById(R.id.card1Hold))
        holdViews.add(view.findViewById(R.id.card2Hold))
        holdViews.add(view.findViewById(R.id.card3Hold))
        holdViews.add(view.findViewById(R.id.card4Hold))
        holdViews.add(view.findViewById(R.id.card5Hold))

        trainingHoldViews.add(view.findViewById(R.id.trainingCard1Hold))
        trainingHoldViews.add(view.findViewById(R.id.trainingCard2Hold))
        trainingHoldViews.add(view.findViewById(R.id.trainingCard3Hold))
        trainingHoldViews.add(view.findViewById(R.id.trainingCard4Hold))
        trainingHoldViews.add(view.findViewById(R.id.trainingCard5Hold))

        trainingCardViews.add(view.findViewById(R.id.card1))
        trainingCardViews.add(view.findViewById(R.id.card2))
        trainingCardViews.add(view.findViewById(R.id.card3))
        trainingCardViews.add(view.findViewById(R.id.card4))
        trainingCardViews.add(view.findViewById(R.id.card5))


        populatePayTable(SettingsUtils.getPayoutTable(context))

        for (i in 0..4) {
            cardViews[i].setOnClickListener {
                when(viewModel.gameState.value){
                    MainViewModel.GameState.DEAL -> {
                        CardUiUtils.toggleHighlightHeldCards(holdViews,i)
                    }
                    else -> {}
                }
            }
        }

        dealButton.setOnClickListener {
            disableDealButtonUi()
//            dismissTrainingModeScreenUi()
            when (viewModel.gameState.value) {
                MainViewModel.GameState.START -> {
                    viewModel.deal()
                }
                MainViewModel.GameState.DEAL -> {
                    viewModel.evaluateHand(getCardsToKeepBooleanArray(),getCardsToKeep())
                }
                MainViewModel.GameState.EVALUATE_NO_WIN -> {
                    // user lost
                    viewModel.gameState.value = MainViewModel.GameState.START
                }
                MainViewModel.GameState.EVALUATE_WIN -> {
                    // user is opting out of double down
                    SoundManager.playSound(requireContext(), SoundManager.SoundType.COLLECTING_COINS)
                    viewModel.collectNoBonus()
                    viewModel.gameState.value = MainViewModel.GameState.START
                }
                MainViewModel.GameState.BONUS -> {
                    // todo add some fun music for bonus mode
                    viewModel.gameState.value = MainViewModel.GameState.START
//                    CardUiUtils.showCardBacks(cardViews)
                    CardUiUtils.makeCardsVisibile(cardViews)
                    viewModel.deal()
                    flip(CardFlipState.FULL_FLIP, viewModel.hand.value?.toList() ?: emptyList())
                }
                else -> {}
            }
        }

        betMaxButton.setOnClickListener {
            SoundManager.playSound(requireActivity(), SoundManager.SoundType.INSERT_COIN)
            viewModel.betMax()
//            StrategyTester.runSimulation()
        }

        betOneButton.setOnClickListener {
            SoundManager.playSound(requireActivity(), SoundManager.SoundType.INSERT_COIN)
            viewModel.betOne()
        }

        doubleButton.setOnClickListener {
            viewModel.gameState.value = MainViewModel.GameState.BONUS
        }

        redButton.setOnClickListener {
            viewModel.collectBonus(true)
            showBonusDoneUi()
        }

        blackButton.setOnClickListener {
            viewModel.collectBonus(false)
            showBonusDoneUi()
        }

        showTip.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                training.isChecked = false
                showActivePaytableColumns()
                showHandStats()
                showTipsView()
                hideTrainingView()
            } else {
                showFullPaytableColumns()
            }
        }

        training.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                showTip.isChecked = false
                showActivePaytableColumns()
                showTrainingView()
                hideHandStats()
                hideTipsView()
            } else {
                showFullPaytableColumns()
            }
        }

        autoHold.setOnCheckedChangeListener { _, isChecked ->
            if(viewModel.gameState.value == MainViewModel.GameState.DEAL){
                if(!isChecked){
                    CardUiUtils.unhighlightHeldCards(holdViews)
                } else {
                    val strategy = Strategy.bestStrategy(viewModel.hand.value ?: emptyList())
                    CardUiUtils.highlightHeldCards(holdViews,strategy.fullCards, strategy.winningCards)
                }
            }
        }

        handStatsButton.setOnClickListener {
            StatDialogUtils.showDialog(
                    requireActivity(),
                    viewModel.aiDecision.value,
                    viewModel.aiDecision.value?.hand,
                    getCardsToKeep(),
                    viewModel.lookupExpectedValue(getCardsToKeep())
            )
        }

        runButton.setOnClickListener {
            if(viewModel.gameState.value != MainViewModel.GameState.DEAL){
                Toast.makeText(requireContext(), "Deal first", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Running simulations", Toast.LENGTH_LONG).show()
                viewModel.getBestHand()
            }
        }

        val helpTraining = view.findViewById<ImageView>(R.id.helpTraining)
        helpTraining.setOnClickListener {
            TipDialog.showDialog(requireContext(),getString(R.string.training_tip_title), getString(R.string.training_tip_desc) )
        }
        val helpStrategy = view.findViewById<ImageView>(R.id.helpStrategy)
        helpStrategy.setOnClickListener {
            TipDialog.showDialog(requireContext(),getString(R.string.strategy_tip_title), getString(R.string.strategy_tip_desc) )
        }

        recyclerAdapter = StrategySuggestionRecyclerViewAdapter(strategyTips)
        with(recyclerView) {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.stackFromEnd = true
            layoutManager = linearLayout
            adapter = recyclerAdapter
        }

        AdHelper.setupAd(requireActivity(),view,"ca-app-pub-7137320034166109/4056911260")
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        SoundManager.load(requireActivity())

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.gameState.observe(viewLifecycleOwner, Observer { state ->
            updateUi(state)
        })

        viewModel.bet.observe(viewLifecycleOwner, Observer { currentBet ->
            currentBet?.let {
                updateHighlightedColumnUi(currentBet)
                updateBetUi(currentBet)
                if(showTip.isChecked) {
                    showFullPaytableColumns()
                    showActivePaytableColumns()
                    showTipsView()
                } else if (training.isChecked) {
                    showFullPaytableColumns()
                    showActivePaytableColumns()
                    showTrainingView()
                }
            }
        })

        viewModel.eval.observe(viewLifecycleOwner, Observer { eval ->
            eval?.let {
                updateWinningCardsUi(eval.second)
                updateWinningTextUi(eval.first)
                updateHighlightedRowUi(eval.first)
            }
        })

        viewModel.totalMoney.observe(viewLifecycleOwner, Observer { totalMoney ->
            totalMoney?.let {
                updateTotalUi(it)
            }
        })

        viewModel.wonLoss.observe(viewLifecycleOwner, Observer { wonLoss ->
            wonLoss?.let {
                updateWinLossUi(it)
            }
        })

        viewModel.aiDecision.observe(viewLifecycleOwner, Observer { decision ->
            aiDecision = decision
//            val sortedHands = decision.sortedRankedHands
//            if(autoHold.isChecked && viewModel.gameState.value == MainViewModel.GameState.DEAL) {
//                CardUiUtils.highlightHeldCards(holdViews, viewModel.hand.value, sortedHands[0].first)
//                SoundManager.playSound(requireActivity(), SoundManager.SoundType.CHIME)
//            }
            enableHandStats()
            enableDealButtonUi()
        })
    }

    private fun updateUi(state: MainViewModel.GameState) {
        val hand = viewModel.hand.value ?: emptyList()
        when (state) {
            MainViewModel.GameState.START -> {
                handStatsButton.isEnabled = true
                CardUiUtils.unhighlightHeldCards(holdViews)
                CardUiUtils.makeCardsVisibile(cardViews)
                disableHandStats()
                clearWinningCardsUi()
                unHiglightRowsUi()
                clearWinningTextUi()
                clearTrainingView()
                enableBetting()
                showNormalButtonsUi()
                updateTrainingViewStats()
                flip(CardFlipState.FACE_DOWN, hand)
                PayTableUiUtils.unblink(payTableLayout)
            }
            MainViewModel.GameState.DEAL -> {
                flip(CardFlipState.FACE_UP, hand)
                val bestStrategy = Strategy.bestStrategy(hand)
                updateTipRecyclerView(bestStrategy.winningCards, bestStrategy.tipsRuledOut)
                if(autoHold.isChecked) {
                    CardUiUtils.highlightHeldCards(
                            holdViews,
                            bestStrategy.fullCards,
                            bestStrategy.winningCards
                    )
                }
                showTapToHold()
                disableBetting()
            }
            MainViewModel.GameState.EVALUATE_WIN -> {
                flip(CardFlipState.FULL_FLIP, hand)
                updateTrainingView(getCardsToKeep())
                CardUiUtils.unhighlightHeldCards(holdViews)
                showBonusAndCollect()
                disableBetting()
                hideTapToHold()
                SoundManager.playSound(requireContext(),viewModel.eval.value?.first ?: Evaluate.Hand.NOTHING)
                PayTableUiUtils.blinkRow(requireContext(), payTableLayout, viewModel.eval.value?.first?.ordinal)
            }
            MainViewModel.GameState.EVALUATE_NO_WIN -> {
                flip(CardFlipState.FULL_FLIP, hand)
                updateTrainingView(getCardsToKeep())
                CardUiUtils.unhighlightHeldCards(holdViews)
                hideTapToHold()
                SoundManager.playSound(requireContext(),viewModel.eval.value?.first ?: Evaluate.Hand.NOTHING)
            }
            MainViewModel.GameState.BONUS -> {
                showRedAndBlack()
                CardUiUtils.unhighlightHeldCards(holdViews)
                clearWinningTextUi()
            }
        }
    }

    private fun showTrainingView() {
        trainingView.visibility = View.VISIBLE
    }

    private fun hideTrainingView() {
        trainingView.visibility = View.GONE
    }

    private fun showHandStats() {
        handStatsLayout.visibility = View.VISIBLE
    }

    private fun hideHandStats() {
        handStatsLayout.visibility = View.GONE
    }

    private fun showTipsView() {
        strategyView.visibility = View.VISIBLE
    }

    private fun hideTipsView() {
        strategyView.visibility = View.GONE
    }

    private fun disableDealButtonUi() {
        dealButton.isEnabled = false
    }

    private fun enableDealButtonUi() {
        dealButton.isEnabled = true
    }

    private fun disableBetting() {
        betOneButton.isEnabled = false
        betMaxButton.isEnabled = false
    }

    private fun enableBetting() {
        betOneButton.isEnabled = true
        betMaxButton.isEnabled = true
    }

    private fun updateTrainingView(heldCards: List<Card>) {
        if (training.isChecked) {
            val bestDecisionAi = aiDecision?.sortedRankedHands?.get(0)?.first ?: emptyList()
            val bestDecisionStrategy = Strategy.bestStrategy(viewModel.originalHand)

            if (bestDecisionAi.toMutableSet() == heldCards.toMutableSet() ||
                    bestDecisionStrategy.winningCards.toMutableSet() == heldCards.toMutableSet()) {
                Timber.d("Training correct optimal = ${bestDecisionStrategy.winningCards.toMutableSet()} your = ${heldCards.toMutableSet()}")
                trainingWrongText.visibility = View.INVISIBLE
                trainingCorrectText.visibility = View.VISIBLE
                LogManager.increaseCorrectCount()
                LogManager.increaseOptimalWonLoss(PayTableManager.getPayOut(SettingsUtils.getPayoutTable(requireContext()),viewModel.eval.value?.first,1))
            } else {
                Timber.d("Training incorrect optimal = ${bestDecisionStrategy.winningCards.toMutableSet()} your = ${heldCards.toMutableSet()}")
                Timber.d("optimal ranks: ${bestDecisionStrategy.winningCards.toMutableSet().joinToString { it.rank.toString() }} your ranks: ${heldCards.toMutableSet().joinToString { it.rank.toString() }}")
                trainingWrongText.visibility = View.VISIBLE
                trainingCorrectText.visibility = View.INVISIBLE
                LogManager.increaseIncorrectCount()
                val bestDecisionHand = Deck.draw5Random(bestDecisionStrategy.winningCards.toMutableList())
                val bestDecisionEval = Evaluate.evaluate(bestDecisionHand)
                LogManager.increaseOptimalWonLoss(PayTableManager.getPayOut(SettingsUtils.getPayoutTable(requireContext()),bestDecisionEval.first,1))
            }
            LogManager.increaseTrainingWonLoss(PayTableManager.getPayOut(SettingsUtils.getPayoutTable(requireContext()),viewModel.eval.value?.first,1))

            CardUiUtils.highlightHeldCards(trainingHoldViews, bestDecisionStrategy.fullCards, bestDecisionStrategy.winningCards)
            CardUiUtils.showCards(requireContext(), trainingCardViews, bestDecisionStrategy.fullCards)
            updateTrainingViewStats()
        }
    }

    private fun updateTrainingViewStats() {
        val stats = LogManager.getStatistics()
        trainingCorrectCountText.text = getString(R.string.correct_training, stats?.correctCount ?: 0)
        trainingWrongCountText.text = getString(R.string.wrong_training, stats?.wrongCount ?: 0)
        stats?.let {
            accuracyText.text = getString(R.string.accuracy, LogManager.getAccuracy())
            winningsText.text = getString(R.string.winnings, stats.trainingWonLoss)
            optimalText.text = getString(R.string.optimal, stats.trainingOptimalWonLoss)
        }

    }

    private fun clearTrainingView() {
        CardUiUtils.showCardBacks(requireContext(),trainingCardViews)
        CardUiUtils.unhighlightHeldCards(trainingHoldViews)
        trainingWrongText.visibility = View.INVISIBLE
        trainingCorrectText.visibility = View.INVISIBLE
    }

    private fun disableHandStats() {
        handStatsButton.isEnabled = false
    }

    private fun enableHandStats() {
        handStatsButton.isEnabled = true
    }

    private fun updateWinningTextUi(first: Evaluate.Hand) {
        winningHandText.visibility = View.VISIBLE
        winningHandText.text = first.readableName
    }

    private fun clearWinningTextUi() {
        winningHandText.visibility = View.INVISIBLE
        winningHandText.text = ""
    }

    private fun updateBetUi(bet: Int){
        betText.text = getString(R.string.bet, bet)
    }

    private fun updateTotalUi(total: Int){
        totalMoneyText.text = getString(R.string.total, total)
    }

    private fun updateWinLossUi(money: Int){
        if(money < 0){
            wonLossText.text = getString(R.string.loss, money)
        } else {
            wonLossText.text = getString(R.string.won, money)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }

    private fun populatePayTable(type: PayTableType) {
        for ((i, row) in payTableLayout.children.withIndex()) {
            val payRowValues = PayTableManager.getPayTableRow(type, i)
            for((j, tv) in (row as TableRow).children.withIndex()){
                if(j != 0) {
                    (tv as TextView).text = payRowValues[j - 1].toString()
                } else {
                    (tv as TextView).text = Evaluate.Hand.values()[i].readableName
                }
            }
        }
    }

    private fun showActivePaytableColumns() {
        payTableLayout.children.forEach {
            val row = it as TableRow
            row.forEachIndexed { index, view ->
                if(index != viewModel.bet.value && index != 0){
                    view.visibility = View.GONE
                }
            }
        }
    }

    private fun showFullPaytableColumns() {
        hideTrainingView()
        hideHandStats()
        hideTipsView()
        payTableLayout.children.forEach {
            val row = it as TableRow
            row.forEach {
                it.visibility = View.VISIBLE
            }
        }
    }

    private fun showTapToHold() {
        gameInstructions.text = getString(R.string.tap_card)
        gameInstructions.visibility = View.VISIBLE
    }

    private fun hideTapToHold() {
        gameInstructions.text = getString(R.string.tap_card)
        gameInstructions.visibility = View.INVISIBLE
    }

    private fun showBonusAndCollect() {
        dealButton.text = getString(R.string.collect_button)
        doubleButton.isEnabled = true
    }

    private fun showRedAndBlack() {
        normalButtonsLayout.visibility = View.INVISIBLE
        bonusButtonsLayout.visibility = View.VISIBLE
        gameInstructions.visibility = View.VISIBLE
        gameInstructions.text = getString(R.string.guess_the_color)
        CardUiUtils.showCardBacks(requireContext(),cardViews)
        cardLayouts[2].isAutoFlipBack = false
        cardLayouts[2].flipTheView()
        for(i in 0..4){
            if(i!=2){
                cardViews[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun showBonusDoneUi() {
        cardViews[2].setImageResource(CardUiUtils.cardToImage(requireContext(),viewModel.hand.value?.get(2)))
        cardLayouts[2].flipTheView()

        if (viewModel.wonLoss.value ?: 0 > 0) {
            gameInstructions.text = getString(R.string.bonus_correct_guess)
        } else {
            gameInstructions.text = getString(R.string.bonus_wrong_guess)
        }
        enableBetting()
        showNormalButtonsUi()
        gameInstructions.visibility = View.VISIBLE
    }

    private fun showNormalButtonsUi() {
//        enableBetChangingUi()
        dealButton.text = getString(R.string.deal_button)
        doubleButton.isEnabled = false
        normalButtonsLayout.visibility = View.VISIBLE
        bonusButtonsLayout.visibility = View.INVISIBLE
        gameInstructions.visibility = View.INVISIBLE
    }

    private fun updateTipRecyclerView(cards: List<Card>, tips: List<String>){
        strategyTips.clear()
        strategyTips.addAll(tips)
        recyclerAdapter.notifyDataSetChanged()
        strategyTitle.text = "Strategy ${Evaluate.deuceCount(cards)} deuces"
        recyclerView.scrollToPosition(strategyTips.size-1)
    }

    private fun updateWinningCardsUi(winningCards: List<Card>) {
        clearWinningCardsUi()
        Timber.d("winning cards: $winningCards")
        for (i in 0..4){
            if(viewModel.hand.value?.get(i) in winningCards){
                cardViews[i].setBackgroundResource(R.color.colorYellow)
            }
        }
    }

    private fun clearWinningCardsUi(){
        cardViews.forEach{it.background = null}
    }

    private fun updateHighlightedColumnUi(bet: Int) {
        // unhighlight higlighted columns
        for(row in payTableLayout.children) {
            val columns = (row as ViewGroup)
            for(i in 1..5) {
                columns[i].setBackgroundResource(R.color.colorDarkBlue)
            }
        }

        // highlight column
        for (row in payTableLayout.children) {
            for (columnIndex in 0 until ((row as ViewGroup).childCount)) {
                if (columnIndex == bet) {
                    row[columnIndex].setBackgroundResource(R.color.colorRed)
                }
            }
        }
    }

    private fun getCardsToKeepBooleanArray() : BooleanArray {
        val cardsHeld = holdViews.map { it.visibility == View.VISIBLE }.toMutableList()
        return cardsHeld.toBooleanArray()
    }

    private fun getCardsToKeep() : List<Card>{
        val cardsHeldBool = getCardsToKeepBooleanArray()
        val  filteredHand = viewModel.hand.value?.toList()?.filterIndexed { index, _ ->
            cardsHeldBool[index]
        }
        return filteredHand ?: emptyList()
    }

    private fun updateHighlightedRowUi(handEval: Evaluate.Hand) {
        val rowIndex = handEval.ordinal
        if(rowIndex < payTableLayout.childCount) {
            payTableLayout[rowIndex].setBackgroundResource(R.color.colorRed)
            highLightedRow = rowIndex
            for(rowElement in (payTableLayout[rowIndex] as TableRow).children) {
                rowElement.setBackgroundResource(R.color.colorRed)
            }
        }
    }

    private fun unHiglightRowsUi() {
        if (highLightedRow >= 0) {
            payTableLayout[highLightedRow].setBackgroundResource(R.color.colorDarkBlue)
            for ((i,rowElement) in (payTableLayout[highLightedRow] as TableRow).children.withIndex()) {
                if (i != getHighlightedColumn()) {
                    rowElement.setBackgroundResource(R.color.colorDarkBlue)
                }
            }
        }
    }

    private fun getHighlightedColumn() : Int {
        return viewModel.bet.value ?: 1 - 1
    }



    private fun flip(state: CardFlipState, cards: List<Card>) {
        if(SettingsUtils.isFlipSoundEnabled(requireContext())){
            SoundManager.playSound(requireActivity(), SoundManager.SoundType.FLIP)
        }

        when(state) {
            CardFlipState.FACE_DOWN -> {
                for (i in 0..4) {
                    cardLayouts[i].isAutoFlipBack = false
                    cardLayouts[i].flipTheView()
                    cardLayouts[i].setOnFlipListener { _, _ ->
                        enableDealButtonUi()
                    }
                }
            }
            CardFlipState.FACE_UP -> {
                for (i in 0..4) {
                    cardLayouts[i].isAutoFlipBack = false
                    cardViews[i].setImageResource(CardUiUtils.cardToImage(requireContext(),cards[i]))
                    cardLayouts[i].flipTheView()
                    cardLayouts[i].setOnFlipListener { _, _ ->
                        enableDealButtonUi()
                    }
//                    }
                }
            }
            CardFlipState.FULL_FLIP -> {
                if(getCardsToKeepBooleanArray().filter { it }.size == 5) enableDealButtonUi()
                for (i in 0..4) {
                    cardLayouts[i].isAutoFlipBack = true
                    if (!getCardsToKeepBooleanArray()[i]) {
                        cardLayouts[i].autoFlipBackTime = 0
                        cardLayouts[i].flipTheView()
                        cardLayouts[i].setOnFlipListener { _, newCurrentSide ->
                            if (newCurrentSide == EasyFlipView.FlipState.BACK_SIDE) {
                                cardViews[i].setImageResource(CardUiUtils.cardToImage(requireContext(),cards[i]))
                            }
                            enableDealButtonUi()
                        }
                    }
                }
            }
        }
    }
}