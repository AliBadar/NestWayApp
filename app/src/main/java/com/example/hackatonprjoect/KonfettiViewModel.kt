package com.example.hackatonprjoect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hackatonprjoect.common.utils.AppObserver
import com.example.hackatonprjoect.common.utils.AppPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.models.Shape
import javax.inject.Inject

@HiltViewModel
class KonfettiViewModel @Inject constructor(
    private val application: Application,
) : AndroidViewModel(application) {
    private val _state = MutableLiveData<State>(State.Idle)
    val state: LiveData<State> = _state

    private val _totalPoints = MutableSharedFlow<Int>()
    val totalPoints = _totalPoints.asSharedFlow()

    init {
        ended()
        getTotalPoints()
    }

    fun getTotalPoints() {
        viewModelScope.launch {
            _totalPoints.emit(AppPreference.getUserPoints(application).first().toInt())
        }

    }

    fun festive() {
        /**
         * See [Presets] for this configuration
         */
        _state.value = State.Started(Presets.festive())
    }

    fun explode() {
        /**
         * See [Presets] for this configuration
         */
        _state.value = State.Started(Presets.explode())
    }

    fun parade() {
        /**
         * See [Presets] for this configuration
         */
        _state.value = State.Started(Presets.parade())
    }

    fun rain() {
        /**
         * See [Presets] for this configuration
         */
        _state.value = State.Started(Presets.rain())
    }

    fun ended() {
        _state.value = State.Idle
    }

    fun updateTotalPoints(totalPoints: Int) {
        viewModelScope.launch {
            AppPreference.saveUserPoints(application, totalPoints.toString())
        }
    }


    sealed class State {
        class Started(val party: List<Party>) : State()

        object Idle : State()
    }
}