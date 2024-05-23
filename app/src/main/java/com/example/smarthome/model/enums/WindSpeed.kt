package com.example.smarthome.model.enums

enum class WindSpeed {
    HIGH,
    MEDIUM,
    LOW;

    fun getAll() : List<String> = listOf(
        "Высокая",
        "Средняя",
        "Низкая"
    )

    fun toWindSpeed(typeName: String): WindSpeed {
        return when(typeName){
            "Высокая" -> HIGH
            "Средняя" -> MEDIUM
            "Низкая" -> LOW
            else -> throw NoSuchElementException("Нет такого типа комнаты")
        }
    }
}