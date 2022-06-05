package loshica.hotel.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import loshica.hotel.core.BaseViewModel
import loshica.hotel.models.Type

class TypeViewModel(override val app: Application): BaseViewModel(app) {

    val types: MutableLiveData<List<Type>> = MutableLiveData(emptyList())

    fun loadTypes() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            api.typeRepository.getAll().let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        types.value = it.body()
                    } else {
                        onError(it.message())
                    }
                }
            }
        })
    }

    fun getTypes(): List<Type> = types.value ?: emptyList()
}