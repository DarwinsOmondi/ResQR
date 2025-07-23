package com.example.resqr.di

import com.example.resqr.BuildConfig
import com.example.resqr.ResQRApp
import com.example.resqr.data.local.database.PasswordDatabase
import com.example.resqr.data.repositoryImpl.AlertRepository
import com.example.resqr.data.repositoryImpl.AuthRepository
import com.example.resqr.data.repositoryImpl.MedicalRepository
import com.example.resqr.data.repositoryImpl.PasswordRepositoryImpl
import com.example.resqr.data.repositoryImpl.QrRepository
import com.example.resqr.data.repositoryImpl.UserRepository
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
import com.example.resqr.domain.usecase.password.DeletePassword
import com.example.resqr.domain.usecase.password.GetPassword
import com.example.resqr.domain.usecase.password.IsPasswordCorrect
import com.example.resqr.domain.usecase.password.Password
import com.example.resqr.domain.usecase.password.SavePassword
import com.example.resqr.domain.usecase.password.UpdatePassword
import com.example.resqr.domain.usecase.qr.QrCodeUseCase
import com.example.resqr.domain.usecase.user.DeleteUserUseCase
import com.example.resqr.domain.usecase.user.GetUserUseCase
import com.example.resqr.domain.usecase.user.InsertUserUseCase
import com.example.resqr.domain.usecase.user.UpdateUserUseCase
import com.example.resqr.domain.usecase.user.UserUseCase
import com.example.resqr.presentation.viewmodel.AlertViewModel
import com.example.resqr.presentation.viewmodel.AuthViewModel
import com.example.resqr.presentation.viewmodel.MedicalViewModel
import com.example.resqr.presentation.viewmodel.PasswordViewModel
import com.example.resqr.presentation.viewmodel.QrViewModel
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

    //dao
    val passwordDao by lazy {
        PasswordDatabase.getDatabase(ResQRApp.appContext).passwordDao()
    }

    //REPOSITORY IMPLEMENTATIONS
    val authRepository by lazy {
        AuthRepository(supabaseClient)
    }

    val medicalRepository by lazy {
        MedicalRepository(supabaseClient)
    }

    val userRepository by lazy {
        UserRepository(supabaseClient)
    }

    val alertRepository by lazy {
        AlertRepository(supabaseClient)
    }

    val qrRepository by lazy {
        QrRepository()
    }

    val passwordRepositoryImpl by lazy {
        PasswordRepositoryImpl(passwordDao)
    }

    //USE CASES
    //Auth
    val signUpUseCase by lazy {
        SignUpUseCase(authRepository)
    }
    val signInUseCase by lazy {
        SignInUseCase(authRepository)
    }
    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }

    //MEDICAL USE CASES
    val insertMedicalDataUseCase by lazy {
        InsertMedicalDataUseCase(medicalRepository)
    }
    val getMedicalDataUseCase by lazy {
        GetMedicalDataUseCase(medicalRepository)
    }
    val getCurrentUserMedicalDataUseCase by lazy {
        GetCurrentUserMedicalDataUseCase(medicalRepository)
    }
    val updateMedicalDataUseCase by lazy {
        UpdateMedicalDataUseCase(medicalRepository)
    }
    val deleteMedicalDataUseCase by lazy {
        DeleteMedicalDataUseCase(medicalRepository)
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
        InsertUserUseCase(userRepository)
    }
    val getUserUseCase by lazy {
        GetUserUseCase(userRepository)
    }
    val updateUserUseCase by lazy {
        UpdateUserUseCase(userRepository)
    }
    val deleteUserUseCase by lazy {
        DeleteUserUseCase(userRepository)
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
        SendAlertUseCase(alertRepository)
    }
    val getAlertUseCase by lazy {
        GetAlertUseCase(alertRepository)
    }
    val updateAlertUseCase by lazy {
        UpdateAlertUseCase(alertRepository)
    }
    val alertUseCase by lazy {
        AlertUseCase(
            sendAlertUseCase = sendAlertUseCase,
            getAlertUseCase = getAlertUseCase,
            updateAlertUseCase = updateAlertUseCase
        )
    }

    //QRCode UseCse
    val qrCodeUseCase by lazy {
        QrCodeUseCase(
            qrCodeRepository = qrRepository
        )
    }

    //Password UseCase
    val deletePassword by lazy {
        DeletePassword(passwordRepositoryImpl)
    }
    val getPassword by lazy {
        GetPassword(passwordRepositoryImpl)
    }

    val isPassWordUseCase by lazy {
        IsPasswordCorrect(passwordRepositoryImpl)
    }

    val updatePassword by lazy {
        UpdatePassword(passwordRepositoryImpl)
    }

    val savePassword by lazy {
        SavePassword(passwordRepositoryImpl)
    }

    val password by lazy {
        Password(
            deletePassword = deletePassword,
            getPassword = getPassword,
            isPasswordCorrect = isPassWordUseCase,
            updatePassword = updatePassword,
            savePassword = savePassword
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
    val qrViewModel by lazy {
        QrViewModel(qrCodeUseCase)
    }

    val passwordViewModel by lazy {
        PasswordViewModel(password)
    }
}