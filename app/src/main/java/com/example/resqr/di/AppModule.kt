package com.example.resqr.di

import com.example.resqr.BuildConfig
import com.example.resqr.data.repository.AlertRepositoryImpl
import com.example.resqr.data.repository.AuthRepositoryImpl
import com.example.resqr.data.repository.MedicalRepositoryImpl
import com.example.resqr.data.repository.UserRepositoryImpl
import com.example.resqr.domain.usecase.alert.AlertUseCase
import com.example.resqr.domain.usecase.alert.GetAlertUseCase
import com.example.resqr.domain.usecase.alert.SendAlertUseCase
import com.example.resqr.domain.usecase.alert.UpdateAlertUseCase
import com.example.resqr.domain.usecase.auth.GetCurrentUserUseCase
import com.example.resqr.domain.usecase.auth.SignInUseCase
import com.example.resqr.domain.usecase.auth.SignUpUseCase
import com.example.resqr.domain.usecase.medical.DeleteMedicalDataUseCase
import com.example.resqr.domain.usecase.medical.GetCurrentUserMedicalDataUseCase
import com.example.resqr.domain.usecase.medical.GetMedicalDataUseCase
import com.example.resqr.domain.usecase.medical.InsertMedicalDataUseCase
import com.example.resqr.domain.usecase.medical.MedicalUseCases
import com.example.resqr.domain.usecase.medical.UpdateMedicalDataUseCase
import com.example.resqr.domain.usecase.user.DeleteUserUseCase
import com.example.resqr.domain.usecase.user.GetUserUseCase
import com.example.resqr.domain.usecase.user.InsertUserUseCase
import com.example.resqr.domain.usecase.user.UpdateUserUseCase
import com.example.resqr.domain.usecase.user.UserUseCase
import com.example.resqr.presentation.viewmodel.AlertViewModel
import com.example.resqr.presentation.viewmodel.AuthViewModel
import com.example.resqr.presentation.viewmodel.MedicalViewModel
import com.example.resqr.presentation.viewmodel.UserViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object AppModule {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
        install(Realtime)
        install(Storage)

    }

    //REPOSITORY IMPLEMENTATIONS
    val authRepositoryImpl by lazy {
        AuthRepositoryImpl(supabaseClient)
    }

    val medicalRepositoryImpl by lazy {
        MedicalRepositoryImpl(supabaseClient)
    }

    val userRepositoryImpl by lazy {
        UserRepositoryImpl(supabaseClient)
    }

    val alertRepositoryImpl by lazy {
        AlertRepositoryImpl(supabaseClient)
    }

    //USE CASES
    //Auth
    val signUpUseCase by lazy {
        SignUpUseCase(authRepositoryImpl)
    }
    val signInUseCase by lazy {
        SignInUseCase(authRepositoryImpl)
    }
    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepositoryImpl)
    }

    //MEDICAL USE CASES
    val insertMedicalDataUseCase by lazy {
        InsertMedicalDataUseCase(medicalRepositoryImpl)
    }
    val getMedicalDataUseCase by lazy {
        GetMedicalDataUseCase(medicalRepositoryImpl)
    }
    val getCurrentUserMedicalDataUseCase by lazy {
        GetCurrentUserMedicalDataUseCase(medicalRepositoryImpl)
    }
    val updateMedicalDataUseCase by lazy {
        UpdateMedicalDataUseCase(medicalRepositoryImpl)
    }
    val deleteMedicalDataUseCase by lazy {
        DeleteMedicalDataUseCase(medicalRepositoryImpl)
    }
    val medicalUseCases by lazy {
        MedicalUseCases(
            insertMedicalData = insertMedicalDataUseCase,
            getMedicalData = getMedicalDataUseCase,
            getCurrentUserMedicalData = getCurrentUserMedicalDataUseCase,
            updateMedicalData = updateMedicalDataUseCase,
            deleteMedicalData = deleteMedicalDataUseCase
        )
    }

    //USER USE CASES
    val insertUserUseCase by lazy {
        InsertUserUseCase(userRepositoryImpl)
    }
    val getUserUseCase by lazy {
        GetUserUseCase(userRepositoryImpl)
    }
    val updateUserUseCase by lazy {
        UpdateUserUseCase(userRepositoryImpl)
    }
    val deleteUserUseCase by lazy {
        DeleteUserUseCase(userRepositoryImpl)
    }

    val userUseCase by lazy {
        UserUseCase(
            insertUser = insertUserUseCase,
            getUser = getUserUseCase,
            updateUser = updateUserUseCase,
            deleteUser = deleteUserUseCase
        )
    }


    //ALERT USE CASES
    val sendAlertUseCase by lazy {
        SendAlertUseCase(alertRepositoryImpl)
    }
    val getAlertUseCase by lazy {
        GetAlertUseCase(alertRepositoryImpl)
    }
    val updateAlertUseCase by lazy {
        UpdateAlertUseCase(alertRepositoryImpl)
    }
    val alertUseCase by lazy {
        AlertUseCase(
            sendAlertUseCase = sendAlertUseCase,
            getAlertUseCase = getAlertUseCase,
            updateAlertUseCase = updateAlertUseCase
        )
    }


    //VIEW MODELS
    val authViewModel by lazy {
        AuthViewModel(signUpUseCase, signInUseCase, getCurrentUserUseCase)
    }
    val medicalViewModel by lazy {
        MedicalViewModel(medicalUseCases)
    }

    val userViewModel by lazy {
        UserViewModel(userUseCase)
    }
    val alertViewModel by lazy {
        AlertViewModel(alertUseCase)
    }
}