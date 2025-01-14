package com.example.lab10dialog

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.editText)
        val showDialogButton: Button = findViewById(R.id.showDialogButton)
        val inputHintTextView: TextView = findViewById(R.id.inputHintTextView)

        showDialogButton.setOnClickListener {
            val userInput = editText.text.toString()

            if (validateInput(userInput)) {
                inputHintTextView.visibility = TextView.GONE
                showSuccessDialog()
            } else {
                inputHintTextView.visibility = TextView.VISIBLE
                showErrorDialog()
            }
        }
    }

    private fun validateInput(input: String): Boolean {
        return input.length >= 3
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_success_title))
            .setMessage(getString(R.string.dialog_success_message))
            .setPositiveButton(getString(R.string.dialog_button_ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_error_title))
            .setMessage(getString(R.string.dialog_error_message))
            .setPositiveButton(getString(R.string.dialog_button_ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
