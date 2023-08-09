package com.alex.riacalc.screens.day

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentDayBinding
import com.alex.riacalc.screens.DialogAdd
import com.alex.riacalc.screens.MyViewModelFactory

class DayFragment : Fragment(), OnClickListener {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DayFragmentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, MyViewModelFactory()).get(DayFragmentVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)


        binding.btnMonth.setOnClickListener {
            findNavController().navigate(R.id.action_dayFragment_to_monthFragment)
        }

        binding.btnSetting.setOnClickListener {
            findNavController().navigate(R.id.action_dayFragment_to_settingFragment)
        }

        binding.btnAddInspection.setOnClickListener(this)
        binding.btnAddTrip.setOnClickListener(this)
        binding.btnAddOther.setOnClickListener(this)


        viewModel.eventListLD.observe(viewLifecycleOwner){

        }

        return binding.root
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    fun showDialog(){
        val cl:  ConstraintLayout = layoutInflater.inflate(R.layout.dialog_add, null) as ConstraintLayout
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(cl)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){
                R.id.btn_add_inspection -> {

                    val dialogAdd = DialogAdd()
                    dialogAdd.show(parentFragmentManager, DialogAdd.DIALOG_TAG)

                    Toast.makeText(requireContext(), "Button inspection", Toast.LENGTH_SHORT).show()
                }
                R.id.btn_add_trip -> {Toast.makeText(requireContext(), "Button trip", Toast.LENGTH_SHORT).show()}
                R.id.btn_add_other -> {Toast.makeText(requireContext(), "Button other", Toast.LENGTH_SHORT).show()}
            }
        }

    }

}