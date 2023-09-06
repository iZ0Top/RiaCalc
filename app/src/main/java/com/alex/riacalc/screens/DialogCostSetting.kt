package com.alex.riacalc.screens

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogCostSettingBinding
import com.alex.riacalc.utils.AppPreferences


class DialogCostSetting : DialogFragment() {

    private var _binding: DialogCostSettingBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        _binding = DialogCostSettingBinding.inflate(inflater)

        binding.dialogCostSettingEt.setText(AppPreferences.getReviewDefaultCost().toString())

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton("Ok", null)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
                val value = binding.dialogCostSettingEt.text.toString().toIntOrNull()
                if (value == null || value == 0) {
                    binding.dialogCostSettingEt.setText(requireContext().getText(R.string.text_0))
                    binding.dialogCostSettingTiLayout.error = requireContext().getString(R.string.text_need_specify_cost)
                    return@setOnClickListener
                }
                AppPreferences.setReviewDefaultCost(value)
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