package loshica.hotel.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import loshica.hotel.databinding.FragmentCurrentRoomBinding
import loshica.hotel.models.Room
import loshica.hotel.viewModels.RoomViewModel

class CurrentRoomFragment : Fragment(), View.OnClickListener {

    private var layout: FragmentCurrentRoomBinding? = null
    private val roomViewModel: RoomViewModel by activityViewModels()

    private var currentRoomObserver: Observer<Room>? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentCurrentRoomBinding.inflate(inflater, container, false)

        with (layout!!) {
            currentRoomObserver = Observer {
                roomTypeName.text = "Type: ${it.type.name}"
                roomTypeOptions.text = "Options: ${it.type.options}"
                roomTypePrice.text = "Price: ${it.type.price}$"
                roomAddress.text = "Address: ${it.address}"
                roomDescription.text = "Description: ${it.description}"
                roomFloor.text = "Floor: ${it.floor}"
                roomPlaces.text = "Places: ${it.places}"
                roomStatus.text = "Status: ${if (it.isFree) "free" else "booked"}"
            }
        }

        return layout?.root
    }

    override fun onStart() {
        super.onStart()
        currentRoomObserver?.let { roomViewModel.currentRoom.observe(this, it) }
    }

    override fun onStop() {
        super.onStop()
        currentRoomObserver?.let { roomViewModel.currentRoom.removeObserver(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layout = null
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}