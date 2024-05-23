package com.example.smarthome.model

import androidx.annotation.DrawableRes
import com.example.smarthome.model.enums.DeviceType

open class Device ( var name: String, var deviceType: DeviceType) {
    var jsonSerializedObject: String = ""

    constructor(_name: String, _deviceType: DeviceType, _jsonValues: String) : this(_name, _deviceType){
        jsonSerializedObject = _jsonValues
    }
}