package com.example.smarthome.model.devices

import com.example.smarthome.model.Device
import com.example.smarthome.model.DeviceType
import com.google.gson.Gson


class Light( var deviceName: String)
    : Device( deviceName, DeviceType.LIGHT) {

    var status: Boolean = false

    constructor(_name: String, _status: Boolean) : this(_name){
        status = _status
    }

    fun toDevice(): Device {
        return Device(deviceName, DeviceType.LIGHT, Gson().toJson(this))
    }

    companion object {
        fun toLight(device: Device): Light{
            return Gson().fromJson(device.jsonSerializedObject, Light::class.java)
        }
    }
}