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
    val currentRoom: MutableLiveData<Room> = MutableLiveData(Default.ROOM)

    private var isEdit: Boolean = false

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

    fun getCurrentRoom(): Room = currentRoom.value ?: Default.ROOM

    fun getIsEdit(): Boolean = isEdit

    fun setCurrentRoom(roomId: Int?) {
        currentRoom.value = rooms.value?.find { it.id == roomId } ?: Default.ROOM
    }

    fun setIsEdit(value: Boolean) {
        isEdit = value
    }

    private fun createRoom(dto: RoomDto) {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.create(dto).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful && it.body() != null) {
                        rooms.value = rooms.value?.plusElement(it.body()!!)
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }

    private fun changeRoom(dto: RoomDto) {
        val currentRoomId: Int = currentRoom.value?.id ?: return

        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.change(currentRoomId, dto).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        val changedRoom: Room? = it.body()

                        currentRoom.value = changedRoom
                        rooms.value = rooms.value
                            ?.map { room -> if (room.id == changedRoom?.id) changedRoom else room }
                            ?: emptyList()
                    } else {
                        onError(it.message())
                    }

                    isEdit = false
                }
            }
        })
    }

    fun handleSubmit(dto: RoomDto) = if (!isEdit) createRoom(dto) else changeRoom(dto)

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