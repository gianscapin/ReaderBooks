package com.gscapin.readbookcompose.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.gscapin.readbookcompose.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel : ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE)

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)

    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName, email)
                        home()
                    } else {
                        Log.d(
                            "Sign in",
                            "sign in with email and password ${task.result.toString()}"
                        )
                    }
                }
        }
    }

    private fun createUser(displayName: String?, email: String) {
        val userId = auth.currentUser?.uid
        val user = User(
            id = userId.toString(),
            userId = userId.toString(),
            displayName = displayName.toString(),
            email = email,
            avatarUrl = "",
            quote = "",
            profession = ""
        ).toMap()

        FirebaseFirestore.getInstance().collection("users").add(user)
    }

    fun signUpWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            home()
                        } else {
                            Log.d(
                                "Login",
                                "sign in with email and password ${task.result.toString()}"
                            )
                        }
                    }
            } catch (ex: Exception) {
                Log.d("Login", "sign up with email and password $ex")
            }
        }
}