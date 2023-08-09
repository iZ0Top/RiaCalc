package com.alex.riacalc.screens.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.btnSettingBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}