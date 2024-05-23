package com.example.smarthome.model.devices

import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.DeviceType
import com.example.smarthome.model.enums.WindSpeed
import com.google.gson.Gson

class AirConditioner(var deviceName: String) : Device(deviceName, DeviceType.AIR_CONDITIONER) {

    var status: Boolean = false
    var temperature: Double = 20.0
    var windSpeed: WindSpeed = WindSpeed.MEDIUM

    constructor(_name: String, _temperature: Double, _windSpeed: WindSpeed) : this(_name) {
        temperature = _temperature
        windSpeed = _windSpeed
    }

    fun toDevice(): Device {
        return Device(deviceName, DeviceType.AIR_CONDITIONER, Gson().toJson(this))
    }

    companion object {
        fun toAirConditioner(device: Device): AirConditioner {
            return Gson().fromJson(device.jsonSerializedObject, AirConditioner::class.java)
        }
    }
}