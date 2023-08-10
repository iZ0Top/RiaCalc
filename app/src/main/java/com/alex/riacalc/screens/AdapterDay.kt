package com.alex.riacalc.screens

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ItemDayBinding
import com.alex.riacalc.databinding.ItemEventBinding
import com.alex.riacalc.model.Event

class AdapterDay: RecyclerView.Adapter<AdapterDay.mHolder>() {

    lateinit var context: Context
    var eventList: List<Event> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = eventList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
        context = parent.context
        return mHolder(binding)
    }

    override fun onBindViewHolder(holder: mHolder, position: Int) {
        val event = eventList[position]


        when(event.type){
            0 -> {
                holder.binding.itemText.text = "+${event.cost}  ${event.description}"
                if (event.cost < 80)
                    holder.binding.root.setCardBackgroundColor(context.getColor(R.color.green_100))
                else {
                    holder.binding.root.setCardBackgroundColor(context.getColor(R.color.blue_100))
                }
            }
            1, 2 -> {
                holder.binding.itemText.text = "-${event.cost}  ${event.description}"
                holder.binding.root.setCardBackgroundColor(context.getColor(R.color.red_100))
            }
        }
    }

    class mHolder(val binding: ItemEventBinding): RecyclerView.ViewHolder(binding.root){

    }
}