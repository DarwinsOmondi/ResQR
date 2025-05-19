package com.example.resqr.utils

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

val supabaseClient = createSupabaseClient(
    supabaseUrl = "",
    supabaseKey = ""
) {
    install(Postgrest)
    install(Auth)
    install(Realtime)
    install(Storage)
}