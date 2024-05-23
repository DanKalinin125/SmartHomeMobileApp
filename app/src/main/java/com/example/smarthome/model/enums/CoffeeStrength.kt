package com.example.smarthome.model.enums

enum class CoffeeStrength {
    HIGH,
    MEDIUM,
    LOW;

    companion object {
        fun getAll(): List<String> = listOf(
            "Высокая",
            "Средняя",
            "Низкая"
        )

        fun toCoffeeStrength(typeName: String): CoffeeStrength {
            return when (typeName) {
                "Высокая" -> HIGH
                "Средняя" -> MEDIUM
                "Низкая" -> LOW
                else -> throw NoSuchElementException("Нет такого типа комнаты")
            }
        }

        fun toString(type: CoffeeStrength): String {
            return when (type) {
                HIGH -> "Высокая"
                MEDIUM -> "Средняя"
                LOW -> "Низкая"
            }
        }
    }
}