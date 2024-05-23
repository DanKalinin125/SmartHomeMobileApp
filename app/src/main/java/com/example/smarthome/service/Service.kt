package com.example.smarthome.service

import com.example.smarthome.data.DataSource
import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.Room
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV

class Service {
    private var dataSource = DataSource

    fun getAll(): MutableList<Room>{
        return dataSource.getAll();
    }

    fun getCurrent(roomId: Int): Room{
        return dataSource.getById(roomId);
    }

    fun createRoom(roomTypeName: String, roomName: String){
        dataSource.save(
            Room(
                roomName,
                RoomType.toImageResourceId(
                    RoomType.toRoomType(roomTypeName)
                )
            )
        )
    }

    fun createDevice(deviceTypeName: String, deviceName: String, room: Room){
        val deviceType = DeviceType.toDeviceType(deviceTypeName)

        val device: Device =
            when(deviceType){
                DeviceType.LIGHT -> Light(deviceName).toDevice()
                DeviceType.TV -> TV(deviceName).toDevice()
                else -> Light(deviceName).toDevice()
            }

        val roomDevices = room.devices
        roomDevices.add(device)
        room.devices = roomDevices

        dataSource.save(room)
    }
}