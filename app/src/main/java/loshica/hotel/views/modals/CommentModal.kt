package loshica.hotel.views.modals

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import loshica.hotel.R
import loshica.hotel.databinding.CommentModalBinding
import loshica.hotel.models.Comment
import loshica.hotel.viewModels.CommentViewModel
import loshica.vendor.view.LOSDialogBuilder

class CommentModal : DialogFragment() {

    private var layout: CommentModalBinding? = null
    private val commentViewModel: CommentViewModel by activityViewModels()

    private var currentCommentObserver: Observer<Comment>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = LOSDialogBuilder(requireActivity())

        layout = CommentModalBinding.inflate(requireActivity().layoutInflater)

        currentCommentObserver = Observer {
            if (commentViewModel.getIsEdit()) {
                layout?.commentContentInput?.setText(it.content)
            }
        }

        val titleRef: Int = if (commentViewModel.getIsEdit())
            R.string.change_comment_modal_header
        else
            R.string.create_comment_modal_header

        val positiveButtonRef: Int = if (commentViewModel.getIsEdit())
            R.string.change
        else
            R.string.create

        val negativeButtonRef: Int = if (commentViewModel.getIsEdit())
            R.string.delete
        else
            R.string.cancel

        return builder
            .setView(layout!!.root)
            .setTitle(requireActivity().resources.getText(titleRef))
            .setPositiveButton(positiveButtonRef) { dialog, _ -> onSubmit(dialog) }
            .setNegativeButton(negativeButtonRef) { dialog, _ -> onDelete(dialog) }
            .create()
    }

    private fun onSubmit(dialogInterface: DialogInterface) {
        layout?.let {
            val content: String? = it.commentContentInput.text?.toString()

            if (content != null && content.trim().isNotEmpty()) {
                commentViewModel.onSubmit(content)
            } else {
                Toast.makeText(requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()
                dialogInterface.cancel()
            }
        }
    }

    private fun onDelete(dialogInterface: DialogInterface) {
        if (commentViewModel.getIsEdit()) {
            commentViewModel.setIsEdit(false)
            commentViewModel.deleteComment()
        }

        dialogInterface.cancel()
    }

    override fun onStart() {
        super.onStart()
        currentCommentObserver?.let { commentViewModel.currentComment.observe(this, it) }
    }

    override fun onStop() {
        super.onStop()
        currentCommentObserver?.let { commentViewModel.currentComment.removeObserver(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layout = null
    }
}