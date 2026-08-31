package com.mathi.finance.core.network

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    // Replace with your actual project URL and API key from the Supabase Dashboard
    private const val SUPABASE_URL = "https://vvcwyfmtqqheqxqzgncj.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_Ue-5nPL4Vo96XwzcI2ND1g_A5HGo7tn"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }
}
