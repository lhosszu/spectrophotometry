package com.uni.spectro.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uni.spectro.R
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.persistence.util.DateFormatter.Companion.format
import com.uni.spectro.ui.adapters.RealmExperimentListAdapter.ExperimentViewHolder
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RealmExperimentListAdapter(context: Context?, data: OrderedRealmCollection<RealmExperiment>) : RealmRecyclerViewAdapter<RealmExperiment, ExperimentViewHolder>(data, true) {

    private lateinit var clickListener: ItemClickListener
    private lateinit var longClockListener: ItemLongClickListener
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items: OrderedRealmCollection<RealmExperiment> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperimentViewHolder {
        val itemView = inflater.inflate(R.layout.item_experiment, parent, false)
        return ExperimentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExperimentViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemTextView.text = item.name
        holder.itemDateTimeTextView.text = format(item.date)
        holder.itemExperimentTypeTextView.text = item.experimentType
    }

    override fun getItem(index: Int): RealmExperiment {
        return items[index]
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        clickListener = itemClickListener
    }

    fun setLongClickListener(listener: ItemLongClickListener) {
        longClockListener = listener
    }

    inner class ExperimentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        var itemTextView: TextView = itemView.findViewById(R.id.experiments_layout_item_text)
        var itemDateTimeTextView: TextView = itemView.findViewById(R.id.experiments_layout_item_datetime)
        var itemExperimentTypeTextView: TextView = itemView.findViewById(R.id.experiments_layout_item_experiment_type)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener.onItemClick(v, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            v?.setBackgroundColor(Color.parseColor("#D6D9DC"))
            return longClockListener.onItemLongClick(v, adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface ItemLongClickListener {
        fun onItemLongClick(view: View?, position: Int): Boolean
    }
}