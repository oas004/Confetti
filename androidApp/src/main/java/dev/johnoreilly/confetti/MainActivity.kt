@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package dev.johnoreilly.confetti

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.johnoreilly.confetti.conferences.ConferencesRoute
import dev.johnoreilly.confetti.ui.ConfettiApp
import org.koin.android.ext.android.inject


@OptIn(ExperimentalComposeApi::class)
class MainActivity : ComponentActivity() {
    private val repository: ConfettiRepository by inject()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val writePermissionState = rememberPermissionState(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )


            val windowSizeClass = calculateWindowSizeClass(this)
            val displayFeatures = calculateDisplayFeatures(this)

            if (writePermissionState.status.isGranted) {
                var showLandingScreen by remember {
                    mutableStateOf(repository.getConference().isEmpty())
                }

                if (showLandingScreen) {
                    ConferencesRoute(navigateToConference = { conference ->
                        showLandingScreen = false
                    })
                } else {
                    ConfettiApp(windowSizeClass, displayFeatures)
                }
            } else {
                //writePermissionState.launchPermissionRequest()

                Column {
                    val textToShow = if (writePermissionState.status.shouldShowRationale) {
                        // If the user has denied the permission but the rationale can be shown,
                        // then gently explain why the app requires this permission
                        "The camera is important for this app. Please grant the permission."
                    } else {
                        // If it's the first time the user lands on this feature, or the user
                        // doesn't want to be asked again for this permission, explain that the
                        // permission is required
                        "Camera permission required for this feature to be available. " +
                            "Please grant the permission"
                    }
                    Text(textToShow)
                    Button(onClick = { writePermissionState.launchPermissionRequest() }) {
                        Text("Request permission")
                    }
                }
            }
        }
    }
}