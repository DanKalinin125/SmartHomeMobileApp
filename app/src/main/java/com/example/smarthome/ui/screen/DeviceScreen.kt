package com.example.smarthome.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.Room
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.service.Service
import com.example.smarthome.ui.devices.AirConditionerComponent
import com.example.smarthome.ui.devices.CoffeeMachineComponent
import com.example.smarthome.ui.devices.LightComponent
import com.example.smarthome.ui.devices.TVComponent
import com.example.smarthome.ui.devices.ThermometerComponent
import com.example.smarthome.ui.theme.SmartHomeTheme

private val plusButtonFontSize = 50.sp
private var service = Service()

@Composable
fun DeviceScreen(navController: NavController, roomId: String, deviceId: String) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        val room = remember { mutableStateOf(service.getCurrent(roomId.toInt())) }
        val device = room.value.devices[deviceId.toInt()]

        DeviceScreenContent(
            navController = navController,
            room = room,
            device = device,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Composable
fun DeviceScreenContent(navController: NavController, room: MutableState<Room>, device: Device, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        DeviceScreenTitle(device.name)
        DeviceComponent(room, device)
    }
}

@Composable
fun DeviceScreenTitle(roomName: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = roomName,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
fun DeviceComponent(room: MutableState<Room>, device: Device, modifier: Modifier = Modifier) {
    when(device.deviceType){
        DeviceType.LIGHT -> LightComponent(room, device)
        DeviceType.TV -> TVComponent(room, device)
        DeviceType.AIR_CONDITIONER -> AirConditionerComponent(room, device)
        DeviceType.COFFEE_MACHINE -> CoffeeMachineComponent(room, device)
        DeviceType.THERMOMETER -> ThermometerComponent(room, device)
    }
}


@Preview(showBackground = true)
@Composable
fun DevicePreview() {
    SmartHomeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            DeviceScreen(navController, roomId = "0", deviceId = "2")
        }
    }
}