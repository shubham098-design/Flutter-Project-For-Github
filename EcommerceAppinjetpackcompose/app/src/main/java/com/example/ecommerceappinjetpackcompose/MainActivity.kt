package com.example.ecommerceappinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ecommerceappinjetpackcompose.presentation.SignUpScreen
import com.example.ecommerceappinjetpackcompose.presentation.navigation.App
import com.example.ecommerceappinjetpackcompose.ui.theme.EcommerceAppInJetpackComposeTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceAppInJetpackComposeTheme {
                App(firebaseAuth)
            }
        }
    }
}
