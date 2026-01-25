package com.friend.chatroom.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.surfaceColors
import com.friend.ui.components.AppText
import kotlinx.coroutines.launch
import timber.log.Timber
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordButton(
    recording: Boolean,
    isLocked: Boolean,
    swipeOffset: () -> Float,
    onSwipeOffsetChange: (Float) -> Unit,
    onStartRecording: () -> Boolean,
    onFinishRecording: () -> Unit,
    onCancelRecording: () -> Unit,
    onLockRecording: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition = updateTransition(targetState = recording, label = "record")
    val scale = transition.animateFloat(
        transitionSpec = { spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow) },
        label = "record-scale",
        targetValueByState = { rec -> if (rec) 2f else 1f },
    )
    val containerAlpha = transition.animateFloat(
        transitionSpec = { tween(2000) },
        label = "record-scale",
        targetValueByState = { rec -> if (rec) 1f else 0f },
    )
    val iconColor = transition.animateColor(
        transitionSpec = { tween(200) },
        label = "record-scale",
        targetValueByState = { rec ->
            if (rec) contentColorFor(LocalContentColor.current)
            else MaterialTheme.surfaceColors.white
        },
    )

    Box {
        // Background during recording
        Box(
            Modifier
                .matchParentSize()
                .aspectRatio(1f)
                .graphicsLayer {
                    alpha = containerAlpha.value
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .clip(CircleShape)
                .background(MaterialTheme.surfaceColors.primary.copy(alpha = .5f)),
        )
        val scope = rememberCoroutineScope()
        val tooltipState = remember { TooltipState() }
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                if (!isLocked)
                    RichTooltip {
                        AppText(stringResource(Res.string.msg_hold_to_record))
                    }
                else onFinishRecording.invoke()
            },
            enableUserInput = false,
            state = tooltipState,
        ) {
            Icon(
                painterResource(id = if (isLocked) Res.drawable.ic_stop_circle else Res.drawable.ic_mic),
                contentDescription = "Record voice message",
                tint = iconColor.value,
                modifier = modifier
                    .background(
                        MaterialTheme.surfaceColors.primary.copy(alpha = .5f),
                        CircleShape,
                    )
                    .sizeIn(minWidth = 45.dp, minHeight = 6.dp)
                    .appPadding(SpacingToken.medium)
                    .clickable {
                        if (isLocked) onFinishRecording.invoke()
                    }
                    .voiceRecordingGesture(
                        horizontalSwipeProgress = swipeOffset,
                        onSwipeProgressChanged = onSwipeOffsetChange,
                        onClick = { scope.launch { tooltipState.show() } },
                        onStartRecording = onStartRecording,
                        onFinishRecording = onFinishRecording,
                        onCancelRecording = onCancelRecording,
                        onLockRecording = onLockRecording,
                        isLocked = isLocked
                    ),
            )
        }
    }
}

private fun Modifier.voiceRecordingGesture(
    horizontalSwipeProgress: () -> Float,
    onSwipeProgressChanged: (Float) -> Unit,
    onClick: () -> Unit = {},
    onStartRecording: () -> Boolean = { false },
    onFinishRecording: () -> Unit = {},
    onCancelRecording: () -> Unit = {},
    onLockRecording: () -> Unit,
    isLocked: Boolean, // Ensure this is used in the logic below
    swipeToCancelThreshold: Dp = 200.dp,
    lockThreshold: Dp = 100.dp,
): Modifier = this
    .pointerInput(isLocked) { detectTapGestures { onClick() } }
    .pointerInput(isLocked) {
        // If already locked, we don't want to start a new drag-to-record session
        if (isLocked) return@pointerInput

        var offsetY = 0f
        var dragging = false
        val swipeToCancelThresholdPx = swipeToCancelThreshold.toPx()
        val lockThresholdPx = lockThreshold.toPx()

        detectDragGesturesAfterLongPress(
            onDragStart = {
                onSwipeProgressChanged(0f)
                offsetY = 0f
                dragging = true
                onStartRecording()
            },
            onDragCancel = {
                // ONLY cancel if we didn't successfully lock
                if (dragging && !isLocked) {
                    //onCancelRecording()
                }
                dragging = false
            },
            onDragEnd = {
                // ONLY finish if we didn't successfully lock
                if (dragging && !isLocked) {
                    onFinishRecording()
                }
                dragging = false
            },
            onDrag = { change, dragAmount ->
                if (dragging && !isLocked) {
                    change.consume()
                    offsetY += dragAmount.y
                    val offsetX = horizontalSwipeProgress() + dragAmount.x
                    onSwipeProgressChanged(offsetX)

                    // Check for Lock threshold
                    if (offsetY < -lockThresholdPx) {
                        onLockRecording()
                        // We don't set dragging = false here because
                        // the pointerInput(isLocked) key change will
                        // reset this detector anyway.
                    }
                    // Check for Cancel threshold
                    else if (offsetX < -swipeToCancelThresholdPx) {
                        dragging = false
                        onCancelRecording()
                    }
                }
            },
        )
    }