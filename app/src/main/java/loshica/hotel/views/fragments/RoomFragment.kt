package loshica.hotel.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import loshica.hotel.databinding.RoomFragmentBinding
import loshica.hotel.views.adapters.RoomAdapter
import loshica.hotel.interfaces.IMainActivity
import loshica.hotel.interfaces.IPickHandler
import loshica.hotel.models.Room
import loshica.hotel.shared.Position
import loshica.hotel.viewModels.CommentViewModel
import loshica.hotel.viewModels.RoomViewModel
import loshica.hotel.views.modals.RoomModal

class RoomFragment : Fragment(), View.OnClickListener, IPickHandler {

    private var layout: RoomFragmentBinding? = null
    private val roomViewModel: RoomViewModel by activityViewModels()
    private val commentViewModel: CommentViewModel by activityViewModels()

    private var roomsObserver: Observer<List<Room>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = RoomFragmentBinding.inflate(inflater, container, false)
        val roomAdapter = RoomAdapter(this)

        with (layout!!) {
            roomRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            roomRecyclerView.adapter = roomAdapter
        }

        layout!!.roomCreateButton.setOnClickListener(this)
        roomsObserver = Observer { roomAdapter.update(it) }

        return layout?.root
    }

    override fun onClick(v: View?) {
        layout?.let {
            when(v) {
                it.roomCreateButton -> {
                    roomViewModel.setIsEdit(false)
                    RoomModal().show(requireActivity().supportFragmentManager, null)
                }
                else -> {}
            }
        }
    }

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
        roomViewModel.setCurrentRoom(position)
        commentViewModel.setRoomId(roomViewModel.getCurrentRoom().id)
        commentViewModel.loadComments()
        (activity as? IMainActivity)?.swipe(Position.ROOM)
    }
}