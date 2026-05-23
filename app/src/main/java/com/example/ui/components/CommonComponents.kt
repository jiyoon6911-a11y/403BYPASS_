package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Universal accessibility scaling text component.
 * Automatically multiplies the base text size by the global viewmodel scale.
 */
@Composable
fun BypassText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    fontFamily: FontFamily? = null,
    style: TextStyle = LocalTextStyle.current,
    textScale: Float = 1.0f,
    voiceNarrationText: String? = null,
    onNarrate: ((String) -> Unit)? = null
) {
    val scaledSize = (fontSize.value * textScale).sp
    val contentColor = if (color == Color.Unspecified) {
        MaterialTheme.colorScheme.onBackground
    } else {
        color
    }

    val finalModifier = if (voiceNarrationText != null && onNarrate != null) {
        modifier.clickable {
            onNarrate(voiceNarrationText)
        }
    } else {
        modifier
    }

    Text(
        text = text,
        modifier = finalModifier.semantics {
            contentDescription = voiceNarrationText ?: text
        },
        fontSize = scaledSize,
        fontWeight = fontWeight,
        color = contentColor,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        fontFamily = fontFamily,
        style = style.copy(lineHeight = (scaledSize.value * 1.5).sp)
    )
}

/**
 * High tactile-size button with built-in accessibility safeguards.
 * Enforces a minimum 48dp height requirement to meet WCAG standards.
 */
@Composable
fun BypassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "bypass_button",
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ),
    border: BorderStroke? = null,
    shape: RoundedCornerShape = RoundedCornerShape(28.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    isEnabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .minimumInteractiveComponentSize() // Enforce 48dp min targets
            .semantics {
                this.testTag = testTag
            },
        colors = colors,
        border = border,
        shape = shape,
        contentPadding = contentPadding,
        enabled = isEnabled,
        content = content
    )
}

/**
 * Section title labeled with neon accessibility side-bar.
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    textScale: Float = 1.0f,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            BypassText(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textScale = textScale,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        action?.invoke()
    }
}

/**
 * Shows active accessibility features on a dashboard card.
 */
@Composable
fun BypassBadgeRow(
    hasElevator: Boolean,
    hasWheelchairSlot: Boolean,
    hasAccessibleRestroom: Boolean,
    hasAudioGuides: Boolean,
    modifier: Modifier = Modifier,
    textScale: Float = 1.0f
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        BadgeTag(text = "엘리베이터", active = hasElevator, activeColor = NeonBlue, textScale = textScale)
        BadgeTag(text = "휠체어석", active = hasWheelchairSlot, activeColor = SafeGreen, textScale = textScale)
        BadgeTag(text = "장애인화장실", active = hasAccessibleRestroom, activeColor = WarningYellow, textScale = textScale)
        BadgeTag(text = "음성안내", active = hasAudioGuides, activeColor = ElectricCyan, textScale = textScale)
    }
}

@Composable
fun BadgeTag(
    text: String,
    active: Boolean,
    activeColor: Color,
    textScale: Float = 1.0f
) {
    Box(
        modifier = Modifier
            .background(
                color = if (active) activeColor.copy(alpha = 0.15f) else SoftGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (active) activeColor.copy(alpha = 0.8f) else SoftGray.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        BypassText(
            text = text,
            fontSize = 11.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            color = if (active) activeColor else TextSecondary,
            textScale = textScale
        )
    }
}

/**
 * Live audio narrator console simulator. Displays what is currently announced to low-vision/blind users.
 */
@Composable
fun VoiceNarratorConsole(
    text: String,
    isActive: Boolean,
    textScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    if (!isActive) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "음성 안내 중",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .animateContentSize()
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                BypassText(
                    text = "403 AUDIO ASSIST (스크린 리더 시뮬레이터)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textScale = textScale
                )
                Spacer(modifier = Modifier.height(2.dp))
                AnimatedContent(
                    targetState = text,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                    },
                    label = "tts_lines"
                ) { stringState ->
                    BypassText(
                        text = "“$stringState”",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        textScale = textScale
                    )
                }
            }
        }
    }
}
