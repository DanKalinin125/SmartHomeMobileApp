package com.example.smarthome.model.devices

import com.example.smarthome.model.Device
import com.example.smarthome.model.enums.CoffeeStrength
import com.example.smarthome.model.enums.DeviceType
import com.google.gson.Gson

class CoffeeMachine (var deviceName: String) : Device(deviceName, DeviceType.COFFEE_MACHINE) {

    var strength: CoffeeStrength = CoffeeStrength.MEDIUM
    var volume: Int = 250

    constructor(_name: String, _strength: CoffeeStrength, _volume: Int) : this(_name) {
        strength = _strength
        volume = _volume
    }

    fun toDevice(): Device {
        return Device(deviceName, DeviceType.COFFEE_MACHINE, Gson().toJson(this))
    }

    companion object {
        fun toCoffeeMachine(device: Device): CoffeeMachine {
            return Gson().fromJson(device.jsonSerializedObject, CoffeeMachine::class.java)
        }
    }
}