package com.example.resqr.di

import com.example.resqr.BuildConfig
import com.example.resqr.ResQRApp
import com.example.resqr.data.local.database.PasswordDatabase
import com.example.resqr.data.repositoryImpl.AlertRepositoryImpl
import com.example.resqr.data.repositoryImpl.AuthRepositoryImpl
import com.example.resqr.data.repositoryImpl.MedicalRepositoryImpl
import com.example.resqr.data.repositoryImpl.PasswordRepositoryImpl
import com.example.resqr.data.repositoryImpl.QrRepositoryImpl
import com.example.resqr.data.repositoryImpl.UserRepositoryImpl
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
import com.example.resqr.domain.usecase.password.DeletePasswordUseCase
import com.example.resqr.domain.usecase.password.GetPasswordUseCase
import com.example.resqr.domain.usecase.password.IsPasswordCorrectUseCase
import com.example.resqr.domain.usecase.password.PasswordUseCase
import com.example.resqr.domain.usecase.password.SavePasswordUseCase
import com.example.resqr.domain.usecase.password.UpdatePasswordUseCase
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

    val qrRepositoryImpl by lazy {
        QrRepositoryImpl()
    }

    val passwordRepositoryImpl by lazy {
        PasswordRepositoryImpl(passwordDao)
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

    //QRCode UseCse
    val qrCodeUseCase by lazy {
        QrCodeUseCase(
            qrCodeRepository = qrRepositoryImpl
        )
    }

    //Password UseCase
    val deletePasswordUseCase by lazy {
        DeletePasswordUseCase(passwordRepositoryImpl)
    }
    val getPasswordUseCase by lazy {
        GetPasswordUseCase(passwordRepositoryImpl)
    }

    val isPassWordUseCase by lazy {
        IsPasswordCorrectUseCase(passwordRepositoryImpl)
    }

    val updatePasswordUseCase by lazy {
        UpdatePasswordUseCase(passwordRepositoryImpl)
    }

    val savePasswordUseCase by lazy {
        SavePasswordUseCase(passwordRepositoryImpl)
    }

    val passwordUseCase by lazy {
        PasswordUseCase(
            deletePassword = deletePasswordUseCase,
            getPassword = getPasswordUseCase,
            isPasswordCorrect = isPassWordUseCase,
            updatePassword = updatePasswordUseCase,
            savePassword = savePasswordUseCase
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
        PasswordViewModel(passwordUseCase)
    }
}