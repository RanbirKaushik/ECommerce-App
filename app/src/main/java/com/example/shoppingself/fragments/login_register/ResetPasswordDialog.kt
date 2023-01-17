package com.example.shoppingself.fragments.login_register

import androidx.fragment.app.Fragment
import com.example.shoppingself.R
import com.example.shoppingself.databinding.ResetPasswordDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setUpBottomSheetDialog(
    onSendClick: (String) -> Unit
) {

    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)

    val binding: ResetPasswordDialogBinding = ResetPasswordDialogBinding.inflate(layoutInflater)
    //val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)


    dialog.setContentView(binding.root)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()


    binding.btnSend.setOnClickListener {
        val email  = binding.edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {
        dialog.dismiss()
    }

}