package com.example.smarthome.model

import com.example.smarthome.R

enum class RoomType {
    KITCHEN,
    LIVING_ROOM,
    BEDROOM,
    OFFICE;

    companion object {
        fun getAll() : List<String> = listOf(
            "Кухня",
            "Зал",
            "Спальня",
            "Кабинет"
        )

        fun toRoomType(typeName: String): RoomType{
            when(typeName){
                "Кухня" -> return KITCHEN
                "Зал" -> return LIVING_ROOM
                "Спальня" -> return BEDROOM
                "Кабинет" -> return OFFICE
                else -> throw NoSuchElementException("Нет такого типа комнаты")
            }
        }

        fun toImageResourceId(roomType: RoomType) : Int{
            return when(roomType){
                KITCHEN -> R.drawable.kitchen
                LIVING_ROOM -> R.drawable.livingroom
                BEDROOM -> R.drawable.bedroom
                OFFICE -> R.drawable.office
            }
        }
    }
}