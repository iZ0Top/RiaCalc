package com.alex.riacalc.screens

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.alex.riacalc.databinding.DialogCostSettingBinding
import com.alex.riacalc.utils.AppPreferences

class DialogCostSetting : DialogFragment() {

    private var _binding: DialogCostSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        _binding = DialogCostSettingBinding.inflate(inflater)

        binding.dialogCostSettingEt.setText(AppPreferences.getReviewDefaultCost().toString())
        binding.dialogCostCarDealershipSettingEt.setText(AppPreferences.getReviewCarDealershipCost().toString())
        binding.dialogCostCarParkSettingEt.setText(AppPreferences.getReviewCarParkDefaultCost().toString())
        binding.dialogCostConstProgressSettingEt.setText(AppPreferences.getReviewConstProgress().toString())


        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton("Ok", null)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {

                val costInspection = binding.dialogCostSettingEt.text.toString().toIntOrNull()
                val costCarDealershipInspection = binding.dialogCostCarDealershipSettingEt.text.toString().toIntOrNull()
                val costCarParkInspection = binding.dialogCostCarParkSettingEt.text.toString().toIntOrNull()
                val costConstProgress = binding.dialogCostConstProgressSettingEt.text.toString().toIntOrNull()

                var dataValid = true

                if (costInspection == null || costInspection == 0) {
                    binding.dialogCostSettingEt.hint = "0"
                    binding.dialogCostSettingTiLayout.error = " "
                    dataValid = false
                }
                if (costCarDealershipInspection == null || costCarDealershipInspection == 0){
                    binding.dialogCostCarDealershipSettingEt.hint = "0"
                    binding.dialogCostCarDealershipSettingTiLayout.error = " "
                    dataValid = false
                }
                if (costCarParkInspection == null || costCarParkInspection == 0){
                    binding.dialogCostCarParkSettingEt.hint = "0"
                    binding.dialogCostCarParkSettingTiLayout.error = " "
                    dataValid = false
                }
                if (costConstProgress == null || costConstProgress == 0){
                    binding.dialogCostConstProgressSettingEt.hint = "0"
                    binding.dialogCostConstProgressSettingTiLayout.error = " "
                    dataValid = false
                }

                if (dataValid){
                    AppPreferences.setReviewDefaultCost(costInspection!!)
                    AppPreferences.setReviewCarDealershipCost(costCarDealershipInspection!!)
                    AppPreferences.setReviewCarParkDefaultCost(costCarParkInspection!!)
                    AppPreferences.setReviewConstProgress(costConstProgress!!)
                }
                else{
                    return@setOnClickListener
                }
                dialog.dismiss()
            }
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val DIALOG_SETTING_TAG = "setting_dialog"
    }

}