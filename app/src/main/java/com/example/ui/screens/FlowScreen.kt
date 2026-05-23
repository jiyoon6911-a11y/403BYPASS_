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

@Composable
fun FlowScreen(
    viewModel: BypassViewModel,
    textScale: Float,
    onNarrate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Collect variables from ViewModel
    val currentTicketTab by viewModel.ticketTabIndex.collectAsState()

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
                text = "MY PERFORMANCE TICKETS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                textScale = textScale
            )
            BypassText(
                text = "나의 스마트 모바일 티켓",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                textScale = textScale
            )
        }

        // --- 2. TABS CONFIGURATION (MANDATORY REQUIREMENT) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlateCard, shape = RoundedCornerShape(24.dp))
                .border(BorderStroke(1.dp, SoftGray), shape = RoundedCornerShape(24.dp))
                .padding(4.dp)
        ) {
            val tabs = listOf("예매 완료 탭", "지난 관람일 탭")
            tabs.forEachIndexed { index, tabName ->
                val isSelected = currentTicketTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) NeonBlue else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.setTicketTab(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BypassText(
                        text = tabName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else TextSecondary,
                        textScale = textScale
                    )
                }
            }
        }

        if (currentTicketTab == 0) {
            // --- 3. UPCOMING PERFORMANCE FEATURED TICKETS ---
            BypassText(
                text = "🗓️ 다가오는 관람일 최우선 상단 노출",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textScale = textScale
            )

            // Primary Ticket Card: 특별 연극 (VR 시범운영)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNarrate("자채 데베스 원 고객님의 특별 연극 예매 티켓입니다. 좌석은 한강이었어요 십만명 이며 일정은 이천이십육년 유월 일일 오전 이십시정각 입니다.")
                    },
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                border = BorderStroke(1.5.dp, ElectricCyan), // Highlight upcoming with Cyan border
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header with custom tags
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(ElectricCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, ElectricCyan), shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.VideogameAsset, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                BypassText(
                                    text = "VR 시범운영",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan,
                                    textScale = textScale
                                )
                            }
                        }

                        // Status pill
                        Box(
                            modifier = Modifier
                                .background(SafeGreen, shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            BypassText(
                                text = "D-9",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                textScale = textScale
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title
                    BypassText(
                        text = "특별 연극 (VR 시범운영)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        textScale = textScale
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = SoftGray, thickness = 0.8.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Exactly Required User Data Fields
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            BypassText(text = "예매 일시", fontSize = 11.sp, color = TextSecondary, textScale = textScale)
                            BypassText(
                                text = "2026.06.01 오전 20:00",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textScale = textScale
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            BypassText(text = "예매번호", fontSize = 11.sp, color = TextSecondary, textScale = textScale)
                            BypassText(
                                text = "BP-20260601-D8",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textScale = textScale
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            BypassText(text = "배정 좌석", fontSize = 11.sp, color = TextSecondary, textScale = textScale)
                            BypassText(
                                text = "한강이었어요 10만명", // Exactly as requested
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NeonBlue,
                                textScale = textScale
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            BypassText(text = "예매자", fontSize = 11.sp, color = TextSecondary, textScale = textScale)
                            BypassText(
                                text = "자채 데베스 ①", // Exactly as requested
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textScale = textScale
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Call bar code
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SlateCardHeader, shape = RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.QrCode, contentDescription = "입장 확인 큐알코드 대기중", tint = Color.White, modifier = Modifier.size(54.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            BypassText(
                                text = "입장 전용 QR 인식 세션 대기중",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textScale = textScale
                            )
                        }
                    }
                }
            }

            // --- 4. THIS MONTH'S CALENDAR (MANDATORY REQUIREMENT) ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BypassText(
                    text = "📅 이번 달 일정 캘린더 (2026년 6월)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SlateCard),
                    border = BorderStroke(1.dp, SoftGray),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BypassText(
                            text = "JUNE 2026",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonBlue,
                            textScale = textScale
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Render Days of Week
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
                            daysOfWeek.forEach { day ->
                                BypassText(
                                    text = day,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (day == "일") HazardRed else TextSecondary,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    textScale = textScale
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Divider(color = SoftGray)
                        Spacer(modifier = Modifier.height(6.dp))

                        // Custom Grid Layout for June (Starts on Monday / 월)
                        var dayCounter = 1
                        val startOffset = 1 // Monday start
                        
                        for (row in 0..4) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (col in 0..6) {
                                    val cellIndex = row * 7 + col
                                    if (cellIndex < startOffset || dayCounter > 30) {
                                        // Empty cell
                                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                                    } else {
                                        val currentDay = dayCounter
                                        val hasPerformance = currentDay == 1 // June 1st performance highlighted
                                        
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(2.dp)
                                                .background(
                                                    if (hasPerformance) ElectricCyan else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .clickable {
                                                    if (hasPerformance) {
                                                        viewModel.triggerTtsAnnouncement("유월 일일은 특별 연극 관람 예정일입니다.")
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            BypassText(
                                                text = currentDay.toString(),
                                                fontSize = 12.sp,
                                                fontWeight = if (hasPerformance) FontWeight.Black else FontWeight.Bold,
                                                color = if (hasPerformance) Color.White else if (col == 0) HazardRed else TextPrimary,
                                                textScale = textScale
                                            )
                                        }
                                        dayCounter++
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Box(modifier = Modifier.size(10.dp).background(ElectricCyan, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            BypassText(text = "하늘색 표시: 배리어프리 특별 연극 관람일", fontSize = 10.sp, color = TextSecondary, textScale = textScale)
                        }
                    }
                }
            }

            // --- 5. COMMON TICKETING INFO & ENTRANTE ACTION TIPS (MANDATORY REQUIREMENT) ---
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
                        text = "📜 뻔한 공연 티켓팅 정보 및 정책",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textScale = textScale
                    )

                    BypassText(
                        text = "• 취소/환불 정책: 관람일 전일 17:00까지 전액 취소 환불이 보장됩니다. 당일 취소는 현행 법률 및 극단 정책에 의해 환불이 불가능합니다.\n• 휠체어 동반석: 휠체어 동반석 예약 추가는 티켓 오픈 당일 종합 접근성 유선 핫라인을 통해 복호 번호를 선 발부받으셔도 즉시 이용할 수 있습니다.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textScale = textScale
                    )

                    Divider(color = SoftGray)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TipsAndUpdates, contentDescription = null, tint = WarningYellow, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = "입장마감 전 필독 팁!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textScale = textScale
                        )
                    }

                    BypassText(
                        text = "공연 시작 20분 전까지 입장해주셔야 VR 장비 조립 및 1:1 매니저 배포 브리핑을 안전하게 마칠 수 있습니다. 지각 시 연출 환경 동기화 장애 예방을 위해 도중 입장이 일부 차단될 수 있는 점 양해바랍니다.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textScale = textScale
                    )
                }
            }

        } else {
            // Past Viewing tab lists
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
                    Icon(imageVector = Icons.Default.History, contentDescription = "지난 관람내역 없음", tint = SoftGray, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    BypassText(
                        text = "지난 3개월간 관람 이력이 존재하지 않습니다.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textScale = textScale,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
