package com.example.smarthome.ui.devices

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.service.Service

@Composable
fun LightComponent(room: MutableState<Room>, device: Device, modifier: Modifier = Modifier) {
    var currentDevice = remember { mutableStateOf(device)}
    var status = remember { mutableStateOf(Light.toLight(currentDevice.value).status)}

    val clarifiedDevice = Light.toLight(currentDevice.value)
    clarifiedDevice.status = status.value

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column (
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            // Изображение
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = DeviceType.toImageResourceId(currentDevice.value.deviceType)),
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
            Column (
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = "Включен",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Switch(
                        checked = status.value,
                        onCheckedChange = {
                            status.value = it
                        }
                    )
                }
            }
        }

        // Кнопка сохранить
        val service = Service()
        val context = LocalContext.current
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                enabled = deviceIsChanged(Light.toLight(currentDevice.value), clarifiedDevice),
                onClick = {
                    val deviceId = room.value.devices.indexOf(currentDevice.value)
                    service.updateDevice(
                        room = room.value,
                        deviceId = deviceId,
                        updatedDevice = clarifiedDevice.toDevice()
                    )
                    room.value = service.getCurrent(room.value.id)
                    currentDevice.value = room.value.devices[deviceId]
                    Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_LONG).show()
                }
            ) {
                Text("Сохранить")
            }
        }
    }
}

private fun deviceIsChanged(oldDevice: Light, newDevice: Light): Boolean{
    return (oldDevice.name != newDevice.name) ||
            (oldDevice.deviceType != newDevice.deviceType) ||
            (oldDevice.status != newDevice.status)
}

@Preview(showBackground = true)
@Composable
fun LightComponentPreview() {
    var room = remember {
        mutableStateOf(
            Room(
                0, "Кухня", RoomType.KITCHEN, mutableListOf(
                    Light("Свет").toDevice(),
                    Light("Свет 2").toDevice(),
                    TV("Телевизор").toDevice()
                )
            )
        )
    }
    var device = room.value.devices[1]

    LightComponent(room = room, device = device)
}