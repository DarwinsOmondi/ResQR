package com.example.resqr.data.repositoryImpl

import android.util.Log
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.usermodel.UserResponse
import com.example.resqr.domain.repository.user.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(private val supabaseClient: SupabaseClient) : UserRepository {
    override fun insertUser(user: User): Flow<UserResponse> = flow {
        emit(UserResponse.Loading)
        try {
            supabaseClient.postgrest["users"].insert(user)
            emit(UserResponse.GetUser(user))
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "insertUser: ${e.message}")
            emit(UserResponse.UserError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun getUser(): Flow<UserResponse> = flow {
        emit(UserResponse.Loading)
        try {
            val user = supabaseClient.postgrest["users"].select().decodeSingle<User>()
            emit(UserResponse.GetUser(user))
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "insertUser: ${e.message}")
            emit(UserResponse.UserError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun updateUser(user: User): Flow<UserResponse> = flow {
        emit(UserResponse.Loading)
        try {
            supabaseClient.postgrest["users"].update(user) {
                filter {
                    eq("id", user.id)
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "insertUser: ${e.message}")
            emit(UserResponse.UserError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun deleteUser(id: Int): Flow<UserResponse> = flow {
        emit(UserResponse.Loading)
        try {
            supabaseClient.postgrest["users"].delete {
                filter {
                    eq("id", id)
                }
            }
            emit(UserResponse.GetUser(null))
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "insertUser: ${e.message}")
            emit(UserResponse.UserError(userFriendlyErrorMessage(e.message)))
        }
    }
}