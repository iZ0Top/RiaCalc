package com.alex.riacalc.screens.day

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ItemEventBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.utils.AppPreferences
import com.alex.riacalc.utils.TYPE_BONUS
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_PARK
import com.alex.riacalc.utils.TYPE_INSPECTION_CONST_PROGRESS
import com.alex.riacalc.utils.TYPE_INSPECTION_OTHER
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP

interface ActionListener {
    fun onEditEvent(event: Event)
    fun onDeleteEvent(event: Event)
    fun onShowDetails(event: Event)
}

class AdapterForDay(private val actionListener: ActionListener) :
    RecyclerView.Adapter<AdapterForDay.MyHolder>(), View.OnClickListener {

    private var listEvents: List<Event> = emptyList()

    fun setList(list: List<Event>) {
        listEvents = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listEvents.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.itemPopupMenu.setOnClickListener(this)

        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val event = listEvents[position]

        holder.itemView.tag = event
        holder.binding.itemPopupMenu.tag = event

        when (event.type) {
            TYPE_INSPECTION -> {
                holder.binding.itemText.text = holder.itemView.context.getString(R.string.template_item_inspection, event.cost, event.description)
                holder.binding.root.setCardBackgroundColor(holder.itemView.context.getColor(R.color.green_100))
            }
            TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER -> {
                holder.binding.itemText.text = holder.itemView.context.getString(R.string.template_item_inspection, event.cost, event.description)
                holder.binding.root.setCardBackgroundColor(holder.itemView.context.getColor(R.color.blue_100))
            }
            TYPE_TRIP, TYPE_OTHER -> {
                holder.binding.itemText.text = holder.itemView.context.getString(R.string.template_item_expense, event.cost, event.description)
                holder.binding.root.setCardBackgroundColor(holder.itemView.context.getColor(R.color.red_100))
            }
            TYPE_BONUS -> {
                holder.binding.itemText.text = holder.itemView.context.getString(R.string.template_item_inspection, event.cost, event.description)
                holder.binding.root.setCardBackgroundColor(holder.itemView.context.getColor(R.color.yellow_100))
            }
        }
    }

    class MyHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val event = v.tag as Event
        when (v.id) {
            R.id.item_popup_menu -> {
                showPopUpMenu(v)
            }
            else -> {
                actionListener.onShowDetails(event)
            }
        }
    }

    private fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val event = view.tag as Event

        popupMenu.menu.add(0, ID_MENU_EDIT, 0, R.string.text_edit)
        popupMenu.menu.add(0, ID_MENU_DELETE, 0, R.string.text_delete)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MENU_EDIT -> {
                    actionListener.onEditEvent(event)
                }
                ID_MENU_DELETE -> {
                    actionListener.onDeleteEvent(event)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_MENU_EDIT = 1
        private const val ID_MENU_DELETE = 2
    }
}