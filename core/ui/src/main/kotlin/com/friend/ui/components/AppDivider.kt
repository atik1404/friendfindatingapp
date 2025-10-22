import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color,
    startIndent: Dp = 0.dp,
    orientation: Orientation = Orientation.Horizontal,
) {
    when (orientation) {
        Orientation.Horizontal -> {
            Box(
                modifier = modifier
                    .padding(start = startIndent)
                    .fillMaxWidth()
                    .height(thickness)
                    .background(color)
            )
        }
        Orientation.Vertical -> {
            Box(
                modifier = modifier
                    .padding(top = startIndent)
                    .fillMaxHeight()
                    .width(thickness)
                    .background(color)
            )
        }
    }
}
