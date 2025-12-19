//package com.example.educationalpractice.Data
//import com.example.educationalpractice.Data.Service.*
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//const val SUPABASE_URL = "https://favuckhcdbijjjmorjbu.supabase.co/"
//
//object RetrofitInstance {
//    //private val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.207.106.77", 3128))
//    //private val client = OkHttpClient.Builder().proxy(proxy).build()
//    private val client = OkHttpClient.Builder().build()
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(SUPABASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
//        .build()
//
//    val userManagementService = retrofit.create(UserManagementService::class.java)
//}

package com.example.educationalpractice.Data.Service

import com.example.educationalpractice.data.service.ProfileService
import com.example.educationalpractice.data.service.UserManagementService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy

const val SUPABASE_URL = "https://favuckhcdbijjjmorjbu.supabase.co/"

object RetrofitInstance {
    private val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.207.106.71", 3128))
    private val client = OkHttpClient.Builder().proxy(proxy).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(SUPABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    // Явно указываем типы
    val userManagementService: UserManagementService = retrofit.create(UserManagementService::class.java)
    val profileService: ProfileService = retrofit.create(ProfileService::class.java)
}