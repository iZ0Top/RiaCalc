package com.alex.riacalc.screens

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources.Theme
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ItemGridBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.utils.AppPreferences
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP

class AdapterForDayItem(
    private val listItem: List<Event>,
    private val context: Context
): RecyclerView.Adapter<AdapterForDayItem.MyHolder>() {

    override fun getItemCount(): Int = listItem.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGridBinding.inflate(inflater, parent, false)

        return MyHolder(binding)
    }


    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val event = listItem[position]

        when(event.type){
            TYPE_INSPECTION -> {
                holder.binding.gridId.text = context.getString(R.string.template_plus, event.cost)
                if (event.cost > AppPreferences.getReviewDefaultCost()){
                    holder.binding.gridId.backgroundTintList = changeColor(R.color.blue_100)
                }
                else{
                    holder.binding.gridId.backgroundTintList = changeColor(R.color.green_100)
                }
            }
            TYPE_TRIP, TYPE_OTHER -> {
                holder.binding.gridId.text = context.getString(R.string.template_minus, event.cost)
                holder.binding.gridId.backgroundTintList = changeColor(R.color.red_100)
            }
        }

    }

    private fun changeColor(colorId: Int): ColorStateList{
        val color = ContextCompat.getColor(context, colorId)
        return ColorStateList.valueOf(color)
    }


    class MyHolder(val binding: ItemGridBinding): RecyclerView.ViewHolder(binding.root){

    }
}