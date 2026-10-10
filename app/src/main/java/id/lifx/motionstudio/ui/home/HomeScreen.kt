package id.lifx.motionstudio.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.lifx.motionstudio.R
import id.lifx.motionstudio.data.ProjectEntity
import id.lifx.motionstudio.ui.components.Ic
import id.lifx.motionstudio.ui.theme.BrandBrush
import id.lifx.motionstudio.ui.theme.CardColor
import id.lifx.motionstudio.ui.theme.Cyan
import id.lifx.motionstudio.ui.theme.Danger
import id.lifx.motionstudio.ui.theme.Muted
import id.lifx.motionstudio.ui.theme.Neon
import id.lifx.motionstudio.ui.theme.Surface1
import id.lifx.motionstudio.ui.theme.TextMain
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTime(ms: Long): String {
    val tenths = ms / 100
    val sec = tenths / 10
    return String.format(Locale.US, "%02d:%02d.%d", sec / 60, sec % 60, tenths % 10)
}

private fun formatShort(ms: Long): String {
    val sec = ms / 1000
    return String.format(Locale.US, "%02d:%02d", sec / 60, sec % 60)
}

private fun formatDate(ms: Long): String =
    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(Date(ms))

@Composable
fun HomeScreen(
    onOpen: (Long) -> Unit,
    onSettings: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val projects by vm.projects.collectAsStateWithLifecycle()
    val query by vm.query.collectAsStateWithLifecycle()
    val sort by vm.sort.collectAsStateWithLifecycle()
    var showNew by remember { mutableStateOf(false) }
    var toDelete by remember { mutableStateOf<ProjectEntity?>(null) }
    var sortMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandBrush),
                    contentAlignment = Alignment.Center
                ) {
                    Ic(R.drawable.ic_keyframe, tint = Color.White, size = 22.dp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("LIFX", color = TextMain, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text("Motion Studio", color = Cyan, fontSize = 12.sp)
                }
                IconButton(onClick = onSettings) { Ic(R.drawable.ic_settings, tint = Muted) }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(BrandBrush)
                    .clickable { showNew = true },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Ic(R.drawable.ic_add, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Buat Proyek Baru", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = query,
                    onValueChange = { vm.query.value = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = { Text("Cari proyek", color = Muted) },
                    leadingIcon = { Ic(R.drawable.ic_search, tint = Muted, size = 20.dp) },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardColor,
                        unfocusedContainerColor = CardColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextMain,
                        unfocusedTextColor = TextMain,
                        cursorColor = Cyan
                    )
                )
                Spacer(Modifier.width(8.dp))
                Box {
                    IconButton(onClick = { sortMenu = true }) { Ic(R.drawable.ic_sort, tint = Cyan) }
                    DropdownMenu(expanded = sortMenu, onDismissRequest = { sortMenu = false }) {
                        SortMode.values().forEach { mode ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        mode.label,
                                        color = if (mode == sort) Cyan else TextMain
                                    )
                                },
                                onClick = {
                                    vm.sort.value = mode
                                    sortMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
        if (projects.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(CardColor)
                            .border(BorderStroke(1.dp, Purple40), RoundedCornerShape(26.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Ic(R.drawable.ic_video, tint = Neon, size = 40.dp)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        if (query.isBlank()) "Belum ada proyek" else "Proyek tidak ditemukan",
                        color = TextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (query.isBlank()) "Ketuk Buat Proyek Baru untuk memulai."
                        else "Coba kata kunci lain.",
                        color = Muted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(projects, key = { it.id }) { p ->
                ProjectCard(p, onClick = { onOpen(p.id) }, onDelete = { toDelete = p })
            }
        }
    }

    if (showNew) {
        NewProjectDialog(
            onDismiss = { showNew = false },
            onCreate = { name, w, h, label, fps ->
                showNew = false
                vm.create(name, w, h, label, fps) { id -> onOpen(id) }
            }
        )
    }

    toDelete?.let { p ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            containerColor = Surface1,
            title = { Text("Hapus proyek?", color = TextMain) },
            text = { Text("\"${p.name}\" akan dihapus permanen.", color = Muted) },
            confirmButton = {
                TextButton(onClick = {
                    vm.delete(p)
                    toDelete = null
                }) { Text("Hapus", color = Danger) }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Batal", color = Muted) }
            }
        )
    }
}

private val Purple40 = Color(0x668B5CF6)

@Composable
private fun ProjectCard(p: ProjectEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ratio = p.width.toFloat() / p.height.toFloat()
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF241552), Color(0xFF0B2A3A)))),
            contentAlignment = Alignment.Center
        ) {
            val iw = if (ratio >= 1f) 48.dp else 48.dp * ratio
            val ih = if (ratio >= 1f) 48.dp / ratio else 48.dp
            Box(
                modifier = Modifier
                    .size(iw, ih)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BrandBrush)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                p.name,
                color = TextMain,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "${p.width}x${p.height} - ${p.ratioLabel} - ${formatShort(p.durationMs)} - ${p.fps} fps",
                color = Cyan,
                fontSize = 12.sp
            )
            Text("Diedit ${formatDate(p.updatedAt)}", color = Muted, fontSize = 11.sp)
        }
        IconButton(onClick = onDelete) { Ic(R.drawable.ic_delete, tint = Muted, size = 20.dp) }
    }
}

private data class RatioOption(val label: String, val rw: Int, val rh: Int)

private val ratios = listOf(
    RatioOption("9:16", 9, 16),
    RatioOption("16:9", 16, 9),
    RatioOption("1:1", 1, 1),
    RatioOption("4:5", 4, 5)
)

@Composable
private fun Chip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Purple40 else CardColor)
            .border(
                BorderStroke(1.dp, if (selected) Cyan else Color.Transparent),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, color = if (selected) Color.White else Muted, fontSize = 13.sp)
    }
}

@Composable
private fun NewProjectDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, width: Int, height: Int, ratioLabel: String, fps: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ratioIdx by remember { mutableIntStateOf(0) }
    var shortSide by remember { mutableIntStateOf(1080) }
    var fps by remember { mutableIntStateOf(30) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface1,
        title = { Text("Proyek Baru", color = TextMain, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    placeholder = { Text("Nama proyek", color = Muted) },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardColor,
                        unfocusedContainerColor = CardColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextMain,
                        unfocusedTextColor = TextMain,
                        cursorColor = Cyan
                    )
                )
                Text("Rasio", color = Muted, fontSize = 12.sp)
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ratios.forEachIndexed { i, r ->
                        Chip(r.label, ratioIdx == i) { ratioIdx = i }
                    }
                }
                Text("Resolusi", color = Muted, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip("720p", shortSide == 720) { shortSide = 720 }
                    Chip("1080p", shortSide == 1080) { shortSide = 1080 }
                }
                Text("Frame rate", color = Muted, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(24, 30, 60).forEach { f ->
                        Chip("$f fps", fps == f) { fps = f }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val r = ratios[ratioIdx]
                val w: Int
                val h: Int
                if (r.rw >= r.rh) {
                    h = shortSide
                    w = shortSide * r.rw / r.rh
                } else {
                    w = shortSide
                    h = shortSide * r.rh / r.rw
                }
                onCreate(name, (w / 2) * 2, (h / 2) * 2, r.label, fps)
            }) { Text("Buat", color = Cyan, fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = Muted) }
        }
    )
}
