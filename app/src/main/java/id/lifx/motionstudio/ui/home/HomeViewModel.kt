package id.lifx.motionstudio.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.lifx.motionstudio.data.AppDatabase
import id.lifx.motionstudio.data.ProjectEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortMode(val label: String) {
    TERBARU("Terbaru"),
    NAMA("Nama A-Z"),
    DURASI("Durasi")
}

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).projectDao()

    val query = MutableStateFlow("")
    val sort = MutableStateFlow(SortMode.TERBARU)

    val projects: StateFlow<List<ProjectEntity>> =
        combine(dao.observeAll(), query, sort) { list, q, s ->
            val filtered = if (q.isBlank()) list
            else list.filter { it.name.contains(q.trim(), ignoreCase = true) }
            when (s) {
                SortMode.TERBARU -> filtered.sortedByDescending { it.updatedAt }
                SortMode.NAMA -> filtered.sortedBy { it.name.lowercase() }
                SortMode.DURASI -> filtered.sortedByDescending { it.durationMs }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun create(
        name: String,
        width: Int,
        height: Int,
        ratioLabel: String,
        fps: Int,
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val id = dao.insert(
                ProjectEntity(
                    name = name.trim().ifBlank { "Proyek Baru" },
                    width = width,
                    height = height,
                    ratioLabel = ratioLabel,
                    fps = fps,
                    createdAt = now,
                    updatedAt = now
                )
            )
            onCreated(id)
        }
    }

    fun delete(project: ProjectEntity) {
        viewModelScope.launch { dao.delete(project) }
    }
}
