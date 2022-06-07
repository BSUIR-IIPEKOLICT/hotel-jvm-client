package loshica.hotel.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import loshica.hotel.databinding.CommentFragmentBinding
import loshica.hotel.interfaces.IPickHandler
import loshica.hotel.models.Comment
import loshica.hotel.viewModels.CommentViewModel
import loshica.hotel.views.adapters.CommentAdapter
import loshica.hotel.views.modals.CommentModal

class CommentFragment : Fragment(), View.OnClickListener, IPickHandler {

    private var layout: CommentFragmentBinding? = null
    private val commentViewModel: CommentViewModel by activityViewModels()

    private var commentsObserver: Observer<List<Comment>>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = CommentFragmentBinding.inflate(inflater, container, false)
        val commentAdapter = CommentAdapter(this)

        with (layout!!) {
            commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            commentRecyclerView.adapter = commentAdapter
        }

        layout!!.commentCreateButton.setOnClickListener(this)
        commentsObserver = Observer { commentAdapter.update(it) }

        return layout?.root
    }

    override fun onClick(v: View?) {
        layout?.let {
            when(v) {
                it.commentCreateButton -> {
                    commentViewModel.setCurrentComment(null)
                    commentViewModel.setIsEdit(false)
                    CommentModal().show(requireActivity().supportFragmentManager, null)
                }
                else -> {}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        commentsObserver?.let { commentViewModel.comments.observe(this, it) }
    }

    override fun onResume() {
        super.onResume()
        commentViewModel.loadComments()
    }

    override fun onStop() {
        super.onStop()
        commentsObserver?.let { commentViewModel.comments.removeObserver(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layout = null
    }

    override fun onPickCard(position: Int) {
        commentViewModel.setCurrentComment(position)
        commentViewModel.setIsEdit(true)
        CommentModal().show(requireActivity().supportFragmentManager, null)
    }
}