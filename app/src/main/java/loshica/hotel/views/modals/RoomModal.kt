package loshica.hotel.views.modals

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import loshica.hotel.R
import loshica.hotel.databinding.RoomModalBinding
import loshica.hotel.dtos.RoomDto
import loshica.hotel.models.Room
import loshica.hotel.models.Type
import loshica.hotel.shared.Default
import loshica.hotel.viewModels.RoomViewModel
import loshica.hotel.viewModels.TypeViewModel
import loshica.vendor.view.LOSDialogBuilder

class RoomModal : DialogFragment(), View.OnClickListener {

    private var layout: RoomModalBinding? = null
    private val roomViewModel: RoomViewModel by activityViewModels()
    private val typeViewModel: TypeViewModel by activityViewModels()

    private var typeId: Int = Default.ROOM.type.id
    private var typesObserver: Observer<List<Type>>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = LOSDialogBuilder(requireActivity())

        layout = RoomModalBinding.inflate(requireActivity().layoutInflater)
        typeId = if (roomViewModel.getIsEdit()) roomViewModel.getCurrentRoom().type.id else typeId

        with(layout!!) {
            if (roomViewModel.getIsEdit()) {
                val room: Room = roomViewModel.getCurrentRoom()

                roomAddressInput.setText(room.address)
                roomDescriptionInput.setText(room.description)
                roomFloorInput.setText(room.floor.toString())
                roomPlacesInput.setText(room.places.toString())
            }
        }

        typesObserver = Observer { onChangeTypes(it) }

        val titleRef: Int = if (roomViewModel.getIsEdit())
            R.string.change_room_modal_header
        else
            R.string.create_room_modal_header

        val positiveButtonRef: Int = if (roomViewModel.getIsEdit())
            R.string.change
        else
            R.string.create

        return builder
            .setView(layout!!.root)
            .setTitle(requireActivity().resources.getText(titleRef))
            .setPositiveButton(positiveButtonRef) { dialog, _ -> onSubmit(dialog) }
            .create()
    }

    private fun onSubmit(dialogInterface: DialogInterface) {
        layout?.let {
            try {
                val address: String? = it.roomAddressInput.text?.toString()
                val description: String? = it.roomDescriptionInput.text?.toString()
                val floor: Int? = it.roomFloorInput.text?.toString()?.toInt()
                val places: Int? = it.roomPlacesInput.text?.toString()?.toInt()

                val isAddressValid: Boolean = address != null && address.isNotBlank()
                val isDescriptionValid: Boolean = description != null && description.isNotBlank()

                if (isAddressValid && isDescriptionValid && floor != null && places != null) {
                    roomViewModel.onSubmit(
                        RoomDto(
                            type = typeId,
                            address = address!!,
                            description = description!!,
                            floor = floor,
                            places = places
                        )
                    )
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()
                dialogInterface.cancel()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        typesObserver?.let { typeViewModel.types.observe(this, it) }
    }

    override fun onStop() {
        super.onStop()
        typesObserver?.let { typeViewModel.types.removeObserver(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layout = null
    }

    override fun onClick(v: View?) {
        layout?.let {
            if (v is RadioButton) {
                typeId = v.id
            }
        }
    }

    private fun onChangeTypes(types: List<Type>) {
        layout?.let { it ->
            types.forEach { type ->
                val radioButton = RadioButton(context)
                typeId = if (roomViewModel.getIsEdit()) roomViewModel.getCurrentRoom().type.id else types[0].id

                radioButton.id = type.id
                radioButton.text = type.name
                radioButton.isChecked = radioButton.id == typeId

                radioButton.setOnClickListener(this)
                it.typeRadioGroup.addView(radioButton)
            }
        }
    }
}