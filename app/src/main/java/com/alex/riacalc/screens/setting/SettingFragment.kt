package com.alex.riacalc.screens.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.fragment.findNavController
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentSettingBinding
import com.alex.riacalc.utils.AppPreferences
import kotlin.math.cos

class SettingFragment : Fragment(), OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "SettingFragment - onCreate" )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "SettingFragment - onCreateView" )
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        readCurrentPreferences()

        binding.btnSettingBack.setOnClickListener(this)
        binding.switchShowCost.setOnCheckedChangeListener(this)
        binding.switchAddTrips.setOnCheckedChangeListener(this)
        binding.switchAddOther.setOnCheckedChangeListener(this)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View) {
        Log.d("TAG", "SettingFragment - onClick" )
        val cost = binding.etSettingInputCost.text.toString().toIntOrNull()
        if (cost == null){
            binding.etSettingInputCost.setText("0")
            binding.etSettingInputCost.setTextColor(requireContext().getColor(R.color.red_400))
            return
        }
        Log.d("TAG", "SettingFragment - onClick (cost = ${cost})" )
        AppPreferences.setReviewDefaultCost(cost)
        findNavController().popBackStack()
    }
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Log.d("TAG", "SettingFragment - onCheckedChanged" )

        when (buttonView.id) {
            R.id.switch_show_cost -> { AppPreferences.setShowCost(isChecked) }
            R.id.switch_add_trips -> { AppPreferences.setExportTrips(isChecked) }
            R.id.switch_add_other -> { AppPreferences.setExportOther(isChecked) }
        }
    }

    private fun readCurrentPreferences() {
        Log.d("TAG", "SettingFragment - readCurrentPreferences" )
        binding.etSettingInputCost.setText(AppPreferences.getReviewDefaultCost().toString())
        binding.switchShowCost.isChecked = AppPreferences.getShowCost()
        binding.switchAddTrips.isChecked = AppPreferences.getExportTrips()
        binding.switchAddOther.isChecked = AppPreferences.getExportOther()
    }
}
