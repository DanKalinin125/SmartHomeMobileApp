package com.example.smarthome.data

import com.example.smarthome.R
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import kotlin.math.max

class DataSource {
    private var rooms: MutableList<Room> = mutableListOf(
        Room(0, "Кухня", R.drawable.kitchen, mutableListOf(
            Light("Свет").toDevice(),
            Light("Свет 2").toDevice(),
            TV("Телевизор").toDevice()
        )),
        Room(1, "Зал", R.drawable.livingroom, mutableListOf(
            Light("Свет").toDevice(),
            TV("Телевизор").toDevice()
        )),
        Room(2, "Спальня 1", R.drawable.bedroom),
        Room(3, "Спальня 2", R.drawable.bedroom),
        Room(4, "Спальня 3", R.drawable.bedroom),
        Room(5, "Кабинет 1", R.drawable.office),
        Room(6, "Кабинет 2", R.drawable.office)
    )

    fun getAll(): MutableList<Room> {
        return rooms
    }

    fun getById(id: Int): Room {
        for (room in rooms) {
            if (room.id == id) {
                return room
            }
        }
        throw NoSuchElementException("Комната с id = " + id + " не найдена")
    }

    fun save(room: Room): Room {
        var roomInListId: Int? = null

        for (i in 0..<rooms.size) {
            if (rooms[i].id == room.id) {
                roomInListId = i
            }
        }

        if (roomInListId == null) {
            room.id = calcNewId()
            rooms.add(room)
        } else {
            rooms[roomInListId] = room
        }

        return room
    }

    private fun calcNewId(): Int {
        var maxId = -1
        for (room in rooms) {
            maxId = max(room.id, maxId)
        }
        return maxId + 1
    }

}