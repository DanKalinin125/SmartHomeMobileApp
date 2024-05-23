package com.example.smarthome.model

import androidx.annotation.DrawableRes

data class Room (
    var name: String,
    @DrawableRes var imageResourceId: Int
) {
    var id: Int = -1
    var devices: MutableList<Device> = mutableListOf()

    constructor(_id: Int, _name: String, _imageResourceId: Int) : this(_name, _imageResourceId){
        id = _id
    }

    constructor(_name: String, _imageResourceId: Int, _devices: MutableList<Device>) : this(_name, _imageResourceId){
        devices = _devices
    }

    constructor(_id: Int, _name: String, _imageResourceId: Int, _devices: MutableList<Device>) : this(_name, _imageResourceId){
        id = _id
        devices = _devices
    }
}

