package com.example.smarthome.ui.devices

import android.annotation.SuppressLint
import android.health.connect.datatypes.units.Temperature
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.AirConditioner
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.model.enums.WindSpeed
import com.example.smarthome.service.Service
import java.lang.Error


@Composable
fun AirConditionerComponent(room: MutableState<Room>, device: Device, modifier: Modifier = Modifier) {
    var currentDevice = remember { mutableStateOf(device)}
    var tmpAirConditioner = AirConditioner.toAirConditioner(currentDevice.value)

    var status = remember { mutableStateOf(tmpAirConditioner.status) }
    var temperature = remember { mutableDoubleStateOf(tmpAirConditioner.temperature) }
    var windSpeed = remember { mutableStateOf(tmpAirConditioner.windSpeed) }

    val clarifiedDevice = AirConditioner.toAirConditioner(currentDevice.value)
    clarifiedDevice.status = status.value
    clarifiedDevice.temperature = temperature.value
    clarifiedDevice.windSpeed = windSpeed.value

    val isError = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column (
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            // Изображение
            DeviceImage(currentDevice = currentDevice)

            // Параметры
            Column (
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                // Статус
                StatusArg(status = status)

                // Температура
                TemperatureArg(temperature = temperature, isError = isError)

                // Скорость ветра
                WindSpeedArg(windSpeed = windSpeed)
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
                enabled = !(isError.value) && deviceIsChanged(AirConditioner.toAirConditioner(currentDevice.value), clarifiedDevice),
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

@Composable
private fun DeviceImage(currentDevice: MutableState<Device>, modifier: Modifier = Modifier){
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
}

@Composable
private fun StatusArg(status: MutableState<Boolean>, modifier: Modifier = Modifier){
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

@Composable
private fun TemperatureArg(temperature: MutableDoubleState, isError: MutableState<Boolean>, modifier: Modifier = Modifier){
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))

    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = "Температура",
                style = MaterialTheme.typography.bodyLarge,
            )

            val temperatureStrValue = remember {
                mutableStateOf((Math.round(temperature.doubleValue * 10.0) / 10.0).toString())
            }
            TextField(
                value = temperatureStrValue.value,
                onValueChange = {
                    try{
                        temperatureStrValue.value = it
                        temperature.doubleValue = temperatureStrValue.value.toDouble()
                        isError.value = false
                    } catch (e : NumberFormatException){
                        isError.value = true
                    }
                },
                isError = isError.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(200.dp)
            )

            Text(
                text = "°C",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        
        AnimatedVisibility(visible = isError.value) {
            Text(
                text = "Данное поле должно быть дробным числом",
                modifier = Modifier.fillMaxWidth(),
                style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
fun WindSpeedArg(windSpeed: MutableState<WindSpeed>, modifier: Modifier = Modifier) {
    val values = WindSpeed.getAll()

    val expanded = remember { mutableStateOf(false) }
    val icon = if (expanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Text(
            text = "Скорость ветра",
            style = MaterialTheme.typography.bodyLarge,
        )

        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = WindSpeed.toString(windSpeed.value),
                onValueChange = {
                    expanded.value = false
                },
                trailingIcon = {
                    Icon(
                        icon,
                        contentDescription = "Открыть/Закрыть",
                        modifier = Modifier.clickable(onClick = {
                            expanded.value = !expanded.value
                        })
                    )
                }
            )

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                }
            ) {
                values.forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            windSpeed.value = WindSpeed.toWindSpeed(type)
                            expanded.value = false
                        },
                        text = { Text(type) }
                    )
                }
            }
        }
    }
}

private fun deviceIsChanged(oldDevice: AirConditioner, newDevice: AirConditioner): Boolean{
    return (oldDevice.name != newDevice.name) ||
            (oldDevice.deviceType != newDevice.deviceType) ||
            (oldDevice.status != newDevice.status) ||
            (oldDevice.temperature != newDevice.temperature) ||
            (oldDevice.windSpeed != newDevice.windSpeed)
}

@Preview(showBackground = true)
@Composable
fun AirConditionerComponentPreview() {
    var room = remember {
        mutableStateOf(
            Room(
                0, "Кухня", RoomType.KITCHEN, mutableListOf(
                    Light("Свет").toDevice(),
                    Light("Свет 2").toDevice(),
                    TV("Телевизор").toDevice(),
                    AirConditioner("Кондей").toDevice()
                )
            )
        )
    }
    var device = room.value.devices[3]

    AirConditionerComponent(room = room, device = device)
}