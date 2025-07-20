package com.example.km_test.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.km_test.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var chooseUserLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val EXTRA_SELECTED_USERNAME = "extra_selected_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(FirstActivity.EXTRA_USERNAME)
        binding.txtUsername.text = username

        binding.selectedUsername.text = "No User Selected"

        chooseUserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedName = result.data?.getStringExtra(EXTRA_SELECTED_USERNAME)
                selectedName?.let {
                    binding.selectedUsername.text = it
                }
            }
        }

        binding.btnBackSecondActivity.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }

        binding.btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            chooseUserLauncher.launch(intent)
        }
    }
}