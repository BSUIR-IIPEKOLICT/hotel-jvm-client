package loshica.hotel.core

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Job
import loshica.hotel.R
import loshica.hotel.interfaces.IApi
import loshica.hotel.shared.Api

abstract class BaseViewModel(protected open val app: Application) : AndroidViewModel(app) {

    protected val jobs: MutableSet<Job> = mutableSetOf()
    protected val api: IApi = Api

    protected fun onError(message: String?) {
        Toast.makeText(
            app.applicationContext,
            "Error: ${message ?: app.resources.getString(R.string.connection_error)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onDestroy() {
        jobs.forEach { it.cancel() }
    }
}