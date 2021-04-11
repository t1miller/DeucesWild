package com.poker.deuceswild.simulator

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poker.deuceswild.R
import com.poker.deuceswild.cardgame.Card
import com.poker.deuceswild.cardgame.Deck
import com.poker.deuceswild.cardgame.dialog.CardSelectionDialog
import com.poker.deuceswild.cardgame.ui.CardUiUtils
import com.poker.deuceswild.handstatui.HandStatAdapter
import com.poker.deuceswild.ui.main.MainViewModel
import java.lang.Math.abs

/**
 * A simple [Fragment] subclass.
 * Use the [SimulatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SimulatorFragment : Fragment(), CardSelectionDialog.CardTouched {

    init {
        Deck.newDeck()
    }

    private var cardViews = mutableListOf<ImageView>()

    private lateinit var startButton: Button

    private lateinit var seekBar: SeekBar

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: HandStatAdapter

    private lateinit var seekBarText: TextView

    private lateinit var errorInputHandText: TextView

    private lateinit var expectedValueText: TextView

    private lateinit var cardsLayout: ConstraintLayout

    private lateinit var progress: ProgressDialog

    private var cardsHoldOverlay: MutableList<TextView> = mutableListOf()

    private var tapOrigin = 0

    private var dialog: Dialog? = null

    private var cards = Deck.draw5()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simulator, container, false)

        startButton = view.findViewById(R.id.button)
        seekBar = view.findViewById(R.id.seekBar)
        seekBarText = view.findViewById(R.id.seekbarValue)
        progress = ProgressDialog(activity)
        errorInputHandText = view.findViewById(R.id.errorText)
        cardsLayout = view.findViewById(R.id.cardsLayout)
        expectedValueText = view.findViewById(R.id.expectedHandValue)

        cardViews.add(view.findViewById(R.id.card1))
        cardViews.add(view.findViewById(R.id.card2))
        cardViews.add(view.findViewById(R.id.card3))
        cardViews.add(view.findViewById(R.id.card4))
        cardViews.add(view.findViewById(R.id.card5))
        cardViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                tapOrigin = index
                dialog = CardSelectionDialog.showCardSelectionDialog(requireContext(),this, getVisibleCards())
            }
        }

        cardsHoldOverlay.add(view.findViewById(R.id.card1Hold))
        cardsHoldOverlay.add(view.findViewById(R.id.card2Hold))
        cardsHoldOverlay.add(view.findViewById(R.id.card3Hold))
        cardsHoldOverlay.add(view.findViewById(R.id.card4Hold))
        cardsHoldOverlay.add(view.findViewById(R.id.card5Hold))

        startButton.setOnClickListener {
            clearHoldUi()
            showSimulationLoadingDialog(seekBar.progress)
            viewModel.getBestHand(getVisibleCards(), seekBar.progress)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarText.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })



        CardUiUtils.showCards(cardViews, cards)
        val recyclerView = view.findViewById(R.id.handList) as RecyclerView
        adapter = HandStatAdapter(requireContext(), mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.aiDecision.observe(viewLifecycleOwner, Observer { aiDecision ->
            adapter.updateData(aiDecision.sortedRankedHands, aiDecision.hand)
            dismissSimulationLoadingDialog()
            showAiCardsToHold(aiDecision.hand, aiDecision.sortedRankedHands.first().first)
            updateYourExpectedValue(aiDecision.sortedRankedHands.first().second)
        })
    }


    private fun showSimulationLoadingDialog(numTrials: Int) {
        progress.setTitle("Monte Carlo Simulation")

        var trialText = "Possible Decisions: 32\n"
        trialText += String.format("Trials Per Decision: %,d\n",numTrials)
        trialText += String.format("Total Games: %,d\n\n",32*numTrials)

        if(numTrials > TRIAL_THRESHOLD) {
            trialText += randomSuprisedExpression()
            trialText += ", that's a lot of trials. Let's Go!"
        }
        progress.setMessage(trialText)
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()
    }

    private fun dismissSimulationLoadingDialog() {
        progress.dismiss()
    }

    private fun showAiCardsToHold(fullHand: List<Card>, bestHand: List<Card>) {
        for ((idx, card) in fullHand.withIndex()) {
            if (bestHand.contains(card)) {
                cardsHoldOverlay[idx].visibility = View.VISIBLE
            }
        }
    }

    private fun updateYourExpectedValue(expectedValue: Double) {
        if(expectedValue >= 0){
            expectedValueText.text = getString(R.string.optimal_dec_pos, expectedValue)
        } else {
            expectedValueText.text = getString(R.string.optimal_dec_neg, abs(expectedValue))
        }
    }

    private fun clearHoldUi() {
        for (card in cardsHoldOverlay) {
            card.visibility = View.GONE
        }
    }

    companion object {

        val NAME = SimulatorFragment::class.java.simpleName

        const val TRIAL_THRESHOLD = 10000

        @JvmStatic
        fun newInstance() = SimulatorFragment()

        fun randomSuprisedExpression() : String {
            val expressions = mutableListOf<String>()
            expressions.add("Holly cannoli")
            expressions.add("Gee willikers")
            expressions.add("Gee whiz")
            expressions.add("Holy toledo")
            expressions.add("Gosh almighty")
            expressions.add("Holy pretzel")
            expressions.add("I'll be jitterBugged")
            expressions.add("Zookers")
            expressions.add("Holy pretzel")
            expressions.add("Hot diggity")
            expressions.add("Jeepers creepers")
            expressions.add("Well call me a biscuit")
            return expressions.random()
        }
    }

    private fun getVisibleCards() : List<Card>{
        return cards.filter{it.rank != -1}
    }

    override fun onCardSelected(card: Card) {
        if(tapOrigin < cardViews.size) {
            cards[tapOrigin] = card
            CardUiUtils.showCards(listOf(cardViews[tapOrigin]), listOf(card))
            dialog?.dismiss()
        } else {
            Toast.makeText(context,"oops", Toast.LENGTH_LONG).show()
        }
    }
}