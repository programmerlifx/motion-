package id.lifx.motionstudio.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val width: Int,
    val height: Int,
    val ratioLabel: String,
    val fps: Int = 30,
    val durationMs: Long = 10_000L,
    val createdAt: Long,
    val updatedAt: Long
)
