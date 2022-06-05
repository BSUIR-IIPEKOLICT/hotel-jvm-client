package loshica.hotel.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import loshica.hotel.core.BaseViewModel

class ConnectionViewModel(override val app: Application) : BaseViewModel(app) {

    val hasConnection: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkConnection() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.mainRepository.healthCheck().let {
                if (!it.isSuccessful) {
                    withContext(Dispatchers.Main) { onError("No stable connection to server") }
                    delay(2000).let { hasConnection.value = false }
                }
            }
        })
    }
}