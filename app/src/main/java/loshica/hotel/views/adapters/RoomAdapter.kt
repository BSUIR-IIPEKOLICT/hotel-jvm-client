package loshica.hotel.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import loshica.hotel.databinding.RoomCardBinding
import loshica.hotel.interfaces.OnPickCard
import loshica.hotel.models.Room

class RoomAdapter(private val pickHandler: OnPickCard) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    private var rooms: MutableList<Room> = mutableListOf()
    private var layout: RoomCardBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layout = RoomCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(layout!!, pickHandler)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room: Room = rooms[position]

        with (holder.layout) {
            roomCardType.text = "Type: ${room.type.name}"
            roomCardFloor.text = "Floor: ${room.floor}"
            roomCardStatus.text = "Status: ${if (room.isFree) "free" else "booked"}"
            root.setOnClickListener(holder)
        }
    }

    override fun getItemCount(): Int = rooms.size

    class ViewHolder internal constructor(
        val layout: RoomCardBinding,
        private val pickHandler: OnPickCard
    ) : RecyclerView.ViewHolder(layout.root), View.OnClickListener {

        override fun onClick(v: View?) {
            pickHandler.onPickCard(adapterPosition)
        }
    }

    fun update(newRooms: List<Room>?) {
        newRooms?.let {
            this.rooms.clear()
            this.rooms = newRooms.toMutableList()
            notifyDataSetChanged()
        }
    }
}