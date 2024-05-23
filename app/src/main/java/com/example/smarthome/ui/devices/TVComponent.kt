package com.example.smarthome.ui.devices

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smarthome.R
import com.example.smarthome.model.Device
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.service.Service
import com.example.smarthome.ui.screen.AddDeviceButton
import com.example.smarthome.ui.screen.DeviceCard

@Composable
fun TVComponent(room: MutableState<Room>, device: Device, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Image(
            painter = painterResource(id = DeviceType.toImageResourceId(device.deviceType)),
            contentDescription = null,
            modifier = modifier
                .size(
                    width = dimensionResource(R.dimen.image_big_size),
                    height = dimensionResource(R.dimen.image_big_size)
                )
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        // Пульт
        val context = LocalContext.current
        val shape = RoundedCornerShape(20.dp)
        val rowModifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_large))
        Column (
            modifier = modifier
                .width(248.dp)
                .padding(dimensionResource(R.dimen.padding_medium))
                .background(Color.Gray, shape)
        ){
            // Включение/выключение
            Row(
                modifier = rowModifier
            ){
                Image(
                    painter = painterResource(R.drawable.power),
                    contentDescription = null,
                    modifier = modifier
                        .size(45.dp)
                        .aspectRatio(1f)
                        .clickable(onClick = {
                            Toast.makeText(context, "Сигнал включить/выключить отправлен", Toast.LENGTH_LONG).show()
                        }),
                    contentScale = ContentScale.Crop
                )
            }

            // Остальные кнопки
            val button_shape = RoundedCornerShape(10.dp)
            val buttonModifier = Modifier
                .size(60.dp)
                .background(Color.DarkGray, button_shape)

            Row (
                modifier = rowModifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column{
                    Box(
                        modifier = buttonModifier
                            .clickable(onClick = {
                                Toast.makeText(context, "Сигнал следующий канал отправлен", Toast.LENGTH_LONG).show()
                            }),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Следующий канал",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = buttonModifier
                            .clickable(onClick = {
                                Toast.makeText(context, "Сигнал предыдущий канал отправлен", Toast.LENGTH_LONG).show()
                            }),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Предыдущий канал",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                }

                Column{
                    Box(
                        modifier = buttonModifier
                            .clickable(onClick = {
                                Toast.makeText(context, "Сигнал увеличить звук отправлен", Toast.LENGTH_LONG).show()
                            }),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painterResource(R.drawable.plus),
                            contentDescription = "Увеличить звук",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = buttonModifier
                            .clickable(onClick = {
                                Toast.makeText(context, "Сигнал уменьшить звук отправлен", Toast.LENGTH_LONG).show()
                            }),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painterResource(R.drawable.minus_svgrepo_com),
                            contentDescription = "Уменьшить звук",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TVComponentPreview() {
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
    var device = room.value.devices[2]

    TVComponent(room = room, device = device)
}