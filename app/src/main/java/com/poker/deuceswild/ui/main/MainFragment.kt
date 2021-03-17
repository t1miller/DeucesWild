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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.poker.deuceswild.R
import com.poker.deuceswild.StrategySuggestionRecyclerViewAdapter
import com.poker.deuceswild.ai.AIDecision
import com.poker.deuceswild.ai.Strategy
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Evaluate
import com.poker.deuceswild.handstatui.StatDialogUtils
import com.poker.deuceswild.log.LogManager
import com.poker.deuceswild.payout.PayTableManager
import com.poker.deuceswild.payout.PayTableType
import com.poker.deuceswild.settings.SettingsFragment
import com.poker.deuceswild.settings.SettingsUtils
import com.poker.deuceswild.sound.SoundManager
import com.wajahatkarim3.easyflipview.EasyFlipView
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        val NAME = SettingsFragment::class.java.simpleName

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

    private lateinit var adView: AdView

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
        adView = view.findViewById(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())
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
                    viewModel.gameState.value = MainViewModel.GameState.START
//                    viewModel.deal()
                }
                MainViewModel.GameState.EVALUATE_WIN -> {
                    // user is opting out of double down
//                    viewModel.collect()
                    viewModel.gameState.value = MainViewModel.GameState.START
                }
                else -> {}
            }
        }

        betMaxButton.setOnClickListener {
            viewModel.betMax()
//            StrategyTester.runSimulation()
        }

        betOneButton.setOnClickListener {
            viewModel.betOne()
        }

        doubleButton.setOnClickListener {
            viewModel.gameState.value = MainViewModel.GameState.BONUS
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

        training.setOnCheckedChangeListener { buttonView, isChecked ->
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

        autoHold.setOnCheckedChangeListener { buttonView, isChecked ->
            if(viewModel.gameState.value == MainViewModel.GameState.DEAL){
                if(!isChecked){
                    CardUiUtils.unhighlightHeldCards(holdViews)
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

        recyclerAdapter = StrategySuggestionRecyclerViewAdapter(strategyTips)
        with(recyclerView) {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.stackFromEnd = true
            layoutManager = linearLayout
            adapter = recyclerAdapter
        }
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            val sortedHands = decision.sortedRankedHands
            aiDecision = decision
            if(autoHold.isChecked) {
                CardUiUtils.highlightHeldCards(holdViews, viewModel.hand.value ,sortedHands[0].first)
            }
            SoundManager.playSound(requireActivity(), SoundManager.SoundType.CHIME)
            enableHandStats()
            enableDealButtonUi()
        })
    }

    private fun updateUi(state: MainViewModel.GameState) {
        val hand = viewModel.hand.value ?: emptyList()
        when (state) {
            MainViewModel.GameState.START -> {
                handStatsButton.isEnabled = true
                disableHandStats()
                clearWinningCardsUi()
                unHiglightRowsUi()
                clearWinningTextUi()
                clearTrainingView()
                enableBetting()
                showNormalButtonsUi()
                flip(CardFlipState.FACE_DOWN, hand)
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
            }
            MainViewModel.GameState.EVALUATE_WIN -> {
                flip(CardFlipState.FULL_FLIP, hand)
                updateTrainingView(getCardsToKeep())
                CardUiUtils.unhighlightHeldCards(holdViews)
                showBonusAndCollect()
                disableBetting()
            }
            MainViewModel.GameState.EVALUATE_NO_WIN -> {
                flip(CardFlipState.FULL_FLIP, hand)
                updateTrainingView(getCardsToKeep())
                CardUiUtils.unhighlightHeldCards(holdViews)
            }
            MainViewModel.GameState.BONUS -> {
                showRedAndBlack()
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
        val bestDecision = aiDecision?.sortedRankedHands?.get(0)?.first ?: emptyList()
        if(bestDecision.toMutableSet() ==  heldCards.toMutableSet() ||
            Strategy.bestStrategy(viewModel.originalHand).winningCards.toMutableSet() == heldCards.toMutableSet()) {
            trainingWrongText.visibility = View.INVISIBLE
            trainingCorrectText.visibility = View.VISIBLE
            LogManager.increaseCorrectCount()
        } else {
            trainingWrongText.visibility = View.VISIBLE
            trainingCorrectText.visibility = View.INVISIBLE
            LogManager.increaseIncorrectCount()
        }
        CardUiUtils.highlightHeldCards(trainingHoldViews, aiDecision?.hand,bestDecision)
        CardUiUtils.showCards(trainingCardViews,aiDecision?.hand)
        val stats = LogManager.getStatistics()
        trainingCorrectCountText.text = getString(R.string.correct_training, stats?.correctCount ?: 0)
        trainingWrongCountText.text = getString(R.string.wrong_training, stats?.wrongCount ?: 0)
        optimalText.text = getString(R.string.optimal, 10)
        stats?.let {
            accuracyText.text = getString(R.string.accuracy,it.correctCount.toFloat()/(it.wrongCount.toFloat() + it.correctCount))
        }
    }

    private fun clearTrainingView() {
        CardUiUtils.showCardBacks(trainingCardViews)
        CardUiUtils.unhighlightHeldCards(trainingHoldViews)
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

    private fun showBonusAndCollect() {
        dealButton.text = getString(R.string.collect_button)
        doubleButton.isEnabled = true
    }

    private fun showRedAndBlack() {
        normalButtonsLayout.visibility = View.INVISIBLE
        bonusButtonsLayout.visibility = View.VISIBLE
        gameInstructions.visibility = View.VISIBLE
        gameInstructions.text = getString(R.string.guess_the_color)
    }

    private fun showBonusDoneUi() {
        cardViews[2].setImageResource(CardUiUtils.cardToImage(viewModel.hand.value?.get(2)))
        cardLayouts[2].flipTheView()

        // if win show chicken dinner
//        if (viewModel.wonLostMoney.value!! > 0) {
//            handEvalText.text = getString(R.string.bonus_correct_guess)
//        } else {
//            handEvalText.text = getString(R.string.bonus_wrong_guess)
//        }
        showNormalButtonsUi()
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
        SoundManager.playSound(requireActivity(), SoundManager.SoundType.FLIP)

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
                    cardViews[i].setImageResource(CardUiUtils.cardToImage(cards[i]))
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
                                cardViews[i].setImageResource(CardUiUtils.cardToImage(cards[i]))
                            }
                            enableDealButtonUi()
                        }
                    }
                }
            }
        }
    }
}