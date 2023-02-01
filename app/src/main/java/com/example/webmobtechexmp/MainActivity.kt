package com.example.webmobtechexmp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.webmobtechexmp.adapter.UserAdapter
import com.example.webmobtechexmp.api.RetrofitClient
import com.example.webmobtechexmp.databinding.ActivityMainBinding
import com.example.webmobtechexmp.model.RandomUserResponse
import com.example.webmobtechexmp.room.AppDatabase
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var isLoading: Boolean = false
    private var page = 1
    private var totalPages = Int.MAX_VALUE

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "user-db"
        ).build()

        adapter = UserAdapter(this)
        layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        binding.rvUsers.adapter = adapter
        setupRecyclerView()

        loadMore()
    }

    private fun loadFromLocalDatabase(page: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val resultsPerPage = 20
            val startIndex = (page - 1) * resultsPerPage
            val endIndex = startIndex + resultsPerPage
            val users = database.userDao().getAll().value
            withContext(Dispatchers.Main) {
                database.userDao().getAll().observe(this@MainActivity, Observer {
                    if (it == null || it.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Data Empty", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("TAG", "${it.size}")
                        if(endIndex <= it.size){
                            binding.pbLoading.visibility = View.VISIBLE
                            adapter.submitList(it.subList(startIndex, endIndex))
                        }
                    }
                })
            }
        }
    }

    private var currentPage = 1

    private fun loadMore() {
        if (!isLoading) {
            var loading = true
            currentPage++
            if (isOnline(this)) {
                loadMoreItems(currentPage)
            } else {
                loadFromLocalDatabase(currentPage)
            }
            loading = false
        }
    }

    private fun setupRecyclerView() {
        binding.rvUsers.apply {
            adapter = this@MainActivity.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager!!.itemCount
                    val firstVisibleItemPosition =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMore()
                        binding.pbLoading.visibility = View.GONE
                        isLoading = false
                    }
                }
            })
        }
    }


    private fun loadMoreItems(page: Int) {
        if (page < totalPages) {
            val resultsPerPage = 20
            RetrofitClient().api.getUsers(page, resultsPerPage)
                .enqueue(object : Callback<RandomUserResponse> {
                    override fun onResponse(
                        call: Call<RandomUserResponse>, response: Response<RandomUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val users = response.body()?.results
                            if (users != null) {
                                Log.e("TAG", "onResponse: ${users.size}")
                                adapter.submitList(users)
                                GlobalScope.launch(Dispatchers.IO) {
                                    database.userDao().insertAll(users)
                                }
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

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                Toast.makeText(this, "Turn On Internet Please !", Toast.LENGTH_SHORT).show()
                TODO("VERSION.SDK_INT < M")
            }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}


