package com.example.educationalpractice.Data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://favuckhcdbijjjmorjbu.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
        }
    }
}