package id.lifx.motionstudio.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.lifx.motionstudio.ui.theme.CardColor
import id.lifx.motionstudio.ui.theme.Cyan
import id.lifx.motionstudio.ui.theme.Muted
import id.lifx.motionstudio.ui.theme.TextMain

@Composable
fun SettingsScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Pengaturan", color = TextMain, fontWeight = FontWeight.Black, fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))
        InfoCard(
            "LIFX Motion Studio",
            "Versi 0.1.0 (Tahap 1)\nEditor video Android berbasis Kotlin dan Jetpack Compose."
        )
        Spacer(Modifier.height(12.dp))
        InfoCard(
            "Sudah berfungsi",
            "Buat, cari, urutkan, buka, dan hapus proyek (tersimpan lokal dengan Room).\n" +
                "Pratinjau kanvas 9:16, 16:9, 1:1, 4:5 dengan playhead, grid, dan safe area."
        )
        Spacer(Modifier.height(12.dp))
        InfoCard(
            "Tahap berikutnya",
            "Impor media, timeline multi-layer, transform dan keyframe, efek, teks, audio, serta ekspor MP4."
        )
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardColor)
            .padding(16.dp)
    ) {
        Text(title, color = Cyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Text(body, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
    }
}
