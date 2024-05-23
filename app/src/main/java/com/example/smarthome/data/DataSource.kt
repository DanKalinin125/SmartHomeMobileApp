package com.example.smarthome.data

import com.example.smarthome.R
import com.example.smarthome.model.Room
import com.example.smarthome.model.devices.AirConditioner
import com.example.smarthome.model.devices.CoffeeMachine
import com.example.smarthome.model.devices.Light
import com.example.smarthome.model.devices.TV
import com.example.smarthome.model.devices.Thermometer
import com.example.smarthome.model.enums.RoomType
import kotlin.math.max

class DataSource {
    companion object{
        private var rooms: MutableList<Room> = mutableListOf(
            Room(0, "Кухня", RoomType.KITCHEN, mutableListOf(
                AirConditioner("Кондиционер").toDevice(),
                CoffeeMachine("Кофемашина").toDevice(),
                Light("Свет").toDevice(),
                Thermometer("Термометр").toDevice(),
                TV("Телевизор").toDevice()
            )),
            Room(1, "Зал", RoomType.LIVING_ROOM, mutableListOf(
                Light("Свет").toDevice(),
                TV("Телевизор").toDevice()
            )),
            Room(2, "Спальня", RoomType.BEDROOM),
            Room(5, "Кабинет", RoomType.OFFICE)
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

        fun delete(room: Room){
            var roomInListId: Int? = null

            for (i in 0..<rooms.size) {
                if (rooms[i].id == room.id) {
                    roomInListId = i
                }
            }

            if (roomInListId != null) {
                rooms.removeAt(roomInListId)
            }
        }

        private fun calcNewId(): Int {
            var maxId = -1
            for (room in rooms) {
                maxId = max(room.id, maxId)
            }
            return maxId + 1
        }
    }
}