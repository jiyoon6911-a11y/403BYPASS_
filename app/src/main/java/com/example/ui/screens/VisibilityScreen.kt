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
fun VisibilityScreen(
    viewModel: BypassViewModel,
    textScale: Float,
    onNarrate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Collect States from ViewModel
    val selectedAssistanceTypes by viewModel.selectedAssistanceTypes.collectAsState()
    val managerBookingStatus by viewModel.managerBookingStatus.collectAsState()
    val glassesRentalStatus by viewModel.glassesRentalStatus.collectAsState()
    val requirementsText by viewModel.requirementsText.collectAsState()

    // Screen-local popup indicators
    var showManagerCalendarPopup by remember { mutableStateOf(false) }
    var showGlassesSchedulePopup by remember { mutableStateOf(false) }
    var chosenTimeSlot by remember { mutableStateOf("") }
    var chosenManagerDate by remember { mutableStateOf("") }

    val supportOptions = listOf(
        "휠체어 동행 지원",
        "안내견 동반 안심 가이드",
        "수어 통역 서포트",
        "자막 안경 기기 매핑 브리핑"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. TITLE PANEL ---
        Column {
            BypassText(
                text = "MATCHING & RESERVATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                textScale = textScale
            )
            BypassText(
                text = "배리어프리 매칭 및 장비 예약",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                textScale = textScale
            )
        }

        // --- 2. ACCESSIBILITY MANAGER INADVANCE BOOKING (MANDATORY REQUIREMENT) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateCard),
            border = BorderStroke(1.dp, SoftGray),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BypassText(
                    text = "🧑‍🤝‍🧑 1:1 현장 동행 접근성 매니저 배정",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale,
                    voiceNarrationText = "관람에 필요한 리소스를 사전에 확인하세요. 공연장 환경과 동행 요구 사항을 매핑하는 예약 도구입니다."
                )

                BypassText(
                    text = "“관람에 필요한 리소스를 사전에 확인하세요” \n예매 전 공연장 환경 및 상세 기밀 요구 사항을 전송하면 대학로 통합 배리어프리가 엄선한 1대1 매니저 일정을 매칭하여 안심 관경을 돕습니다.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textScale = textScale
                )

                Divider(color = SoftGray, thickness = 0.8.dp)

                // Subtitle: Pre-select support types
                BypassText(
                    text = "필요한 지원 유형 사전 선택 (중복 가능)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale
                )

                // Column of Interactive Checkboxes with rounded design
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    supportOptions.forEach { option ->
                        val isChecked = selectedAssistanceTypes.contains(option)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isChecked) NeonBlue.copy(alpha = 0.08f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    BorderStroke(
                                        width = 1.dp,
                                        color = if (isChecked) NeonBlue else SoftGray
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.toggleAssistanceType(option) }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { viewModel.toggleAssistanceType(option) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = NeonBlue,
                                    uncheckedColor = TextSecondary,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BypassText(
                                text = option,
                                fontSize = 13.sp,
                                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimary,
                                textScale = textScale
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Custom Input Field for requirements
                BypassText(
                    text = "상세 요구 사양 및 휠체어 등급 기재",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale
                )

                OutlinedTextField(
                    value = requirementsText,
                    onValueChange = { viewModel.setRequirements(it) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        unfocusedBorderColor = SoftGray,
                        focusedLabelColor = NeonBlue
                    ),
                    placeholder = {
                        BypassText(
                            text = "예: 전동 휠체어 사용하며 턱 높낮이 유의 요망, 혹은 극장 진입로 엘리베이터 주행 매니저 필수",
                            fontSize = 11.sp,
                            color = SoftGray,
                            textScale = textScale
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // INTEGRATED MANAGER SCHEDULE VIEW & BOOKING BUTTON
                Button(
                    onClick = { showManagerCalendarPopup = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (managerBookingStatus == "CONFIRMED") SafeGreen else NeonBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (managerBookingStatus == "CONFIRMED") Icons.Default.CheckCircle else Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = if (managerBookingStatus == "CONFIRMED") "매니저 사전 예약 완료 (변경하기)" else "통합 매니저 일정 화면 및 예약",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }

                // Booking success banner disclosures
                managerBookingStatus?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SafeGreen.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, SafeGreen), shape = RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            BypassText(
                                text = "✅ 1:1 동행 매니저 매칭 확정",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafeGreen,
                                textScale = textScale
                            )
                            BypassText(
                                text = "매칭 일정: 2026.06.01 [예정극 관람 30분 전 원외 대기부스 만남]\n배정 매니저: 최수민 (공연 접근성 자격 취득 2년차 베테랑)\n요구 사양 전송 완료: \"$requirementsText\"",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                textScale = textScale
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "예약 취소하기",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = HazardRed,
                                modifier = Modifier
                                    .clickable { viewModel.cancelManagerBooking() }
                                    .align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        // --- 3. AI SUBTITLE GLASSES RENTAL SECTION (MANDATORY REQUIREMENT) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateCard),
            border = BorderStroke(1.dp, SoftGray),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BypassText(
                    text = "👓 AI AR 스마트 자막 안경 현장 대여",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale
                )

                // Required Exact Slogan Label
                BypassText(
                    text = "“공연에서 시간을 더 잘 알고 싶다면 AI AR 삼각으로 실시간 자막을 저장받으세요”",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    textScale = textScale
                )

                BypassText(
                    text = "수집된 데이터에 의해 실시간 대사 및 화자 가이드를 실물 스마트 고글에 연출해 장벽을 파쇄합니다.\n* 혜택 안내: 자막 안경 현장 대여 후 피드백 및 만족도 성실 제출 시, 다음 공연에서 사용가능한 자막 기기 수수료 적립 포인트 혜택을 환원해드립니다.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textScale = textScale
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlateDark, shape = RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.IntegrationInstructions, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            BypassText(text = "연계 서비스 제공소", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan, textScale = textScale)
                        }
                        BypassText(
                            text = "충무아트센터 자막 안경 배포창구 100% 실시간 연동\n안개 도면 지원 및 보정 필터 내장형 증강 모델",
                            fontSize = 12.sp,
                            color = TextPrimary,
                            textScale = textScale
                        )
                    }
                }

                // TIME SLOT QUERY BUTTON
                Button(
                    onClick = { showGlassesSchedulePopup = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (glassesRentalStatus == "RENTED") SafeGreen else NeonBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (glassesRentalStatus == "RENTED") Icons.Default.Check else Icons.Default.QueryBuilder,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = if (glassesRentalStatus == "RENTED") "자막안경 임시 배정 완료" else "기기 대여 가능 시간 조회",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }

                AnimatedVisibility(
                    visible = glassesRentalStatus == "RENTED",
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SafeGreen.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, SafeGreen), shape = RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                BypassText(text = "👓 자막 기기 대여 코드 확정", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SafeGreen, textScale = textScale)
                                BypassText(text = "대여 세션: 18:30 대여 | 수수료 포인트 지급 대상 등록", fontSize = 11.sp, color = TextPrimary, textScale = textScale)
                            }
                            Text(
                                text = "취소하기",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = HazardRed,
                                modifier = Modifier.clickable { viewModel.cancelGlassesRental() }
                            )
                        }
                    }
                }
            }
        }

        // --- Simulated Overlay Dialog 1: Manager Schedule Map ---
        if (showManagerCalendarPopup) {
            AlertDialog(
                onDismissRequest = { showManagerCalendarPopup = false },
                confirmButton = {
                    TextButton(
                        onClick = { 
                            if (chosenManagerDate.isNotEmpty()) {
                                viewModel.bookManager()
                                showManagerCalendarPopup = false
                            }
                        }
                    ) {
                        BypassText(text = "예약완료", color = NeonBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showManagerCalendarPopup = false }) {
                        BypassText(text = "닫기", color = TextSecondary, fontSize = 14.sp, textScale = textScale)
                    }
                },
                title = {
                    BypassText(text = "📅 통합 매니저 배정 일정 지도", fontSize = 16.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BypassText(text = "6월 1일 특별 연극 공연장(충무아트센터)에 배정 가능한 전문 동행 매니저 리스트입니다. 배정할 시간을 클릭하세요.", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                        Divider(color = SoftGray)
                        
                        val slots = listOf("18:00 시작 세션 (잔여 매니저 2명)", "19:00 시작 세션 (잔여 매니저 1명)", "19:30 시작 세션 (예약 마감)")
                        slots.forEach { slot ->
                            val isChosen = chosenManagerDate == slot
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isChosen) NeonBlue else SlateDark,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        BorderStroke(1.dp, if (isChosen) NeonBlue else SoftGray),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { chosenManagerDate = slot }
                                    .padding(12.dp)
                            ) {
                                BypassText(
                                    text = slot, 
                                    fontSize = 12.sp, 
                                    fontWeight = FontWeight.Bold, 
                                    color = if (isChosen) Color.White else TextPrimary, 
                                    textScale = textScale
                                )
                            }
                        }
                    }
                },
                containerColor = SlateCardHeader
            )
        }

        // --- Simulated Overlay Dialog 2: Subtitle Glasses Available Times ---
        if (showGlassesSchedulePopup) {
            AlertDialog(
                onDismissRequest = { showGlassesSchedulePopup = false },
                confirmButton = {
                    TextButton(
                        onClick = { 
                            if (chosenTimeSlot.isNotEmpty()) {
                                viewModel.rereserveGlasses()
                                showGlassesSchedulePopup = false
                            }
                        }
                    ) {
                        BypassText(text = "대여 예약 등록", color = NeonBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showGlassesSchedulePopup = false }) {
                        BypassText(text = "닫기", color = TextSecondary, fontSize = 14.sp, textScale = textScale)
                    }
                },
                title = {
                    BypassText(text = "👓 충무아트센터 스마트 기기 가능 점검", fontSize = 16.sp, fontWeight = FontWeight.Bold, textScale = textScale)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BypassText(text = "6월 1일 자막 안경 수량 여분: 총 12대. 대여 수령 예정 가상 시각 세션을 정해주시기 바랍니다.", fontSize = 12.sp, color = TextSecondary, textScale = textScale)
                        Divider(color = SoftGray)
                        
                        val slots = listOf("18:30 (수령 여분 여유)", "19:00 (마감 임박)", "19:30 (마감)")
                        slots.forEach { slot ->
                            val isChosen = chosenTimeSlot == slot
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isChosen) NeonBlue else SlateDark,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        BorderStroke(1.dp, if (isChosen) NeonBlue else SoftGray),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { chosenTimeSlot = slot }
                                    .padding(12.dp)
                            ) {
                                BypassText(
                                    text = slot, 
                                    fontSize = 12.sp, 
                                    fontWeight = FontWeight.Bold, 
                                    color = if (isChosen) Color.White else TextPrimary, 
                                    textScale = textScale
                                )
                            }
                        }
                    }
                },
                containerColor = SlateCardHeader
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
