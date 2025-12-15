package com.example.educationalpractice.Data
import com.example.myfirstproject.data.service.UserManagementService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy

//const val SUPABASE_URL = "https://favuckhcdbijjjmorjbu.supabase.co/"
const val SUPABASE_URL = "https://voeknphobxqsiwmlmgbm.supabase.co/"

object RetrofitInstance {
//    private val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.207.106.77", 3128))
//    private val client = OkHttpClient.Builder().proxy(proxy).build()
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(SUPABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userManagementService = retrofit.create(UserManagementService::class.java)
}