package loshica.hotel.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import loshica.hotel.databinding.FragmentMainBinding
import loshica.hotel.interfaces.IMainActivity
import loshica.hotel.interfaces.OnPickCard
import loshica.hotel.models.Room
import loshica.hotel.shared.Position
import loshica.hotel.viewModels.RoomModel

class MainFragment : Fragment(), View.OnClickListener, OnPickCard {

    private var layout: FragmentMainBinding? = null
    private val roomModel: RoomModel by activityViewModels()

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

    override fun onResume() {
        super.onResume()
        roomsObserver?.let { roomModel.rooms.observe(this, it) }
    }

    override fun onStop() {
        super.onStop()
        roomsObserver?.let { roomModel.rooms.removeObserver(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        roomModel.onDestroy()
        layout = null
    }

    override fun onPickCard(position: Int) {
        roomModel.setCurrentRoom(position)
        (activity as? IMainActivity)?.swipe(Position.ROOM)
    }
}