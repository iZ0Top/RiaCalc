package com.alex.riacalc.screens.export

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentExportBinding
import com.alex.riacalc.screens.month.MonthFragmentVM

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ExportFragment : Fragment() {

    private var _binding: FragmentExportBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: MonthFragmentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MonthFragmentVM::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExportBinding.inflate(inflater, container, false)

        viewModel.

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}