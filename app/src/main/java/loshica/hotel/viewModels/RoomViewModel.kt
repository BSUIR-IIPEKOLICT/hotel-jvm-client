package loshica.hotel.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import loshica.hotel.core.BaseViewModel
import loshica.hotel.dtos.RoomDto
import loshica.hotel.models.Room
import loshica.hotel.shared.Default

class RoomViewModel(override val app: Application): BaseViewModel(app) {

    val rooms: MutableLiveData<List<Room>> = MutableLiveData(emptyList())
        get() = field

    val currentRoom: MutableLiveData<Room> = MutableLiveData(Default.room)
        get() = field

    init {
        loadRooms()
    }

    private fun loadRooms() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.getAll().let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        rooms.value = it.body()
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }

    fun getRooms(): List<Room> = rooms.value ?: emptyList()

    fun getCurrentRoom(): Room = currentRoom.value ?: Default.room

    fun setCurrentRoom(roomId: Int?) {
        currentRoom.value = rooms.value?.find { it.id == roomId } ?: Default.room
    }

    fun createRoom(dto: RoomDto) {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.create(dto).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        rooms.value?.plusElement(it.body())
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }

    fun changeRoom(id: Int, dto: RoomDto) {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.change(id, dto).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        val changedRoom: Room? = it.body()

                        rooms.value = rooms.value
                            ?.map { room -> if (room.id == changedRoom?.id) changedRoom else room }
                            ?: emptyList()
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }

    fun deleteRoom(roomId: Int) {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.delete(roomId).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        rooms.value = rooms.value?.filter { room -> room.id != it.body()?.id }
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }
}