package com.example.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smarthome.model.Device
import com.example.smarthome.model.DeviceType
import com.example.smarthome.model.devices.Light
import com.example.smarthome.ui.screen.HomeScreen
import com.example.smarthome.ui.screen.RoomScreen
import com.example.smarthome.ui.theme.SmartHomeTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val homeScreen = stringResource(R.string.home_screen)

            val roomScreen = stringResource(R.string.room_screen)
            var roomScreenArg = stringResource(R.string.room_screen_arg)
            roomScreenArg = roomScreenArg.slice(1..<(roomScreenArg.length - 1))

            val deviceScreen = stringResource(R.string.device_screen)
            var deviceScreenArg = stringResource(R.string.room_screen_arg)
            deviceScreenArg = deviceScreenArg.slice(1..<(deviceScreenArg.length - 1))

            SmartHomeTheme {
                NavHost(
                    navController = navController,
                    startDestination = homeScreen
                ) {
                    composable(route = homeScreen) {
                        HomeScreen(navController)
                    }
                    composable(roomScreen) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString(roomScreenArg)
                        if (id != null)
                            RoomScreen(navController, id)
                    }
                }
            }
        }
    }
}

