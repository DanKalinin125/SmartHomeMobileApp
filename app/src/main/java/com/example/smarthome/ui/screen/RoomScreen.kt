package com.example.smarthome.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.DeviceType
import com.example.smarthome.model.Room
import com.example.smarthome.service.Service
import com.example.smarthome.ui.theme.SmartHomeTheme
import com.example.smarthome.utils.swapList

private val plusButtonFontSize = 50.sp
private var service = Service()

@Composable
fun RoomScreen(navController: NavController, roomId: String) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        val room = remember { mutableStateOf(service.getCurrent(roomId.toInt())) }

        RoomScreenContent(
            navController = navController,
            room = room,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Composable
fun RoomScreenContent(navController: NavController, room: MutableState<Room>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        RoomScreenTitle(room.value.name)
        DeviceGrid(navController = navController, room = room)
    }
}

@Composable
fun RoomScreenTitle(roomName: String, modifier: Modifier = Modifier) {
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
fun DeviceGrid(navController: NavController, room: MutableState<Room>, modifier: Modifier = Modifier) {
    val deviceList = remember { mutableStateListOf<Device>() }
    deviceList.swapList(room.value.devices)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        item { AddDeviceButton(deviceList, room) }
        items(deviceList.toList()) { device ->
            DeviceCard(navController, room, device)
        }
    }
}

@Composable
fun AddDeviceButton(deviceList: SnapshotStateList<Device>, room: MutableState<Room>, modifier: Modifier = Modifier) {
    val openDialog = remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(
                dimensionResource(R.dimen.padding_small) * 2 +
                        dimensionResource(R.dimen.image_size) +
                        21.dp +
                        dimensionResource(R.dimen.padding_medium) * 2
            )
            .fillMaxWidth()
            .clickable(onClick = { openDialog.value = true })
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontSize = plusButtonFontSize,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

    if (openDialog.value) {
        AddDeviceDialog(openDialog, deviceList, room)
    }
}

@Composable
fun AddDeviceDialog(
    openDialog: MutableState<Boolean>,
    deviceList: SnapshotStateList<Device>,
    room: MutableState<Room>
) {
    val name = remember { mutableStateOf("") }
    val typeName = remember { mutableStateOf(DeviceType.getAll()[0]) }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = "Новое устройство")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            ) {
                Column {
                    val showRoomNameErrorFlag = remember { mutableStateOf(false) }

                    TextField(
                        label = { Text("Название") },
                        value = name.value,
                        onValueChange = {
                            name.value = it
                            showRoomNameErrorFlag.value = true
                        },
                        isError = (name.value == "" && showRoomNameErrorFlag.value)
                    )

                    AnimatedVisibility(visible = (name.value == "" && showRoomNameErrorFlag.value)) {
                        Text(
                            text = "Данное поле должно быть заполнено",
                            modifier = Modifier.fillMaxWidth(),
                            style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }

                DeviceTypeDropdownMenu(typeName)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                    service.createDevice(typeName.value, name.value, room.value)
                    room.value = service.getCurrent(room.value.id)
                    deviceList.swapList(room.value.devices)
                },
                enabled = (name.value != "" && typeName.value != "")
            ) {
                Text("Создать", style = MaterialTheme.typography.titleSmall)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }
            ) {
                Text("Отменить", style = MaterialTheme.typography.titleSmall)
            }
        }
    )
}

@Composable
fun DeviceTypeDropdownMenu(
    selectedDeviceTypeName: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val deviceTypes = DeviceType.getAll()

    val expanded = remember { mutableStateOf(false) }
    val icon = if (expanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            label = { Text("Категория") },
            value = selectedDeviceTypeName.value,
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
            deviceTypes.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        selectedDeviceTypeName.value = type
                        expanded.value = false
                    },
                    text = { Text(type) }
                )
            }
        }
    }
}


@Composable
fun DeviceCard(
    navController: NavController,
    room: MutableState<Room>,
    device: Device,
    modifier: Modifier = Modifier
) {
    val deviceScreen = stringResource(R.string.device_screen)
    val deviceScreenRoom = stringResource(R.string.device_screen_room)
    val deviceScreenDevice = stringResource(R.string.device_screen_device)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    Log.d("room", room.value.toString())
                    Log.d("devices", room.value.devices.toString())
                    Log.d("deviceId", room.value.devices.indexOf(device).toString())

                    navController.navigate(
                        deviceScreen
                            .replace(
                                oldValue = deviceScreenRoom,
                                newValue = room.value.id.toString()
                            )
                            .replace(
                                oldValue = deviceScreenDevice,
                                newValue = room.value.devices.indexOf(device).toString()
                            )
                    )
                }
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    dimensionResource(R.dimen.padding_small),
                    dimensionResource(R.dimen.padding_small)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = device.imageResourceId),
                contentDescription = null,
                modifier = modifier
                    .size(
                        width = dimensionResource(R.dimen.image_size),
                        height = dimensionResource(R.dimen.image_size)
                    )
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddDeviceDialogPreview() {
    SmartHomeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val deviceList = remember { mutableStateListOf<Device>() }
            val openDialog = remember { mutableStateOf(false) }
            val room = remember { mutableStateOf(service.getCurrent(0)) }

            RoomScreen(navController = navController, room.value.id.toString())
            AddDeviceDialog(openDialog, deviceList, room)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    SmartHomeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            RoomScreen(navController = navController, roomId = "0")
        }
    }
}