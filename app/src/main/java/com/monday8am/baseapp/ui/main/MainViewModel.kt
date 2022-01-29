package com.monday8am.baseapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monday8am.baseapp.domain.Result
import com.monday8am.baseapp.domain.model.SortMethod
import com.monday8am.baseapp.domain.model.User
import com.monday8am.baseapp.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

sealed class DataState {
    object Idle : DataState()
    object Loading : DataState()
    data class Error(val message: String) : DataState()
}

data class MainUiState(
    val state: DataState = DataState.Idle,
    val users: List<User> = listOf(),
    val sortMethod: SortMethod = SortMethod.NAME
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsers: GetUsers,
    private val saveSortMethod: SaveSortMethod,
    private val addUser: AddUser,
    private val removeUser: RemoveUser
) : ViewModel(), CoroutineScope {

    private val uiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = uiState.asStateFlow()

    init {
        uiState.update { it.copy(state = DataState.Loading) }

        launch {
            getUsers(Unit).collect { result ->
                when (result) {
                    is Result.Success -> uiState.update {
                        it.copy(users = result.data, state = DataState.Idle)
                    }
                    is Result.Error -> uiState.update {
                        it.copy(state = DataState.Error("Message!"))
                    }
                }
            }
        }
    }

    fun addRandomUser() {
        launch {
            val user = User(
                "Anton - " + Random.nextInt(0, 10000000),
                "Dev",
                "Android",
                ""
            )
            addUser(user)
        }
    }

    fun removeUserFronList(userId: String) {
        launch {
            removeUser(userId)
        }
    }

    fun sortItems() {
        launch {
            saveSortMethod(Unit)
            uiState.update { state ->
                state.copy(users = state.users.sortedBy { it.name })
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext
}
