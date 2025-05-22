import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class SnackbarType {
    SUCCESS,
    ERROR,
    INFO
}

data class SnackbarData(
    val message: String,
    val type: SnackbarType = SnackbarType.INFO,
    val duration: Long = 3000L,
    val actionLabel: String? = null,
    val onActionClick: (() -> Unit)? = null,
)

@Composable
fun TopSnackbar(
    snackbarData: SnackbarData?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Control visibility with AnimatedVisibility
    val visible = remember { MutableTransitionState(false) }
    
    // Update visibility based on snackbarData
    LaunchedEffect(snackbarData) {
        if (snackbarData != null) {
            visible.targetState = true
            // Auto dismiss after duration
            delay(snackbarData.duration)
            visible.targetState = false
            delay(300) // Wait for animation to complete
            onDismiss()
        } else {
            visible.targetState = false
        }
    }
    
    // Clean up when leaving composition
    DisposableEffect(Unit) {
        onDispose {
            visible.targetState = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AnimatedVisibility(
            visibleState = visible,
            enter = fadeIn(animationSpec = tween(300)) + 
                   slideInVertically(animationSpec = tween(300)) { -it },
            exit = fadeOut(animationSpec = tween(300)) + 
                   slideOutVertically(animationSpec = tween(300)) { -it }
        ) {
            if (snackbarData != null) {
                SnackbarContent(
                    snackbarData = snackbarData,
                    onDismiss = {
                        visible.targetState = false
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun SnackbarContent(
    snackbarData: SnackbarData,
    onDismiss: () -> Unit,
) {
    val (backgroundColor, contentColor, icon) = when (snackbarData.type) {
        SnackbarType.SUCCESS -> Triple(
            Color(0xFF4CAF50),  // Green
            Color.White,
            Icons.Default.Check
        )
        SnackbarType.ERROR -> Triple(
            Color(0xFFE53935),  // Red
            Color.White,
            Icons.Default.Error
        )
        SnackbarType.INFO -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            Icons.Default.Info
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDismiss() }
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = snackbarData.type.name,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = snackbarData.message,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (snackbarData.actionLabel != null && snackbarData.onActionClick != null) {
                    Text(
                        text = snackbarData.actionLabel,
                        color = contentColor,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { 
                                snackbarData.onActionClick.invoke()
                                onDismiss()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = contentColor,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDismiss() }
            )
        }
    }
}
