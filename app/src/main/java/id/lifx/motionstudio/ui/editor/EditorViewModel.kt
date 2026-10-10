package id.lifx.motionstudio.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import id.lifx.motionstudio.data.AppDatabase
import id.lifx.motionstudio.data.ProjectEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditorViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).projectDao()
    private val projectId: Long = handle.get<Long>("projectId") ?: -1L

    val project: StateFlow<ProjectEntity?> =
        dao.observe(projectId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Tandai proyek sebagai baru diedit saat dibuka.
        viewModelScope.launch {
            dao.observe(projectId).first()?.let {
                dao.update(it.copy(updatedAt = System.currentTimeMillis()))
            }
        }
    }
}
