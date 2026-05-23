package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.components.BypassText
import com.example.ui.components.SectionTitle
import com.example.ui.components.BypassButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.BypassViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcosystemScreen(
    viewModel: BypassViewModel,
    textScale: Float,
    onNarrate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Collect States from ViewModel
    val userNickname by viewModel.userNickname.collectAsState()
    val historyReviews by viewModel.historyReviews.collectAsState()
    val highContrastMode by viewModel.highContrastMode.collectAsState()
    val supportersApplied by viewModel.supportersApplied.collectAsState()

    // Screen-local states
    var showPersonalSettingsDialog by remember { mutableStateOf(false) }
    var showReviewDraftDialog by remember { mutableStateOf(false) }
    
    // Draft fields
    var draftPlayTitle by remember { mutableStateOf("특별 연극 (VR 시범운영)") }
    var draftRating by remember { mutableStateOf(5) }
    var draftReviewText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. TITLE PANEL WITH SETTINGS GEAR ICON ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                BypassText(
                    text = "MY PORTAL & SETTINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue,
                    textScale = textScale
                )
                BypassText(
                    text = "마이페이지",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    textScale = textScale
                )
            }

            // Settings Cog Icon (Gear icon requirement)
            IconButton(
                onClick = { 
                    showPersonalSettingsDialog = true 
                    viewModel.triggerTtsAnnouncement("설정 단추 선택. 개인 접근성 쉴드 설정을 진입합니다.")
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(SlateCard, shape = CircleShape)
                    .border(BorderStroke(1.dp, SoftGray), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "개인 설정 진입",
                    tint = NeonBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // --- 2. USER PROFILE BLOCK (MANDATORY REQUIREMENT) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateCard),
            border = BorderStroke(1.2.dp, NeonBlue.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Highly Styled Profile Avatar representation (Geometric Balance)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(NeonBlue, shape = CircleShape)
                        .border(BorderStroke(2.dp, ElectricCyan), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    BypassText(
                        text = "장벽",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textScale = textScale
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    BypassText(
                        text = userNickname,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        textScale = textScale
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(ElectricCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            BypassText(
                                text = if (supportersApplied) "서포터즈 1기" else "일반 회원",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan,
                                textScale = textScale
                            )
                        }

                        if (supportersApplied) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(SafeGreen.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                BypassText(
                                    text = "근접성 어바이터 🏅",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafeGreen,
                                    textScale = textScale
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 3. VIEWING HISTORY & REVIEWS (MANDATORY REQUIREMENT) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BypassText(
                text = "🎭 관람 히스토리 및 리뷰",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textScale = textScale
            )

            // Button to simulate adding review
            Text(
                text = "+ 시범후기 작성",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                modifier = Modifier
                    .clickable { showReviewDraftDialog = true }
                    .padding(4.dp)
            )
        }

        if (historyReviews.isEmpty()) {
            // MANDATORY INITIAL STATE REQUIREMENT
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                border = BorderStroke(1.dp, SoftGray),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SpeakerNotesOff,
                        contentDescription = "기록없음",
                        tint = SoftGray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BypassText(
                        text = "아직 작성된 히스토리가 없습니다", // Exact string requested by user
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textScale = textScale,
                        textAlign = TextAlign.Center
                    )
                    BypassText(
                        text = "관람을 마치신 후, 우측 상단의 '+ 시범후기 작성' 버튼을 눌려 나만의 장벽 경험 평가 리스트를 채울 수 있습니다.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        textScale = textScale,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Rendered dynamically when filled
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                historyReviews.forEach { reviewItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateCard),
                        border = BorderStroke(1.dp, SoftGray),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            BypassText(
                                text = reviewItem,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textScale = textScale
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BypassText(
                                text = "✓ 인증됨: 장비 수령 완료 후 성실 검증 인센티브 자동 적립",
                                fontSize = 11.sp,
                                color = SafeGreen,
                                textScale = textScale
                            )
                        }
                    }
                }

                // Tool to clear history to return to empty state
                Text(
                    text = "전체 기록 지우기 (초기 상태 되돌리기)",
                    fontSize = 11.sp,
                    color = HazardRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { viewModel.clearHistory() }
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
            }
        }

        // --- 4. ACCESSIBILITY OVERVIEW INSIDE USER PAGE ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateCard),
            border = BorderStroke(1.dp, SoftGray),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BypassText(
                    text = "⚙️ 개인 접근성 선호 쉴드",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale
                )
                
                Divider(color = SoftGray)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BypassText(text = "현재 다크모드/고대비 전환 가동 여부", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                    BypassText(
                        text = if (highContrastMode) "활성 (저시력 모드)" else "일반 다크 모드",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrastMode) WarningYellow else NeonBlue,
                        textScale = textScale
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BypassText(text = "배리어프리 텍스트 스케일링", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                    BypassText(
                        text = "${String.format("%.1f", textScale)}배 확대 상태",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        textScale = textScale
                    )
                }
            }
        }

        // --- Dialog 1: Simulating Settings ---
        if (showPersonalSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showPersonalSettingsDialog = false },
                confirmButton = {
                    TextButton(onClick = { showPersonalSettingsDialog = false }) {
                        BypassText(text = "설정 저장", color = NeonBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                    }
                },
                title = {
                    BypassText(text = "⚙️ 개인화 프로필 설정", fontSize = 16.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        BypassText(text = "닉네임 수정", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                        OutlinedTextField(
                            value = "장벽없는예술가",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false // Mock text editing for simplicity
                        )
                        BypassText(text = "유닉 안전 진입 가동, 장애 환경 기본 수치 점검 등의 옵션을 변경할 수 있습니다.", fontSize = 11.sp, color = SoftGray, textScale = textScale)
                    }
                },
                containerColor = SlateCardHeader
            )
        }

        // --- Dialog 2: Create Sim Review ---
        if (showReviewDraftDialog) {
            AlertDialog(
                onDismissRequest = { showReviewDraftDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (draftReviewText.isNotEmpty()) {
                                viewModel.addPastReviewSimulation(draftPlayTitle, draftRating, draftReviewText)
                                draftReviewText = ""
                                showReviewDraftDialog = false
                            }
                        }
                    ) {
                        BypassText(text = "기록 보관", color = NeonBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReviewDraftDialog = false }) {
                        BypassText(text = "취소", color = TextSecondary, fontSize = 14.sp, textScale = textScale)
                    }
                },
                title = {
                    BypassText(text = "🎭 관람 후기 수집 일지 기록", fontSize = 16.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BypassText(text = "공연명 지정", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                        OutlinedTextField(
                            value = draftPlayTitle,
                            onValueChange = { draftPlayTitle = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        BypassText(text = "경험 평가 만족도 (1~5)", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            (1..5).forEach { rate ->
                                val isSelected = draftRating == rate
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(if (isSelected) NeonBlue else SlateDark, shape = CircleShape)
                                        .clickable { draftRating = rate }
                                        .border(BorderStroke(1.dp, if (isSelected) NeonBlue else SoftGray), shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BypassText(text = rate.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextPrimary, textScale = textScale)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        BypassText(text = "후기 내용", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                        OutlinedTextField(
                            value = draftReviewText,
                            onValueChange = { draftReviewText = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { BypassText(text = "접근성과 연출에 대한 솔직한 후기를 적어주세요.", fontSize = 11.sp, color = SoftGray, textScale = textScale) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                },
                containerColor = SlateCardHeader
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
