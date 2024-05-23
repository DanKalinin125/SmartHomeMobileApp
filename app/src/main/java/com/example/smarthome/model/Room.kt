package com.example.smarthome.model

import androidx.annotation.DrawableRes
import com.example.smarthome.model.enums.RoomType

data class Room (
    var name: String,
    var roomType: RoomType
) {
    var id: Int = -1
    var devices: MutableList<Device> = mutableListOf()

    constructor(_id: Int, _name: String, _roomType: RoomType) : this(_name, _roomType){
        id = _id
    }

    constructor(_name: String, _roomType: RoomType, _devices: MutableList<Device>) : this(_name, _roomType){
        devices = _devices
    }

    constructor(_id: Int, _name: String, _roomType: RoomType, _devices: MutableList<Device>) : this(_name, _roomType){
        id = _id
        devices = _devices
    }
}

