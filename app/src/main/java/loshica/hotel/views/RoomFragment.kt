package loshica.hotel.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import loshica.hotel.adapters.RoomAdapter
import loshica.hotel.databinding.FragmentMainBinding
import loshica.hotel.interfaces.IMainActivity
import loshica.hotel.interfaces.OnPickCard
import loshica.hotel.models.Room
import loshica.hotel.shared.Position
import loshica.hotel.viewModels.RoomViewModel

class RoomFragment : Fragment(), View.OnClickListener, OnPickCard {

    private var layout: FragmentMainBinding? = null
    private val roomViewModel: RoomViewModel by activityViewModels()

    private var roomsObserver: Observer<List<Room>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = FragmentMainBinding.inflate(inflater, container, false)
        val roomAdapter = RoomAdapter(this)

        with (layout!!) {
            mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            mainRecyclerView.adapter = roomAdapter

            roomsObserver = Observer { roomAdapter.update(it) }
        }

        return layout?.root
    }

    override fun onClick(v: View?) {}

    override fun onStart() {
        super.onStart()
        roomsObserver?.let { roomViewModel.rooms.observe(this, it) }
    }

    override fun onStop() {
        super.onStop()
        roomsObserver?.let { roomViewModel.rooms.removeObserver(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layout = null
    }

    override fun onPickCard(position: Int) {
        roomViewModel.setCurrentRoom(position + 1)
        (activity as? IMainActivity)?.swipe(Position.ROOM)
    }
}