package com.example.smarthome.service

import com.example.smarthome.data.DataSource
import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.AirConditioner
import com.example.smarthome.model.devices.CoffeeMachine
import com.example.smarthome.model.enums.RoomType
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.devices.Thermometer

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
                RoomType.toRoomType(roomTypeName)
            )
        )
    }

    fun deleteRoom(room: Room){
        dataSource.delete(room)
    }

    fun createDevice(deviceTypeName: String, deviceName: String, room: Room){
        val deviceType = DeviceType.toDeviceType(deviceTypeName)

        val device: Device =
            when(deviceType){
                DeviceType.LIGHT -> Light(deviceName).toDevice()
                DeviceType.TV -> TV(deviceName).toDevice()
                DeviceType.COFFEE_MACHINE -> CoffeeMachine(deviceName).toDevice()
                DeviceType.AIR_CONDITIONER -> AirConditioner(deviceName).toDevice()
                DeviceType.THERMOMETER -> Thermometer(deviceName).toDevice()
            }

        val roomDevices = room.devices
        roomDevices.add(device)
        room.devices = roomDevices

        dataSource.save(room)
    }

    fun updateDevice(room: Room, deviceId: Int, updatedDevice: Device){
        val roomDevices = room.devices
        roomDevices[deviceId] = updatedDevice
        room.devices = roomDevices

        dataSource.save(room)
    }

    fun deleteDevice(room: Room, deviceId: Int){
        val roomDevices = room.devices
        roomDevices.removeAt(deviceId)
        room.devices = roomDevices

        dataSource.save(room)
    }
}