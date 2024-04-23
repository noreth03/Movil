package com.example.kotlin.movil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.movil.model.RetrofitClient
import com.example.kotlin.movil.model.ninja.ninjaRespones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _ninjaResponse = MutableLiveData<ninjaRespones>()
    val ninjaResponse: LiveData<ninjaRespones>
        get() = _ninjaResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun fetchDNSLookup(domain: String, apiKey: String) {
        val service = RetrofitClient.retrofitService
        val call = service.getDNSLookup(domain, apiKey)

        call.enqueue(object : Callback<ninjaRespones> {
            override fun onResponse(call: Call<ninjaRespones>, response: Response<ninjaRespones>) {
                if (response.isSuccessful) {
                    _ninjaResponse.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()} ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<ninjaRespones>, t: Throwable) {
                _error.value = "Error: ${t.message}"
            }
        })
    }
}
