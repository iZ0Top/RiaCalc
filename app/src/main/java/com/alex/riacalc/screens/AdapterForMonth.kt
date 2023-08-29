package com.alex.riacalc.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.riacalc.databinding.ItemDayBinding
import com.alex.riacalc.model.Day

class AdapterForMonth(): RecyclerView.Adapter<AdapterForMonth.MyHolder>() {

    private val listDay: List<Day> = emptyList()

    override fun getItemCount() = listDay.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayBinding.inflate(inflater, parent, false)

        return MyHolder(binding)
    }



    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val day = listDay[position]
    }

    class MyHolder(val binding: ItemDayBinding): RecyclerView.ViewHolder(binding.root){

    }
}