package id.lifx.motionstudio.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.lifx.motionstudio.ui.theme.TextMain

@Composable
fun Ic(@DrawableRes id: Int, tint: Color = TextMain, size: Dp = 24.dp) {
    Icon(
        painter = painterResource(id),
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(size)
    )
}
