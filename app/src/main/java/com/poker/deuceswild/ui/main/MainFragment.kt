package com.poker.deuceswild.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import com.poker.deuceswild.R
import com.poker.deuceswild.cardgame.Evaluate
import com.poker.deuceswild.payout.PayTableManager
import com.poker.deuceswild.payout.PayTableType

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var payTableLayout: TableLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.main_fragment, container, false)
        payTableLayout = view.findViewById(R.id.payoutTable)

        populatePayTable(PayTableType._101_28)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun populatePayTable(type: PayTableType) {
        for ((i,row) in payTableLayout.children.withIndex()) {
            val payRowValues = PayTableManager.getPayTableRow(type,i)
            for((j,tv) in (row as TableRow).children.withIndex()){
                if(j != 0) {
                    (tv as TextView).text = payRowValues[j-1].toString()
                }
            }
        }
    }

}