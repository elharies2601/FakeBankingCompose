package id.elharies.fakebanking.util.window

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthSize { Compact, Medium, Expanded }
enum class WindowHeightSize { Compact, Medium, Expanded }

data class WindowSizeInfo(
    val widthSize: WindowWidthSize,
    val heightSize: WindowHeightSize,
    val screenWidthDp: Dp,
    val screenHeightDp: Dp
) {
    val isTablet: Boolean get() = widthSize == WindowWidthSize.Expanded
    val isLandscape: Boolean get() = screenWidthDp > screenHeightDp
    val isMediumOrExpanded: Boolean get() = widthSize != WindowWidthSize.Compact

    /** Fraction of screen width a centered content card should occupy */
    val contentWidthFraction: Float
        get() = when (widthSize) {
            WindowWidthSize.Compact -> 1f
            WindowWidthSize.Medium -> 0.75f
            WindowWidthSize.Expanded -> 0.55f
        }

    /** Horizontal padding for list / page content */
    val horizontalPadding: Dp
        get() = when (widthSize) {
            WindowWidthSize.Compact -> 16.dp
            WindowWidthSize.Medium -> 32.dp
            WindowWidthSize.Expanded -> 48.dp
        }

    /** Number of grid columns for menu cards */
    val menuColumnCount: Int
        get() = when (widthSize) {
            WindowWidthSize.Compact -> 2
            WindowWidthSize.Medium -> 3
            WindowWidthSize.Expanded -> 4
        }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun rememberWindowSizeInfo(): WindowSizeInfo {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp.dp
    val screenHeightDp = config.screenHeightDp.dp

    return remember(config) {
        val widthSize = when {
            config.screenWidthDp < 600 -> WindowWidthSize.Compact
            config.screenWidthDp < 840 -> WindowWidthSize.Medium
            else -> WindowWidthSize.Expanded
        }
        val heightSize = when {
            config.screenHeightDp < 480 -> WindowHeightSize.Compact
            config.screenHeightDp < 900 -> WindowHeightSize.Medium
            else -> WindowHeightSize.Expanded
        }
        WindowSizeInfo(widthSize, heightSize, screenWidthDp, screenHeightDp)
    }
}
