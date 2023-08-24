package com.alex.riacalc.screens.month

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentMonthBinding
import com.alex.riacalc.screens.DialogSetMonthAndYear
import com.alex.riacalc.screens.SharedViewModel
import com.alex.riacalc.utils.AppPreferences
import java.util.Calendar

class MonthFragment : Fragment(), OnClickListener {

    private lateinit var viewModel: MonthFragmentVM
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthFragmentVM::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMonthBinding.inflate(inflater, container, false)

        setupDialogSetMonthAndYearListener()

        binding.includedMonthHeader.btnMonthDay.setImageDrawable(resources.getDrawable(R.drawable.ic_calendar_day))
        binding.includedMonthHeader.frameDate.setOnClickListener(this)
        binding.includedMonthHeader.btnMonthDay.setOnClickListener(this)
        binding.includedMonthHeader.btnExport.setOnClickListener(this)
        binding.includedMonthHeader.btnSetting.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {

        if (v != null){
            when(v.id){
                R.id.btn_setting -> {findNavController().navigate(R.id.action_monthFragment_to_settingFragment)}
                R.id.btn_month_day -> {findNavController().navigate(R.id.action_monthFragment_to_dayFragment)}
                R.id.btn_export -> { Toast.makeText(requireContext(), "in progress..", Toast.LENGTH_SHORT).show() }
                R.id.frame_date -> { showDialogSetMonthAndYear() }
            }
        }
    }

    fun changeDate(month: Int, year: Int){
        Toast.makeText(requireContext(), "New date: year=$year, month=$month", Toast.LENGTH_SHORT).show()
    }

    fun showDialogSetMonthAndYear(){
        val c = Calendar.getInstance()
        DialogSetMonthAndYear.show(parentFragmentManager, c.get(Calendar.MONTH), c.get(Calendar.YEAR))
    }
    fun setupDialogSetMonthAndYearListener(){
        DialogSetMonthAndYear.setupListener(parentFragmentManager, viewLifecycleOwner) { month, year ->
            changeDate(month, year)
        }
    }
}