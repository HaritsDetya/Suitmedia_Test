package com.example.km_test.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.km_test.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    companion object {
        const val EXTRA_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCheck.setOnClickListener {
            checkPalindrome()
        }

        binding.btnNext.setOnClickListener {
            navigateToSecondScreen()
        }
    }

    private fun checkPalindrome() {
        val inputText = binding.editTextPalindrome.text.toString()

        if (inputText.isEmpty()) {
            Toast.makeText(this, "Please enter a sentence to check!", Toast.LENGTH_SHORT).show()
            return
        }

        val isPalindrome = isPalindrome(inputText)
        val message = if (isPalindrome) "isPalindrome" else { "not palindrome" }

        showPalindromeResultDialog(message)
    }

    private fun isPalindrome(text: String): Boolean {
        val cleanText = text.lowercase().filter { it.isLetterOrDigit() }
        return cleanText == cleanText.reversed()
    }

    private fun showPalindromeResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Palindrome Check Result")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToSecondScreen() {
        val name = binding.editTextName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(EXTRA_USERNAME, name)
        startActivity(intent)
    }
}