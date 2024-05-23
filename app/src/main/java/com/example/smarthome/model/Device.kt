package com.example.smarthome.model

import androidx.annotation.DrawableRes

open class Device ( var name: String, var deviceType: DeviceType) {

    @DrawableRes var imageResourceId: Int = DeviceType.toImageResourceId(deviceType)
    var jsonSerializedObject: String = ""

    constructor(_name: String, _deviceType: DeviceType, _jsonValues: String) : this(_name, _deviceType){
        jsonSerializedObject = _jsonValues
    }
}