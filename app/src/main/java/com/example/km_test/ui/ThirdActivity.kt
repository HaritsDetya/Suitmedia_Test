package com.example.km_test.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.km_test.databinding.ActivityThirdBinding
import com.example.km_test.network.ApiConfig
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    private lateinit var userAdapter: UserAdapter

    private var currentPage = 1
    private var totalPages = 1
    private var isLoading = false
    private var perPegeCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        loadUsers(currentPage)
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter { user ->
            val resultIntent = Intent().apply {
                putExtra(SecondActivity.EXTRA_SELECTED_USERNAME, user.getFullName())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ThirdActivity)
            adapter = userAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && (currentPage < totalPages)) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount -2
                            && firstVisibleItemPosition >= 0
                        ) {
                            currentPage++
                            loadUsers(currentPage)
                        }
                    }
                }
            })
        }
    }

    private fun setupListeners() {
        binding.btnBackThirdActivity.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            userAdapter.clearUsers()
            loadUsers(currentPage)
        }
    }

    private fun loadUsers(page: Int) {
        if (isLoading) return

        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyStateTextView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = ApiConfig.getApiService().getUsers(page, perPegeCount)
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    totalPages = userResponse.total_pages
                    userAdapter.addUsers(userResponse.data)
                    binding.emptyStateTextView.visibility = if (userAdapter.itemCount == 0) View.VISIBLE else View.GONE
                } else {
                    Toast.makeText(this@ThirdActivity, "Failed to load users: ${response.message()}", Toast.LENGTH_SHORT).show()
                    binding.emptyStateTextView.visibility = View.VISIBLE
                }
            } catch (e: IOException) {
                Toast.makeText(this@ThirdActivity, "Network error. Check your connection.", Toast.LENGTH_SHORT).show()
                binding.emptyStateTextView.visibility = View.VISIBLE
            } catch (e: HttpException) {
                Toast.makeText(this@ThirdActivity, "Server error: ${e.code()}", Toast.LENGTH_SHORT).show()
                binding.emptyStateTextView.visibility = View.VISIBLE
            } finally {
                isLoading = false
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}