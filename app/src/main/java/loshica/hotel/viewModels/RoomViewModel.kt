package loshica.hotel.viewModels

import android.app.Application
import android.widget.Toast
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

    fun setCurrentRoom(index: Int?) {
        rooms.value?.let {
            currentRoom.value = if (index == null) Default.ROOM else it[index]
        }
    }

    fun setIsEdit(value: Boolean) {
        isEdit = value
    }

    private fun createRoom(dto: RoomDto) {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.create(dto).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful && it.body() != null) {
                        Toast.makeText(app.applicationContext, "Room created", Toast.LENGTH_SHORT).show()
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
            api.roomRepository.change(currentRoomId, if (dto.isFree) "free" else "booked", dto).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        val changedRoom: Room? = it.body()

                        Toast.makeText(app.applicationContext, "Room changed", Toast.LENGTH_SHORT).show()

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

    fun deleteRoom() {
        val currentRoomId: Int = currentRoom.value?.id ?: return

        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.roomRepository.delete(currentRoomId).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        Toast.makeText(app.applicationContext, "Room deleted", Toast.LENGTH_SHORT).show()
                        rooms.value = rooms.value?.filter { room -> room.id != it.body()?.id }
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }
}