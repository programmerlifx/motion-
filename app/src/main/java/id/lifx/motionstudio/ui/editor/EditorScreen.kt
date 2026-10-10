package id.lifx.motionstudio.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.withFrameNanos
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.lifx.motionstudio.R
import id.lifx.motionstudio.ui.components.Ic
import id.lifx.motionstudio.ui.home.formatTime
import id.lifx.motionstudio.ui.theme.Bg
import id.lifx.motionstudio.ui.theme.BrandBrush
import id.lifx.motionstudio.ui.theme.CardColor
import id.lifx.motionstudio.ui.theme.Cyan
import id.lifx.motionstudio.ui.theme.Muted
import id.lifx.motionstudio.ui.theme.Purple
import id.lifx.motionstudio.ui.theme.TextMain

@Composable
fun EditorScreen(onBack: () -> Unit, vm: EditorViewModel = viewModel()) {
    val project by vm.project.collectAsStateWithLifecycle()
    val p = project

    if (p == null) {
        Box(Modifier.fillMaxSize().background(Bg), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Cyan)
        }
        return
    }

    var playhead by remember { mutableLongStateOf(0L) }
    var playing by remember { mutableStateOf(false) }
    var showGrid by remember { mutableStateOf(true) }
    var showSafe by remember { mutableStateOf(false) }
    val duration = p.durationMs

    LaunchedEffect(playing) {
        if (playing) {
            if (playhead >= duration) playhead = 0L
            var last = withFrameNanos { it }
            while (playing) {
                val now = withFrameNanos { it }
                val delta = (now - last) / 1_000_000L
                last = now
                playhead = minOf(duration, playhead + delta)
                if (playhead >= duration) playing = false
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Ic(R.drawable.ic_back) }
            Column(Modifier.weight(1f)) {
                Text(p.name, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Text(
                    "${p.width}x${p.height} - ${p.ratioLabel} - ${p.fps} fps",
                    color = Cyan,
                    fontSize = 11.sp
                )
            }
        }

        BoxWithConstraints(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val ratio = p.width.toFloat() / p.height.toFloat()
            val fitW = if (maxWidth / maxHeight > ratio) maxHeight * ratio else maxWidth
            val fitH = fitW / ratio
            Box(
                modifier = Modifier
                    .size(fitW, fitH)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF1B1140), Color(0xFF0B2A3A))))
                    .drawBehind {
                        if (showGrid) {
                            for (i in 1..2) {
                                val x = size.width * i / 3f
                                val y = size.height * i / 3f
                                drawLine(Color(0x33FFFFFF), Offset(x, 0f), Offset(x, size.height), 1f)
                                drawLine(Color(0x33FFFFFF), Offset(0f, y), Offset(size.width, y), 1f)
                            }
                        }
                        if (showSafe) {
                            drawRect(
                                color = Color(0x9922D3EE),
                                topLeft = Offset(size.width * 0.05f, size.height * 0.05f),
                                size = Size(size.width * 0.9f, size.height * 0.9f),
                                style = Stroke(width = 2f)
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(formatTime(playhead), color = Color(0x88FFFFFF), fontSize = 13.sp)
            }
        }

        Column(Modifier.padding(horizontal = 16.dp)) {
            Slider(
                value = playhead.toFloat(),
                onValueChange = {
                    playing = false
                    playhead = it.toLong()
                },
                valueRange = 0f..duration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Cyan,
                    activeTrackColor = Purple,
                    inactiveTrackColor = CardColor
                )
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BrandBrush)
                        .clickable { playing = !playing },
                    contentAlignment = Alignment.Center
                ) {
                    Ic(if (playing) R.drawable.ic_pause else R.drawable.ic_play, tint = Color.White)
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    "${formatTime(playhead)} / ${formatTime(duration)}",
                    color = TextMain,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showGrid = !showGrid }) {
                    Ic(R.drawable.ic_grid, tint = if (showGrid) Cyan else Muted)
                }
                IconButton(onClick = { showSafe = !showSafe }) {
                    Ic(R.drawable.ic_safe, tint = if (showSafe) Cyan else Muted)
                }
            }
        }
    }
}
