package com.example.smarthome.ui.devices

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.CoffeeMachine
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.enums.CoffeeStrength
import com.example.smarthome.model.enums.CoffeeType
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.service.Service


@Composable
fun CoffeeMachineComponent(
    room: MutableState<Room>,
    device: Device,
    modifier: Modifier = Modifier
) {
    val currentDevice = remember { mutableStateOf(device) }
    val tmpCoffeeMachine = CoffeeMachine.toCoffeeMachine(currentDevice.value)

    val coffeeType = remember { mutableStateOf(tmpCoffeeMachine.coffeeType) }
    val strength = remember { mutableStateOf(tmpCoffeeMachine.strength) }
    val volume = remember { mutableIntStateOf(tmpCoffeeMachine.volume) }

    val clarifiedDevice = CoffeeMachine.toCoffeeMachine(currentDevice.value)
    clarifiedDevice.coffeeType = coffeeType.value
    clarifiedDevice.strength = strength.value
    clarifiedDevice.volume = volume.intValue

    val isError = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            // Изображение
            DeviceImage(currentDevice = currentDevice)

            // Параметры
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                // Напиток
                CoffeeTypeArg(coffeeType = coffeeType)

                // Крепость напитка
                StrengthArg(strength = strength)

                // Объем
                VolumeArg(volume = volume, isError = isError)
            }
        }

        // Кнопка отправить
        val service = Service()
        val context = LocalContext.current
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                enabled = !(isError.value) && deviceIsChanged(
                    CoffeeMachine.toCoffeeMachine(
                        currentDevice.value
                    ), clarifiedDevice
                ),
                onClick = {
                    val deviceId = room.value.devices.indexOf(currentDevice.value)
                    service.updateDevice(
                        room = room.value,
                        deviceId = deviceId,
                        updatedDevice = clarifiedDevice.toDevice()
                    )
                    room.value = service.getCurrent(room.value.id)
                    currentDevice.value = room.value.devices[deviceId]
                    Toast.makeText(context, "Сигнал кофемашине отправлен", Toast.LENGTH_LONG).show()
                }
            ) {
                Text("Отправить")
            }
        }
    }
}

@Composable
private fun DeviceImage(currentDevice: MutableState<Device>, modifier: Modifier = Modifier) {
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
fun CoffeeTypeArg(coffeeType: MutableState<CoffeeType>, modifier: Modifier = Modifier) {
    val values = CoffeeType.getAll()

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
            text = "Напиток",
            style = MaterialTheme.typography.bodyLarge,
        )

        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = CoffeeType.toString(coffeeType.value),
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
                            coffeeType.value = CoffeeType.toCoffeeType(type)
                            expanded.value = false
                        },
                        text = { Text(type) }
                    )
                }
            }
        }
    }
}

@Composable
fun StrengthArg(strength: MutableState<CoffeeStrength>, modifier: Modifier = Modifier) {
    val values = CoffeeStrength.getAll()

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
            text = "Напиток",
            style = MaterialTheme.typography.bodyLarge,
        )

        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = CoffeeStrength.toString(strength.value),
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
                            strength.value = CoffeeStrength.toCoffeeStrength(type)
                            expanded.value = false
                        },
                        text = { Text(type) }
                    )
                }
            }
        }
    }
}

@Composable
fun VolumeArg(
    volume: MutableIntState,
    isError: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = "Объем",
                style = MaterialTheme.typography.bodyLarge,
            )

            val volumeStrValue = remember {
                mutableStateOf(volume.intValue.toString())
            }
            TextField(
                value = volumeStrValue.value,
                onValueChange = {
                    try {
                        volumeStrValue.value = it
                        volume.intValue = volumeStrValue.value.toInt()
                        isError.value = false
                    } catch (e: NumberFormatException) {
                        isError.value = true
                    }
                },
                isError = isError.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                text = "мл",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        AnimatedVisibility(visible = isError.value) {
            Text(
                text = "Данное поле должно быть числом",
                modifier = Modifier.fillMaxWidth(),
                style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}


private fun deviceIsChanged(oldDevice: CoffeeMachine, newDevice: CoffeeMachine): Boolean {
    return (oldDevice.name != newDevice.name) ||
            (oldDevice.deviceType != newDevice.deviceType) ||
            (oldDevice.coffeeType != newDevice.coffeeType) ||
            (oldDevice.strength != newDevice.strength) ||
            (oldDevice.volume != newDevice.volume)
}

@Preview(showBackground = true)
@Composable
fun CoffeeMachineComponentPreview() {
    val room = remember {
        mutableStateOf(
            Room(
                0, "Кухня", RoomType.KITCHEN, mutableListOf(
                    Light("Свет").toDevice(),
                    Light("Свет 2").toDevice(),
                    TV("Телевизор").toDevice(),
                    CoffeeMachine("Кофемашина").toDevice()
                )
            )
        )
    }
    val device = room.value.devices[3]

    CoffeeMachineComponent(room = room, device = device)
}