package com.example.smarthome.ui.screen

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.smarthome.model.Room
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.service.Service
import com.example.smarthome.ui.theme.SmartHomeTheme
import com.example.smarthome.utils.swapList

private val plusButtonFontSize = 50.sp
private var service = Service()

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        HomeScreenContent(
            navController = navController,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Composable
fun HomeScreenContent(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        HomeScreenTitle()
        RoomGrid(navController)
    }
}

@Composable
fun HomeScreenTitle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(R.string.home_screen_title),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
fun RoomGrid(navController: NavController, modifier: Modifier = Modifier) {
    val roomList = remember { mutableStateListOf<Room>() }
    roomList.swapList(service.getAll())

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        item { AddRoomButton(roomList) }
        items(roomList.toList()) { room ->
            RoomCard(navController, room, roomList)
        }
    }
}

@Composable
fun AddRoomButton(roomList: SnapshotStateList<Room>, modifier: Modifier = Modifier) {
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
        AddRoomDialog(openDialog, roomList)
    }
}

@Composable
fun AddRoomDialog(
    openDialog: MutableState<Boolean>,
    roomList: SnapshotStateList<Room>
) {
    val roomName = remember { mutableStateOf("") }
    val roomTypeName = remember { mutableStateOf(RoomType.getAll()[0]) }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = "Новая комната")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            ) {
                Column {
                    val showRoomNameErrorFlag = remember { mutableStateOf(false) }

                    TextField(
                        label = { Text("Название") },
                        value = roomName.value,
                        onValueChange = {
                            roomName.value = it
                            showRoomNameErrorFlag.value = true
                        },
                        isError = (roomName.value == "" && showRoomNameErrorFlag.value)
                    )

                    AnimatedVisibility(visible = (roomName.value == "" && showRoomNameErrorFlag.value)) {
                        Text(
                            text = "Данное поле должно быть заполнено",
                            modifier = Modifier.fillMaxWidth(),
                            style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }

                RoomTypeDropdownMenu(roomTypeName)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                    service.createRoom(roomTypeName.value, roomName.value)
                    roomList.swapList(service.getAll())
                },
                enabled = (roomName.value != "" && roomTypeName.value != "")
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
fun RoomTypeDropdownMenu(
    selectedRoomTypeName: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val roomTypes = RoomType.getAll()

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
            value = selectedRoomTypeName.value,
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
            roomTypes.forEach { roomType ->
                DropdownMenuItem(
                    onClick = {
                        selectedRoomTypeName.value = roomType
                        expanded.value = false
                    },
                    text = { Text(roomType) }
                )
            }
        }
    }
}

@Composable
fun RoomCard(
    navController: NavController,
    room: Room,
    roomList: SnapshotStateList<Room>,
    modifier: Modifier = Modifier
) {
    val roomScreen = stringResource(R.string.room_screen)
    val roomScreenArg = stringResource(R.string.room_screen_arg)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    navController.navigate(
                        roomScreen
                            .replace(
                                oldValue = roomScreenArg,
                                newValue = room.id.toString()
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
            Box(
                contentAlignment = Alignment.TopCenter
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Удалить комнату",
                        modifier.clickable(onClick = {
                            service.deleteRoom(room)
                            roomList.swapList(service.getAll())
                        })
                    )
                }
                Image(
                    painter = painterResource(id = RoomType.toImageResourceId(room.roomType)),
                    contentDescription = null,
                    modifier = modifier
                        .size(
                            width = dimensionResource(R.dimen.image_size),
                            height = dimensionResource(R.dimen.image_size)
                        )
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }


            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddRoomDialogPreview() {
    SmartHomeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val roomList = remember { mutableStateListOf<Room>() }
            val openDialog = remember { mutableStateOf(false) }
            HomeScreen(navController)
            AddRoomDialog(openDialog, roomList)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    SmartHomeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            HomeScreen(navController)
        }
    }
}