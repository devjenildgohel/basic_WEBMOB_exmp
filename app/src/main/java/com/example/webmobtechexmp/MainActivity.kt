package com.example.webmobtechexmp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webmobtechexmp.adapter.UserAdapter
import com.example.webmobtechexmp.api.RetrofitClient
import com.example.webmobtechexmp.databinding.ActivityMainBinding
import com.example.webmobtechexmp.model.RandomUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var isLoading = false
    private var page = 1
    private var totalPages = Int.MAX_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(this)
        layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        binding.rvUsers.adapter = adapter
        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && page < totalPages) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        binding.pbLoading.visibility = View.VISIBLE
                        isLoading = true
                        loadMoreItems()
                    }
                }
            }
        })
        loadMoreItems()
    }

    /*   private fun fetchUsers(page: Int) {
           RetrofitClient().api.getUsers(page, 20)
               .enqueue(object : Callback<RandomUserResponse> {
                   override fun onResponse(
                       call: Call<RandomUserResponse>,
                       response: Response<RandomUserResponse>
                   ) {
                       if (response.isSuccessful) {
                           val data = response.body()
                           if (data != null) {
                               totalPages = data.totalPages
                               adapter.submitList(data.results)
                               binding.pbLoading.visibility = View.GONE
                               isLoading = false
                           }
                       }
                   }

                   override fun onFailure(call: Call<RandomUserResponse>, t: Throwable) {
                       Log.e("TAG", "onFailure: ${t.message}")
                       binding.pbLoading.visibility = View.GONE
                       isLoading = false
                   }
               })
       }*/

    private fun loadMoreItems() {
        if (page < totalPages) {
            page++
            val resultsPerPage = 20

            RetrofitClient().api.getUsers(page, resultsPerPage)
                .enqueue(object : Callback<RandomUserResponse> {
                    override fun onResponse(
                        call: Call<RandomUserResponse>,
                        response: Response<RandomUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val users = response.body()?.results
                            if (users != null) {
                                adapter.submitList(users)
                            }
                        } else {
                            // handle error
                        }
                        binding.pbLoading.visibility = View.GONE
                        isLoading = false
                    }

                    override fun onFailure(call: Call<RandomUserResponse>, t: Throwable) {
                        // handle error
                        binding.pbLoading.visibility = View.GONE
                        isLoading = false
                    }
                })
        }
    }


}

