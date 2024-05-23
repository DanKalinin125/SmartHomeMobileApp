package com.example.smarthome.model.devices

import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.DeviceType
import com.google.gson.Gson

class Thermometer (var deviceName: String) : Device(deviceName, DeviceType.THERMOMETER) {

    var temperature: Double = 20.0

    constructor(_name: String, _temperature: Double) : this(_name) {
        temperature = _temperature
    }

    fun toDevice(): Device {
        return Device(deviceName, DeviceType.THERMOMETER, Gson().toJson(this))
    }

    companion object {
        fun toThermometer(device: Device): Thermometer {
            return Gson().fromJson(device.jsonSerializedObject, Thermometer::class.java)
        }
    }
}