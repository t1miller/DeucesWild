package com.poker.deuceswild

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.poker.deuceswild.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class StrategySuggestionRecyclerViewAdapter(
        private val values: List<String>)
    : RecyclerView.Adapter<StrategySuggestionRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_strategy_suggestion_item, parent, false)

//        val tv1 = view.findViewById<TextView>(R.id.item_number)
//        val tv2 = view.findViewById<TextView>(R.id.content)
//
//        tv1.paintFlags = tv1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        tv2.paintFlags = tv2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = (position+1).toString()
        holder.contentView.text = item
        if(itemCount != position+1){
            // last item
            holder.idView.paintFlags = holder.idView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.contentView.paintFlags = holder.contentView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.layout.background = null
        } else {
            holder.layout.setBackgroundResource(R.color.colorLightTurquoise)
            holder.idView.paintFlags = holder.idView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.contentView.paintFlags = holder.contentView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            holder.idView.paintFlags = holder.idView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            holder.contentView.paintFlags = holder.contentView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)
        val layout: LinearLayout = view.findViewById(R.id.layoutRow)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}