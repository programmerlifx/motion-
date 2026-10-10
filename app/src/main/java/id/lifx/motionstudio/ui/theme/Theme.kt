package id.lifx.motionstudio.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Bg = Color(0xFF070A14)
val Surface1 = Color(0xFF0E1324)
val CardColor = Color(0xFF151B33)
val Purple = Color(0xFF8B5CF6)
val Neon = Color(0xFFB66DFF)
val Cyan = Color(0xFF22D3EE)
val TextMain = Color(0xFFEAEFFF)
val Muted = Color(0xFF8A93B5)
val Danger = Color(0xFFFF5C7A)

val BrandBrush = Brush.linearGradient(listOf(Purple, Cyan))

private val Scheme = darkColorScheme(
    primary = Purple,
    secondary = Cyan,
    background = Bg,
    surface = Surface1,
    onPrimary = Color.White,
    onBackground = TextMain,
    onSurface = TextMain,
    surfaceVariant = CardColor,
    onSurfaceVariant = Muted
)

@Composable
fun LifxTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, content = content)
}
