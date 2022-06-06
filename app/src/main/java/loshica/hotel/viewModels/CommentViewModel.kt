package loshica.hotel.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import loshica.hotel.core.BaseViewModel
import loshica.hotel.models.Comment
import loshica.hotel.shared.Default

class CommentViewModel(override val app: Application): BaseViewModel(app) {

    val comments: MutableLiveData<List<Comment>> = MutableLiveData(emptyList())
    val currentComment: MutableLiveData<Comment> = MutableLiveData(Default.COMMENT)

    private var isEdit: Boolean = false
    private var roomId: Int = 0

    fun loadComments() {
        if (roomId < 1) return

        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                api.roomRepository.getRoomComments(roomId).let {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            comments.value = it.body()
                        } else {
                            throw Exception(it.message())
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e.message)
            }
        })
    }

    fun getIsEdit(): Boolean = isEdit

    fun setCurrentComment(index: Int?) {
        comments.value?.let {
            currentComment.value = if (index == null) Default.COMMENT else it[index]
        }
    }

    fun setRoomId(value: Int) {
        roomId = value
    }

    fun setIsEdit(value: Boolean) {
        isEdit = value
    }

    private fun createComment(content: String) {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                api.commentRepository.create(Comment(room = roomId, content = content)).let {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful && it.body() != null) {
                            Toast.makeText(
                                app.applicationContext,
                                "Comment created",
                                Toast.LENGTH_SHORT
                            ).show()

                            comments.value = (comments.value ?: emptyList()).plusElement(it.body()!!)
                        } else {
                            throw Exception(it.message())
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e.message)
            }
        })
    }

    private fun changeComment(content: String) {
        val currentCommentId: Int = currentComment.value?.id ?: return

        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                api.commentRepository
                    .change(currentCommentId, Comment(room = roomId, content = content))
                    .let {
                        withContext(Dispatchers.Main) {
                            if (it.isSuccessful) {
                                val changedComment: Comment? = it.body()

                                Toast.makeText(
                                    app.applicationContext,
                                    "Comment changed",
                                    Toast.LENGTH_SHORT
                                ).show()

                                currentComment.value = changedComment
                                comments.value = comments.value
                                    ?.map { comment ->
                                        if (comment.id == changedComment?.id) changedComment else comment
                                    }
                                    ?: emptyList()
                            } else {
                                throw Exception(it.message())
                            }
                        }
                    }
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isEdit = false
            }
        })
    }

    fun onSubmit(content: String) = if (!isEdit) createComment(content) else changeComment(content)

    fun deleteComment() {
        val currentCommentId: Int = currentComment.value?.id ?: return

        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                api.commentRepository.delete(currentCommentId).let {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                app.applicationContext,
                                "Comment deleted",
                                Toast.LENGTH_SHORT
                            ).show()

                            currentComment.value = Default.COMMENT
                            comments.value = comments.value?.filter { comment ->
                                comment.id != it.body()?.id
                            }
                        } else {
                            throw Exception(it.message())
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e.message)
            }
        })
    }
}