package com.example.smarthome.model

import com.example.smarthome.R

enum class DeviceType {
    LIGHT,
    TV,
    AIR_CONDITIONER,
    COFFEE_MACHINE,
    THERMOMETER;

    companion object {
        fun getAll() : List<String> = listOf(
            "Свет",
            "Телевизор",
            "Кондиционер",
            "Кофемашина",
            "Термометр"
        )

        fun toDeviceType(typeName: String): DeviceType{
            when(typeName){
                "Свет" -> return LIGHT
                "Телевизор" -> return TV
                "Кондиционер" -> return AIR_CONDITIONER
                "Кофемашина" -> return COFFEE_MACHINE
                "Термометр" -> return THERMOMETER
                else -> throw NoSuchElementException("Нет такого типа датчика")
            }
        }

        fun toImageResourceId(deviceType: DeviceType) : Int{
            return when(deviceType){
                LIGHT -> R.drawable.light
                TV -> R.drawable.tv
                AIR_CONDITIONER -> R.drawable.air_conditioner
                COFFEE_MACHINE -> R.drawable.coffee_machine
                THERMOMETER -> R.drawable.thermometer
            }
        }
    }
}