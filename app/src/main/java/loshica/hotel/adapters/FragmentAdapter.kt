package loshica.hotel.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import loshica.hotel.views.CurrentRoomFragment
import loshica.hotel.views.RoomFragment

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RoomFragment()
            1 -> CurrentRoomFragment()
            else -> RoomFragment()
        }
    }
}