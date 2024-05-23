package com.example.smarthome.model.devices

import android.util.Log
import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.DeviceType
import com.google.gson.Gson


class Light(var deviceName: String) : Device(deviceName, DeviceType.LIGHT) {

    var status: Boolean = false

    constructor(_name: String, _status: Boolean) : this(_name) {
        status = _status
    }

    fun toDevice(): Device {
        Log.d("serLight", Gson().toJson(this))
        return Device(deviceName, DeviceType.LIGHT, Gson().toJson(this))
    }

    companion object {
        fun toLight(device: Device): Light {
            return Gson().fromJson(device.jsonSerializedObject, Light::class.java)
        }
    }
}