package com.poker.deuceswild.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.poker.deuceswild.ai.Strategy
import com.poker.deuceswild.ai.StrategyTester
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Evaluate
import com.poker.deuceswild.payout.PayTableManager
import com.poker.deuceswild.payout.PayTableType
import com.poker.deuceswild.settings.SettingsFragment
import com.poker.deuceswild.sound.SoundManager
import com.wajahatkarim3.easyflipview.EasyFlipView
import org.w3c.dom.Text
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        val NAME = SettingsFragment::class.java.simpleName

        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var payTableLayout: TableLayout

    private var cardLayouts: MutableList<EasyFlipView> = mutableListOf()

    private var cardViews: MutableList<ImageView> = mutableListOf()

    private var holdViews: MutableList<TextView> = mutableListOf()

    private lateinit var dealButton: Button

    private lateinit var dealMaxButton: Button

    private lateinit var dealOneButton: Button

    private lateinit var winningHandText: TextView

    private lateinit var strategyTitle: TextView

    private lateinit var showTip: CheckBox

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerAdapter: StrategySuggestionRecyclerViewAdapter

    private var strategyTips = mutableListOf<String>()

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
        dealMaxButton = view.findViewById(R.id.betmax)
        dealOneButton = view.findViewById(R.id.betone)
        winningHandText = view.findViewById(R.id.winningHandText)
        showTip = view.findViewById(R.id.showTip)
        recyclerView = view.findViewById(R.id.recyclerView)
        strategyTitle = view.findViewById(R.id.strategyTitle)

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

        populatePayTable(PayTableType._101_28)

        dealButton.setOnClickListener {
            viewModel.deal()
        }

        dealMaxButton.setOnClickListener {
            viewModel.betMax()
            StrategyTester.runSimulation()
        }

        dealOneButton.setOnClickListener {
            viewModel.betOne()
        }

        showTip.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                removeInActivePaytableColumns()
            } else {
                showFullPaytableColumns()
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
//            updateUi(state)
            when (state) {
                MainViewModel.GameState.START -> {
                    flip(CardFlipState.FACE_DOWN, viewModel.hand.value ?: emptyList())
                }
                MainViewModel.GameState.DEAL -> {
                    flip(CardFlipState.FULL_FLIP, viewModel.hand.value ?: emptyList())
                    val fullHand = viewModel.hand.value?.toList() ?: emptyList()
                    updateTip(Strategy.bestStrategy(fullHand), fullHand)
                }
                else -> {}
            }
        })

        viewModel.bet.observe(viewLifecycleOwner, Observer { currentBet ->
            currentBet?.let {
                updateHighlightedColumnUi(currentBet)
//                    updateBetText(currentBet)
            }
        })
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

    private fun removeInActivePaytableColumns() {
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
        payTableLayout.children.forEach {
            val row = it as TableRow
            row.forEach {
                it.visibility = View.VISIBLE
            }
        }
    }

    private fun updateTip(strategyResponse: Strategy.StrategyResponse, fullCards: List<Card>) {
        updateWinningCardsUi(strategyResponse.winningCards, fullCards)
        CardUiUtils.highlightHeldCards(holdViews, fullCards, strategyResponse.winningCards)
        updateTipView(fullCards,strategyResponse.tipsRuledOut)
    }

    private fun updateTipView(cards: List<Card>, tips: List<String>){
        strategyTips.clear()
        strategyTips.addAll(tips)
        recyclerAdapter.notifyDataSetChanged()
        strategyTitle.text = "Strategy ${Strategy.deuceCount(cards)} deuces"
    }

    private fun updateWinningCardsUi(winningCards: List<Card>, fullCards: List<Card>) {
        cardViews.forEach { it.background = null }
        fullCards.forEachIndexed { index, card ->
            if(card in winningCards){
                cardViews[index].setBackgroundResource(R.color.colorYellow)
            }
        }
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


    private fun flip(state: CardFlipState, cards: List<Card>) {
        SoundManager.playSound(requireActivity(), SoundManager.SoundType.FLIP)
        Timber.d("Flip State: %s", state)

        when(state) {
            CardFlipState.FACE_DOWN -> {
                for (i in 0..4) {
                    cardLayouts[i].isAutoFlipBack = false
                    cardLayouts[i].flipTheView()
                    cardLayouts[i].setOnFlipListener { _, _ ->
//                        enableDealButtonUi()
                    }
                }
            }
            CardFlipState.FACE_UP -> {
                for (i in 0..4) {
                    cardLayouts[i].isAutoFlipBack = false
//                    if(!viewModel.getKeptCardIndeces()[i]) {
                    cardViews[i].setImageResource(CardUiUtils.cardToImage(cards[i]))
                    cardLayouts[i].flipTheView()
                    cardLayouts[i].setOnFlipListener { _, _ ->
//                            enableDealButtonUi()
                    }
//                    }
                }
            }
            CardFlipState.FULL_FLIP -> {
                for (i in 0..4) {
                    cardLayouts[i].isAutoFlipBack = true
                    if (!viewModel.getKeptCardIndeces()[i]) {
                        cardLayouts[i].autoFlipBackTime = 50
                        cardLayouts[i].flipTheView()
                        cardLayouts[i].setOnFlipListener { _, newCurrentSide ->
                            if (newCurrentSide == EasyFlipView.FlipState.BACK_SIDE) {
                                cardViews[i].setImageResource(CardUiUtils.cardToImage(cards[i]))
                            }
//                            enableDealButtonUi()
                        }
                    }
                }
            }
        }
    }

}