package com.alex.riacalc.screens

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ItemDayBinding
import com.alex.riacalc.model.Day
import java.util.Calendar

class AdapterForMonth(): RecyclerView.Adapter<AdapterForMonth.MyHolder>() {

    private var listDay: List<Day> = emptyList()
    private lateinit var dayNames: Array<String>
    private lateinit var context: Context

    fun setList(list: List<Day>){
        listDay = list
    }

    override fun getItemCount() = listDay.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayBinding.inflate(inflater, parent, false)
        context = parent.context
        dayNames = context.resources.getStringArray(R.array.day_name)

        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val day = listDay[position]

        with(holder.binding){
            itemDayDay.text = context.getString(
                R.string.template_item_day_day,
                day.date.get(Calendar.DAY_OF_MONTH),
                dayNames[day.date.get(Calendar.DAY_OF_WEEK)]
            )
            itemDayCount.text = day.inspectionCount.toString()
            itemDaySum.text = context.getString(R.string.text_formatted_currency,   )


        }
    }

    class MyHolder(val binding: ItemDayBinding): RecyclerView.ViewHolder(binding.root)
}