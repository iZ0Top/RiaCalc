package com.alex.riacalc.screens.month

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.riacalc.MainActivity
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ActivityMainBinding
import com.alex.riacalc.databinding.FragmentMonthBinding
import com.alex.riacalc.model.Day
import com.alex.riacalc.screens.DialogSetMonthAndYear
import com.alex.riacalc.screens.SharedVM
import com.alex.riacalc.utils.KEY_ARGUMENTS_TO_DAY
import com.alex.riacalc.utils.KEY_ARGUMENTS_TO_MONTH
import java.util.Calendar

class MonthFragment : Fragment(), OnClickListener {

    private lateinit var viewModel: MonthFragmentVM
    private lateinit var sharedViewModel: SharedVM

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var observerDate: Observer<Calendar>
    private lateinit var observerDays: Observer<List<Day>>
    private lateinit var adapter: AdapterForMonth
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var calendar: Calendar

    private var listDays = emptyList<Day>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthFragmentVM::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedVM::class.java)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        val mainActivity = activity as MainActivity
        mainBinding = mainActivity.binding

        if (arguments != null){
            viewModel.setDate(requireArguments().getSerializable(KEY_ARGUMENTS_TO_MONTH) as Calendar)
        }

        layoutManager = LinearLayoutManager(requireContext())

        initAdapter()
        initObservers()
        setupObservers()
        setupDialogSetMonthAndYearListener()

        binding.recyclerViewMonth.layoutManager = layoutManager
        binding.recyclerViewMonth.adapter = adapter

        with(mainBinding.toolbar){
            toolbarBtnSetting.visibility = View.GONE
            toolbarBtnMonth.visibility = View.GONE

            toolbarBtnBack.visibility = View.VISIBLE
            toolbarBtnExport.visibility = View.VISIBLE

            toolbarFrameDate.setOnClickListener(this@MonthFragment)
            toolbarBtnExport.setOnClickListener(this@MonthFragment)
            toolbarBtnBack.setOnClickListener(this@MonthFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.calendarLD.removeObserver(observerDate)
        _binding = null
    }

    private fun initAdapter() {
        adapter = AdapterForMonth(object : ActionListenerDay {
            override fun editDay(date: Calendar) {
                val bundle = Bundle()
                bundle.putSerializable(KEY_ARGUMENTS_TO_DAY, date)
                findNavController().navigate(R.id.action_monthFragment_to_dayFragment, bundle )
            }
        })
    }

    private fun initObservers(){
        observerDate = Observer {
            changeDate(it)
            loadEvents()
        }
        observerDays = Observer {
            updateInfo(it)
            adapter.setList(it)
            sharedViewModel.listDaysLD.value = it
        }
    }

    private fun setupObservers() {
        viewModel.calendarLD.observe(viewLifecycleOwner, observerDate)
        //viewModel.loadEventsForMonth().observe(viewLifecycleOwner, observerDays)
        viewModel.mediatorLiveData.observe(viewLifecycleOwner, observerDays)
    }

    private fun loadEvents() {
        viewModel.loadEventsForMonth()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.toolbar_btn_back -> {
                    findNavController().navigate(R.id.action_monthFragment_to_dayFragment)
                }
                R.id.toolbar_btn_export -> {
                    findNavController().navigate(R.id.action_monthFragment_to_exportFragmet)
                }
                R.id.toolbar_frame_date -> {
                    showDialogSetMonthAndYear()
                }
            }
        }
    }

    private fun changeDate(newDate: Calendar) {
        calendar = newDate
        val monthNames = resources.getStringArray(R.array.month_name_for_picker)
        with(mainBinding.toolbar) {
            toolbarTextDateFirst.text = monthNames[calendar.get(Calendar.MONTH)]
            toolbarTextDateSecond.text = calendar.get(Calendar.YEAR).toString()
        }
    }

    private fun showDialogSetMonthAndYear() {
        DialogSetMonthAndYear.show(parentFragmentManager, calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
    }

    private fun setupDialogSetMonthAndYearListener() {
        DialogSetMonthAndYear.setupListener(parentFragmentManager, viewLifecycleOwner) { month, year ->
            val newDate = Calendar.getInstance()
            newDate.set(Calendar.YEAR, year)
            newDate.set(Calendar.MONTH, month)
            viewModel.setDate(newDate)
        }
    }

    private fun updateInfo(listDays: List<Day>){
        with(mainBinding.toolbar){
            toolbarTxtReviewsCount.text = listDays.sumOf {it.inspectionCount }.toString()
            toolbarTxtTripsCount.text = listDays.sumOf { it.tripCount }.toString()
            toolbarTxtOtherCount.text = listDays.sumOf { it.otherCount }.toString()

            toolbarTxtReviewsSum.text = resources.getString(
                R.string.template_formatted_currency,
                listDays.sumOf { it.inspectionSum }
            )
            toolbarTxtTripsSum.text = resources.getString(
                R.string.template_formatted_currency,
                listDays.sumOf { it.tripSum }
            )
            toolbarTxtOtherSum.text = resources.getString(
                R.string.template_formatted_currency,
                listDays.sumOf { it.otherSum }
            )
        }
    }
}