package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BypassButton
import com.example.ui.components.BypassText
import com.example.ui.components.VoiceNarratorConsole
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.BypassTab
import com.example.ui.viewmodel.BypassViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vModel: BypassViewModel = viewModel()
            
            // Collect global accessibility states
            val textScale by vModel.textScale.collectAsState()
            val highContrastMode by vModel.highContrastMode.collectAsState()
            val ttsActive by vModel.ttsSimulatedActive.collectAsState()
            val ttsLine by vModel.ttsNarrativeLine.collectAsState()
            val currentTab by vModel.currentTab.collectAsState()

            // State for overlaying accessibility settings panel
            var showSettingsOverlay by remember { mutableStateOf(false) }

            MyApplicationTheme(highContrastMode = highContrastMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Custom accessible navigation bar respecting bottom gestural bar safe drawing zones
                        CustomNavigationBar(
                            selectedTab = currentTab,
                            textScale = textScale,
                            onTabSelected = { vModel.setTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        // Screen Swapping Dispatcher
                        when (currentTab) {
                            BypassTab.HOME -> HomeScreen(
                                viewModel = vModel,
                                textScale = textScale,
                                onNavigate = { vModel.setTab(BypassTab.MOBILITY) },
                                onNarrate = { vModel.triggerTtsAnnouncement(it) }
                            )
                            BypassTab.MOBILITY -> MobilityScreen(
                                viewModel = vModel,
                                textScale = textScale,
                                onNarrate = { vModel.triggerTtsAnnouncement(it) }
                            )
                            BypassTab.VISIBILITY -> VisibilityScreen(
                                viewModel = vModel,
                                textScale = textScale,
                                onNarrate = { vModel.triggerTtsAnnouncement(it) }
                            )
                            BypassTab.FLOW_SAFETY -> FlowScreen(
                                viewModel = vModel,
                                textScale = textScale,
                                onNarrate = { vModel.triggerTtsAnnouncement(it) }
                            )
                            BypassTab.ECOSYSTEM -> EcosystemScreen(
                                viewModel = vModel,
                                textScale = textScale,
                                onNarrate = { vModel.triggerTtsAnnouncement(it) }
                            )
                        }

                        // Floating Quick Universal Controller Access Indicator (Universal Design)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 100.dp)
                        ) {
                            FloatingActionButton(
                                onClick = { showSettingsOverlay = !showSettingsOverlay },
                                containerColor = NeonBlue,
                                contentColor = Color.White,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (showSettingsOverlay) Icons.Default.Close else Icons.Default.Settings,
                                    contentDescription = "유니버설 접근성 설정 패널 열기",
                                    tint = Color.White
                                )
                            }
                        }

                        // Animated overlay panel for accessibility adjustment
                        AnimatedVisibility(
                            visible = showSettingsOverlay,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(2.dp, NeonBlue),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        BypassText(
                                            text = "⚙️ UNIVERSAL DESIGN CONTROL CENTER",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeonBlue,
                                            textScale = textScale
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "닫기",
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clickable { showSettingsOverlay = false },
                                            tint = Color.White
                                        )
                                    }

                                    Divider(color = SoftGray)

                                    // 1. Interactive scale controls
                                    Column {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            BypassText(
                                                text = "🔍 글자 및 구성 요소 확대 비율",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                textScale = textScale
                                            )
                                            BypassText(
                                                text = "${String.format("%.1f", textScale)}x",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeonBlue,
                                                textScale = textScale
                                            )
                                        }

                                        Slider(
                                            value = textScale,
                                            onValueChange = { scale -> vModel.updateTextScale(scale) },
                                            valueRange = 1.0f..1.8f,
                                            steps = 3,
                                            colors = SliderDefaults.colors(
                                                thumbColor = NeonBlue,
                                                activeTrackColor = NeonBlue
                                            )
                                        )
                                    }

                                    // 2. High contrast toggler
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            BypassText(
                                                text = "🖤 완벽 고대비 흑백 모드",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                textScale = textScale
                                            )
                                            BypassText(
                                                text = "배경을 완전한 검은색(#000000)으로 전환해 저시력 시각 가시도 보호",
                                                fontSize = 10.sp,
                                                color = Color.White.copy(alpha = 0.6f),
                                                textScale = textScale
                                            )
                                        }
                                        Switch(
                                            checked = highContrastMode,
                                            onCheckedChange = { vModel.toggleHighContrast() },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = NeonBlue,
                                                checkedTrackColor = NeonBlue.copy(alpha = 0.4f)
                                            )
                                        )
                                    }

                                    // 3. Screen reader voice toggler
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            BypassText(
                                                text = "🎙️ 인-앱 스크린 리더 콘솔 활성",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                textScale = textScale
                                            )
                                            BypassText(
                                                text = "터치와 선택 요소를 하단 해설창으로 자막화해주는 음성 안내 시뮬레이터",
                                                fontSize = 10.sp,
                                                color = Color.White.copy(alpha = 0.6f),
                                                textScale = textScale
                                            )
                                        }
                                        Switch(
                                            checked = ttsActive,
                                            onCheckedChange = { vModel.toggleTtsSimulated() },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = NeonBlue,
                                                checkedTrackColor = NeonBlue.copy(alpha = 0.4f)
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Bottom fixed screen reader console simulator
                        VoiceNarratorConsole(
                            text = ttsLine,
                            isActive = ttsActive,
                            textScale = textScale,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 76.dp) // Offset of navigation bottom bar
                        )
                    }
                }
            }
        }
    }
}

/**
 * Custom modern Accessible Navigation Bar styled with Neon pills and labels
 */
@Composable
fun CustomNavigationBar(
    selectedTab: BypassTab,
    textScale: Float,
    onTabSelected: (BypassTab) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(width = 0.6.dp, color = SoftGray),
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars) // Handle bottom notch safe drawing zones
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BypassTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val itemColor = if (isSelected) NeonBlue else TextSecondary
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .semantics {
                            contentDescription = when (tab) {
                                BypassTab.HOME -> "홈 화면"
                                BypassTab.MOBILITY -> "안내맵 화면"
                                BypassTab.VISIBILITY -> "매칭예약 화면"
                                BypassTab.FLOW_SAFETY -> "나의티켓 화면"
                                BypassTab.ECOSYSTEM -> "마이페이지 화면"
                            }
                        }
                ) {
                    Icon(
                        imageVector = when (tab) {
                            BypassTab.HOME -> Icons.Default.Home
                            BypassTab.MOBILITY -> Icons.Default.Map
                            BypassTab.VISIBILITY -> Icons.Default.CalendarMonth
                            BypassTab.FLOW_SAFETY -> Icons.Default.ConfirmationNumber
                            BypassTab.ECOSYSTEM -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = itemColor,
                        modifier = Modifier
                            .size(22.dp)
                            .animateContentSize()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    BypassText(
                        text = when (tab) {
                            BypassTab.HOME -> "홈"
                            BypassTab.MOBILITY -> "안내맵"
                            BypassTab.VISIBILITY -> "매칭예약"
                            BypassTab.FLOW_SAFETY -> "나의티켓"
                            BypassTab.ECOSYSTEM -> "마이페이지"
                        },
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = itemColor,
                        textScale = textScale
                    )
                }
            }
        }
    }
}
