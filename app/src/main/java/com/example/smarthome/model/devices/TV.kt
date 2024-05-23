package com.example.smarthome.model.devices

import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.DeviceType
import com.google.gson.Gson

class TV( var deviceName: String)
    : Device( deviceName, DeviceType.TV) {

    fun toDevice(): Device {
        return Device(deviceName, DeviceType.TV, Gson().toJson(this))
    }

    companion object {
        fun toTV(device: Device): TV{
            return Gson().fromJson(device.jsonSerializedObject, TV::class.java)
        }
    }
}