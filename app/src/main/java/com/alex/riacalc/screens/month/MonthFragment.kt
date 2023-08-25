package com.alex.riacalc.screens.month

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentMonthBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.screens.DialogSetMonthAndYear
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthFragment : Fragment(), OnClickListener {

    private lateinit var viewModel: MonthFragmentVM

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateObserver: Observer<Calendar>
    private lateinit var mediatorObserver: Observer<List<Event>>

    private lateinit var calendar: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthFragmentVM::class.java)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAGM", "MonthFragment - onCreateView")

        _binding = FragmentMonthBinding.inflate(inflater, container, false)

        val d = requireArguments().getSerializable(KEY_ARGUMENTS) as Calendar
        viewModel.setDate(d)

        //отримуємо дату з аргуменітв
        //встановлюємо дату у вьюмодель

        //Спрацьовує обсервер
        //оновлюємо інтерфейс
        //завантажуємо дані

        dateObserver = Observer<Calendar> {
            Log.d("TAGM", "MonthFragment - onCreateView - observerDate")
            calendar = it
            updateView()
            loadEvents()
        }

        mediatorObserver = Observer {
            Log.d("TAGM", "MonthFragment - onCreateView - observerDate, list size = ${it.size}")
            Log.d("TAGM", it.toString())
        }

        viewModel.calendarLD.observe(viewLifecycleOwner, dateObserver)
        viewModel.loadEventsForMonth().observe(viewLifecycleOwner, mediatorObserver)

        binding.includedMonthHeader.btnMonthDay.setImageDrawable(resources.getDrawable(R.drawable.ic_calendar_day))
        binding.includedMonthHeader.frameDate.setOnClickListener(this)
        binding.includedMonthHeader.btnMonthDay.setOnClickListener(this)
        binding.includedMonthHeader.btnExport.setOnClickListener(this)
        binding.includedMonthHeader.btnSetting.setOnClickListener(this)

        setupDialogSetMonthAndYearListener()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.calendarLD.removeObserver(dateObserver)
        _binding = null
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.btn_setting -> {
                    findNavController().navigate(R.id.action_monthFragment_to_settingFragment)
                }

                R.id.btn_month_day -> {
                    findNavController().navigate(R.id.action_monthFragment_to_dayFragment)
                }

                R.id.btn_export -> {
                    Toast.makeText(requireContext(), "in progress..", Toast.LENGTH_SHORT).show()
                }

                R.id.frame_date -> {
                    showDialogSetMonthAndYear()
                }
            }
        }
    }

    private fun updateView() {
        Log.d("TAGM", "MonthFragment - updateView")
        val monthNames = resources.getStringArray(R.array.month_name_for_picker)
        with(binding.includedMonthHeader) {
            textDateFirst.text = monthNames[calendar.get(Calendar.MONTH)]
            textDateSecond.text = calendar.get(Calendar.YEAR).toString()
        }
    }

    private fun loadEvents() {
        viewModel.loadEventsForMonth()
        Log.d("TAGM", "MonthFragment - loadEvents")
    }

    private fun showDialogSetMonthAndYear() {
        Log.d("TAGM", "MonthFragment - showDialogSetMonthAndYear")
        DialogSetMonthAndYear.show(
            parentFragmentManager,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
    }

    private fun setupDialogSetMonthAndYearListener() {
        Log.d("TAGM", "MonthFragment - setupDialogSetMonthAndYearListener")
        DialogSetMonthAndYear.setupListener(
            parentFragmentManager,
            viewLifecycleOwner
        ) { month, year ->

            val newDate = Calendar.getInstance()
            newDate.set(Calendar.YEAR, year)
            newDate.set(Calendar.MONTH, month)
            viewModel.setDate(newDate)
        }
    }

    companion object {
        const val KEY_ARGUMENTS = "key_argument"
    }

    fun rebuildEvents(list: List<Event>) {
        val newList = mutableListOf<EventWithDate>()
        for (event in list) {
            newList.add(
                EventWithDate(
                    id = event.id,
                    type = event.type,
                    cost = event.cost,
                    description = event.description,
                    date = dateParser(event.date)
                )
            )
        }
    }

    private fun dateParser(dateString: String): Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(dateString)
        val calendar = Calendar.getInstance()
        if (date != null) calendar.time = date
        return calendar
    }

    private fun createDay(list: List<EventWithDate>){
        val dayList = mutableListOf<Day>()

        val group = list.groupBy { it.date.get(Calendar.DAY_OF_MONTH) }

        val newList = group.map { (date, events) ->
            var inspectionCount = 0
            var inspectionSum = 0
            var tripCount = 0
            var tripSum = 0
            var otherCount = 0
            var otherSum = 0

            for (event in events){
                when(event.type){
                    TYPE_INSPECTION -> {
                        inspectionCount++
                        inspectionSum += event.cost
                    }
                    TYPE_TRIP -> {
                        tripCount++
                        tripSum += event.cost
                    }
                    TYPE_OTHER -> {
                        otherCount++
                        otherSum += event.cost
                    }
                }
            }

            Day(date, inspectionCount, inspectionSum, tripCount,tripSum,otherCount,otherSum, events)
        }


    }

    data class EventWithDate(
        val id: Int,
        val type: Int,
        var cost: Int,
        var description: String,
        var date: Calendar
    )

    data class Day(
        val day: Int,
        val inspectionCount: Int,
        val inspectionSum: Int,
        val tripCount: Int,
        val tripSum: Int,
        val otherCount: Int,
        val otherSum: Int,
        val list: List<EventWithDate>
    )
}