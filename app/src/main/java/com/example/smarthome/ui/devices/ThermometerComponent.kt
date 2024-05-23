package com.example.smarthome.ui.devices

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.devices.Thermometer
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.enums.RoomType

@Composable
fun ThermometerComponent(room: MutableState<Room>, device: Device, modifier: Modifier = Modifier) {
    val clarifiedDevice = Thermometer.toThermometer(device)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        // Изображение
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = DeviceType.toImageResourceId(clarifiedDevice.deviceType)),
                contentDescription = null,
                modifier = modifier
                    .size(
                        width = dimensionResource(R.dimen.image_big_size),
                        height = dimensionResource(R.dimen.image_big_size)
                    )
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }

        // Параметры
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = "Температура: ${clarifiedDevice.temperature} °C",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThermometerComponentPreview() {
    val room = remember {
        mutableStateOf(
            Room(
                0, "Кухня", RoomType.KITCHEN, mutableListOf(
                    Light("Свет").toDevice(),
                    Light("Свет 2").toDevice(),
                    TV("Телевизор").toDevice(),
                    Thermometer("Термометр").toDevice()
                )
            )
        )
    }
    val device = room.value.devices[3]

    ThermometerComponent(room = room, device = device)
}