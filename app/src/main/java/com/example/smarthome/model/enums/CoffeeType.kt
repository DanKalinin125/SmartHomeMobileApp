package com.example.smarthome.model.enums

import com.example.smarthome.R

enum class CoffeeType {
    ESPRESSO,
    CAPPUCCINO,
    LATTE;

    companion object {
        fun getAll() : List<String> = listOf(
            "Эспрессо",
            "Капучино",
            "Латте"
        )

        fun toCoffeeType(typeName: String): CoffeeType {
            return when(typeName){
                "Эспрессо" -> ESPRESSO
                "Капучино" -> CAPPUCCINO
                "Латте" -> LATTE
                else -> throw NoSuchElementException("Нет такого типа датчика")
            }
        }

        fun toString(type: CoffeeType): String{
            return when(type){
                ESPRESSO -> "Эспрессо"
                CAPPUCCINO -> "Капучино"
                LATTE -> "Латте"
            }
        }
    }
}