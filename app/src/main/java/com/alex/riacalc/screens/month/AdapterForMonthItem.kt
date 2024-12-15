package com.alex.riacalc.screens.month

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ItemGridBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_PARK
import com.alex.riacalc.utils.TYPE_INSPECTION_CONST_PROGRESS
import com.alex.riacalc.utils.TYPE_INSPECTION_OTHER
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP

class AdapterForMonthItem(
    private val listItem: List<Event>,
    private val context: Context
): RecyclerView.Adapter<AdapterForMonthItem.MyHolder>() {

    override fun getItemCount(): Int = listItem.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGridBinding.inflate(inflater, parent, false)

        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val event = listItem[position]

        with(holder.binding.gridLine){
            visibility = if (event.description == "") View.GONE else View.VISIBLE
        }

        when(event.type){
            TYPE_INSPECTION -> {
                holder.binding.gridText.text = context.getString(R.string.template_plus, event.cost)
                holder.binding.gridText.backgroundTintList = changeColor(R.color.green_100)
            }
            TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER -> {
                holder.binding.gridText.text = context.getString(R.string.template_plus, event.cost)
                holder.binding.gridText.backgroundTintList = changeColor(R.color.blue_100)
            }
            TYPE_TRIP, TYPE_OTHER -> {
                holder.binding.gridText.text = context.getString(R.string.template_minus, event.cost)
                holder.binding.gridText.backgroundTintList = changeColor(R.color.red_100)
            }
        }
    }

    private fun changeColor(colorId: Int): ColorStateList{
        val color = ContextCompat.getColor(context, colorId)
        return ColorStateList.valueOf(color)
    }

    class MyHolder(val binding: ItemGridBinding): RecyclerView.ViewHolder(binding.root)
}