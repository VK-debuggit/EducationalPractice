//package com.example.myfirstproject.data.service
//
//import com.example.educationalpractice.Data.Model.ForgotPasswordRequest
//import com.example.educationalpractice.Data.Model.SignInRequest
//import com.example.educationalpractice.Data.Model.SignInResponse
//import com.example.educationalpractice.Data.Model.SignUpRequest
//import com.example.educationalpractice.Data.Model.SignUpResponse
//import com.example.educationalpractice.Data.Model.UpdatePasswordRequest
//import com.example.educationalpractice.Data.Model.VerifyOtpRequest
//import com.example.educationalpractice.Data.Model.VerifyRequest
//import com.example.educationalpractice.Data.Model.VerifyResponse
//import com.google.gson.annotations.SerializedName
//import retrofit2.Response
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.Headers
//import retrofit2.http.POST
//import retrofit2.http.PUT
//import retrofit2.http.Query
//
//const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E"
//
//interface UserManagementService {
//    @Headers("apikey: $API_KEY")
//    @POST("auth/v1/signup")
//    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>
//
//    @Headers("apikey: $API_KEY")
//    @POST("auth/v1/token?grant_type=password")
//    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>
//
//    @Headers("apikey: $API_KEY")
//    @POST("auth/v1/verify")
//    suspend fun verify(@Body verifyRequest: VerifyRequest): Response<VerifyResponse>
////    @Headers("apikey: $API_KEY")
////    @POST("auth/v1/signup")
////    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>
////
////    @Headers("apikey: $API_KEY")
////    @POST("auth/v1/token?grant_type=password")
////    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>
////
////    @Headers("apikey: $API_KEY")
////    @POST("auth/v1/verify")
////    suspend fun verify(@Body verifyRequest: VerifyRequest): Response<VerifyResponse>
////
//    @Headers("apikey: $API_KEY")
//    @POST("auth/v1/recover")
//    suspend fun resetPasswordForEmail(@Body request: ForgotPasswordRequest): Response<Unit>
////
////    @Headers("apikey: $API_KEY")
////    @POST("auth/v1/magiclink")
////    suspend fun sendMagicLink(@Body request: ForgotPasswordRequest): Response<Unit>
////
////    @Headers("apikey: $API_KEY", "Authorization: Bearer $API_KEY")
////    @POST("rest/v1/rpc/verify_otp_code")
////    suspend fun verifyOtp(@Body request: VerifyOtpRequest): retrofit2.Response<Boolean>
////
//    @Headers("apikey: $API_KEY")
//    @PUT("auth/v1/user")
//    suspend fun updateUser(@Body request: UpdatePasswordRequest): Response<Unit>
//
//    @Headers("apikey: $API_KEY")
//    @POST("auth/v1/verify")
//    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>
//
//    @Headers("apikey: $API_KEY")
//    @POST("auth/v1/otp")
//    suspend fun resendOtp(@Body request: ResendOtpRequest): Response<Unit>
//}
//
//// Добавьте модели:
//data class VerifyOtpRequest(
//    @SerializedName("email") val email: String,
//    @SerializedName("token") val token: String,
//    @SerializedName("type") val type: String = "signup" // или "magiclink" или "recovery"
//)
//
//data class VerifyOtpResponse(
//    @SerializedName("access_token") val accessToken: String?,
//    @SerializedName("refresh_token") val refreshToken: String?,
//    @SerializedName("user") val user: User?
//)
//
//data class ResendOtpRequest(
//    @SerializedName("email") val email: String,
//    @SerializedName("type") val type: String = "signup"
//)
//
//data class User(
//    @SerializedName("id") val id: String,
//    @SerializedName("email") val email: String,
//    @SerializedName("confirmed_at") val confirmedAt: String?
//)

package com.example.educationalpractice.data.service

import com.example.educationalpractice.Data.Model.*
import com.example.educationalpractice.Data.Model.SignInRequest
import com.example.educationalpractice.Data.Model.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E"

interface UserManagementService {
//    @Headers("apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E")
//    @POST("auth/v1/signup")
//    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>
//
//    @Headers("apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E")
//    @POST("auth/v1/token?grant_type=password")
//    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>
//
//    @Headers("apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E")
//    @POST("auth/v1/verify")
//    suspend fun verify(@Body verifyRequest: VerifyRequest): Response<VerifyResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/verify")
    suspend fun verify(@Body verifyRequest: VerifyRequest): Response<VerifyResponse>

    @Headers("apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E")
    @POST("auth/v1/recover")
    suspend fun resetPasswordForEmail(@Body request: ForgotPasswordRequest): Response<Unit>

    @Headers("apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E")
    @PUT("auth/v1/user")
    suspend fun updateUser(@Body request: UpdatePasswordRequest): Response<Unit>

    // 1. Отправить email для сброса
    @Headers("apikey: ...")
    @POST("auth/v1/recover")
    suspend fun sendPasswordResetEmail(@Body request: ForgotPasswordRequest): Response<Unit>

    // 2. Сбросить пароль с токеном из email
    @Headers("apikey: ...")
    @POST("auth/v1/reset")
    suspend fun resetPasswordWithToken(@Body request: ResetPasswordRequest): Response<Unit>

    // 3. Обновить пароль если авторизован (с токеном)
    @Headers("apikey: ...")
    @PUT("auth/v1/user")
    suspend fun updateUserPassword(
        @Header("Authorization") token: String,
        @Body request: UpdatePasswordRequest
    ): Response<Unit>
}

//// Модели данных
//data class SignUpRequest(val email: String, val password: String)
//data class SignInRequest(val email: String, val password: String)
//data class VerifyOtpRequest(val email: String, val token: String, val type: String = "signup")
//data class ResendOtpRequest(val email: String, val type: String = "signup")
//
data class ResetPasswordRequest(
    val token: String,
    val password: String
)
//
//data class SignUpResponse(
//    val id: String?,
//    val user: SignUpUser?,
//    val access_token: String?,
//    val refresh_token: String?
//)
//
//data class SignUpUser(
//    val id: String,
//    val email: String,
//    val created_at: String,
//    val confirmed_at: String?,
//    val last_sign_in_at: String?
//)
//
//data class SignInResponse(
//    val access_token: String?,
//    val refresh_token: String?,
//    val user: SignInUser?
//)
//
//data class SignInUser(
//    val id: String,
//    val email: String,
//    val confirmed_at: String?
//)
//
//data class VerifyOtpResponse(
//    val access_token: String?,
//    val refresh_token: String?,
//    val user: OtpUser?
//)
//
//data class OtpUser(
//    val id: String,
//    val email: String,
//    val confirmed_at: String?
//)