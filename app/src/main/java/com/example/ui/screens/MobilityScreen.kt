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
import androidx.compose.ui.graphics.Brush
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
import kotlinx.coroutines.delay

@Composable
fun MobilityScreen(
    viewModel: BypassViewModel,
    textScale: Float,
    onNarrate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Collect variables from ViewModel
    val arActive by viewModel.arSimulatorActive.collectAsState()
    val threeDViewActive by viewModel.threeDViewActive.collectAsState()
    val elevatorOutOfOrder by viewModel.subwayElevatorOutOfOrder.collectAsState()

    // Local states
    var smartWaitingActive by remember { mutableStateOf(false) }
    var selected3DLevel by remember { mutableStateOf("1층") }
    var showTotaInfoPopup by remember { mutableStateOf(false) }

    // Pulse animation ticks for AR view
    var mockArStepsIndex by remember { mutableStateOf(0) }
    val simulatedArRoutes = listOf(
        "앞으로 15m 직진하세요 (장벽 없는 슬로프 주행로)",
        "우회전하여 승강기를 호출하세요 (2호기 우대 엘리베이터)",
        "2층 객석 A구역 입구에 도착했습니다. 안전 점검 완료!"
    )

    LaunchedEffect(arActive) {
        if (arActive) {
            while (true) {
                delay(3500)
                mockArStepsIndex = (mockArStepsIndex + 1) % simulatedArRoutes.size
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. TITLE & SUBTITLE ---
        Column {
            BypassText(
                text = "REAL-TIME MOBILITY MAP",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                textScale = textScale
            )
            BypassText(
                text = "안내맵 및 실시간 혼잡 동선",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                textScale = textScale
            )
        }

        // --- 2. REAL-TIME VENUE CONGESTION SECTION ---
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BypassText(
                        text = "🚨 현장 구역별 실시간 혼잡 현황",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textScale = textScale
                    )
                    
                    Box(
                        modifier = Modifier
                            .background(HazardRed.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        BypassText(
                            text = "LIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = HazardRed,
                            textScale = textScale
                        )
                    }
                }

                Divider(color = SoftGray, thickness = 0.8.dp)

                // 1F Wheelchair Restroom (Wait count: 4)
                CongestionRow(
                    location = "1층 휠체어 전용 화장실",
                    statusText = "잔여 (현재 대기 4명)",
                    isCongested = true,
                    textScale = textScale
                )

                // B1 Information Desk (Wait: 1)
                CongestionRow(
                    location = "B1층 주차장 안내 데스크",
                    statusText = "여유 (현재 대기 1명)",
                    isCongested = false,
                    textScale = textScale
                )

                // 2F Ticket Lobby (Normal)
                CongestionRow(
                    location = "2층 유니버설 객석 로비",
                    statusText = "보통 (대기 대기 없음)",
                    isCongested = false,
                    textScale = textScale
                )

                Spacer(modifier = Modifier.height(4.dp))

                // SMART WAITING Proposal Button
                Button(
                    onClick = { 
                        smartWaitingActive = !smartWaitingActive
                        val ttsMsg = if (smartWaitingActive) {
                            "스마트 웨이팅 추천 경로가 분석되었습니다. 현재 1층 화장실 혼잡 회피를 위해 2층 대기인원 없음 공간을 대체 이용하세요!"
                        } else {
                            "웨이팅 제안을 해제합니다."
                        }
                        viewModel.triggerTtsAnnouncement(ttsMsg)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (smartWaitingActive) SafeGreen else NeonBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.OfflineBolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = if (smartWaitingActive) "최적 우회 동선 적용 중" else "스마트 웨이팅 추천 제안 받기",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }

                // Smart waiting disclosure
                AnimatedVisibility(
                    visible = smartWaitingActive,
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
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            BypassText(
                                text = "💡 지능형 혼잡 회피 알림",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafeGreen,
                                textScale = textScale
                            )
                            BypassText(
                                text = "1층 휠체어 전용 화장실은 현재 대기 시간이 길어 복잡합니다. 엘리베이터를 타고 2층 화장실을 즉시 활용할 경우 대기 시간 '12분 단축'이 가능합니다.",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                textScale = textScale
                            )
                        }
                    }
                }
            }
        }

        // --- 3. AI AR PATH FINDING CAMERA SIMULATOR ---
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
                    text = "📸 실시간 카메라 기반 AI AR 길안내",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale,
                    voiceNarrationText = "카메라를 통해 안전한 진입로를 overlay해주는 AR 탐색 인터페이스입니다."
                )

                BypassText(
                    text = "내 위치를 보며 찾아가기 — 복잡한 2D 평면 도면 대신 실물 카메라 뷰파인더 위에 안전한 지상 단계별 단축길 표기",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textScale = textScale
                )

                // START/STOP BUTTON
                Button(
                    onClick = { viewModel.toggleArSimulator() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (arActive) HazardRed else NeonBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (arActive) Icons.Default.VideocamOff else Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = if (arActive) "AR 길안내 정지" else "AR 길안내 시작 (카메라 연동)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }

                // AR View Mockup Simulator Panel
                AnimatedVisibility(
                    visible = arActive,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black)
                            .border(BorderStroke(2.dp, NeonBlue), shape = RoundedCornerShape(16.dp))
                    ) {
                        // Blurred perspective representation
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(NeonBlue.copy(alpha = 0.4f), Color.Transparent),
                                        radius = 600f
                                    )
                                )
                        )

                        // Outer HUD indicators
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.Red, shape = CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    BypassText(text = "LIVE CAMERA", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold, textScale = textScale)
                                }
                                BypassText(text = "자동 보정 기능 ON", fontSize = 9.sp, color = ElectricCyan, textScale = textScale)
                            }

                            // Dynamic navigation message
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .background(Color(0xE6002244), shape = RoundedCornerShape(12.dp))
                                    .border(BorderStroke(1.dp, NeonBlue), shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.DirectionsWalk, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    BypassText(
                                        text = simulatedArRoutes[mockArStepsIndex],
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textScale = textScale
                                    )
                                }
                            }

                            BypassText(text = "충무아트센터 로비 복도 진입로 자동 인식 중", fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f), textScale = textScale)
                        }
                    }
                }
            }
        }

        // --- 4. S-MAP BUILDING 3D SIMULATOR SECTION ---
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
                    text = "🏢 S-MAP 연동 건물 3D 입체 투시도",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textScale = textScale
                )

                BypassText(
                    text = "“원하는 자리도 건물을 둘러보고, 층별 간판을 확인해보세요”",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textScale = textScale
                )

                // 3D VIEWER TRIGGER
                Button(
                    onClick = { viewModel.toggleThreeDView() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (threeDViewActive) SafeGreen else NeonBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = if (threeDViewActive) "3D 모델 닫기" else "건물 3D로 보기",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }

                // 3D View Mockup
                AnimatedVisibility(
                    visible = threeDViewActive,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SlateCardHeader, shape = RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BypassText(text = "S-MAP 3D 시뮬레이션", fontSize = 11.sp, color = ElectricCyan, fontWeight = FontWeight.Bold, textScale = textScale)
                            
                            // Floor selector
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("B1", "1층", "2층").forEach { lvl ->
                                    val isSelected = selected3DLevel == lvl
                                    Box(
                                        modifier = Modifier
                                            .background(if (isSelected) NeonBlue else SlateDark, shape = RoundedCornerShape(6.dp))
                                            .clickable { 
                                                selected3DLevel = lvl
                                                viewModel.triggerTtsAnnouncement("S-MAP 3D 건물의 ${lvl} 내부 도면을 로드하였습니다.")
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        BypassText(text = lvl, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextSecondary, textScale = textScale)
                                    }
                                }
                            }
                        }

                        // Rendering Area representing the mock building layouts
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color(0xFF070B19), shape = RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.dp, SoftGray), shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.ViewInAr, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                BypassText(
                                    text = "충무아트센터 $selected3DLevel 입체 도면 시연",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textScale = textScale
                                )
                                BypassText(
                                    text = when (selected3DLevel) {
                                        "B1" -> "주차 타워 로비 | 리프트 승강장 간판식 식별"
                                        "1층" -> "유니버설 메인 종합 매표소 및 BF 경사판 탐험 안전선"
                                        else -> "대극장 로비 및 휠체어 고수 전용 뷰 관제석 마커 확인"
                                    },
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    textScale = textScale
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. TRANSIT COMMITTED SUBWAY ELEVATOR INDEX ---
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BypassText(
                        text = "🚇 또타지하철 교통 약자 교통로 연동",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textScale = textScale
                    )
                    
                    Box(
                        modifier = Modifier
                            .background(
                                if (elevatorOutOfOrder) HazardRed.copy(alpha = 0.15f) else SafeGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        BypassText(
                            text = if (elevatorOutOfOrder) "고장 알림" else "정상 구동",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (elevatorOutOfOrder) HazardRed else SafeGreen,
                            textScale = textScale
                        )
                    }
                }

                BypassText(
                    text = "“지하철 EV 고장 현황 실시간 안내” 교통공사 Open-API 연계 시범 동작중",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textScale = textScale
                )

                // Elevator Out of Order Alarm box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (elevatorOutOfOrder) HazardRed.copy(alpha = 0.08f) else SafeGreen.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            BorderStroke(1.dp, if (elevatorOutOfOrder) HazardRed else SafeGreen),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { viewModel.toggleSubwayElevator() }
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (elevatorOutOfOrder) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (elevatorOutOfOrder) HazardRed else SafeGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            BypassText(
                                text = "혜화역(4호선) 역사 내부 EV 점검",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textScale = textScale
                            )
                            BypassText(
                                text = if (elevatorOutOfOrder) {
                                    "⚠️ 혜화역 4번 출구 외벽 엘리베이터 승강기 수리 중 (우회로: 1번 출구 전용 휠체어 리프트를 이용바랍니다)"
                                } else {
                                    "✅ 실시간 혜화역 내 승강기 2개 기 모두 정상 작동 중입니다. 장애 없는 이동이 보장됩니다."
                                },
                                fontSize = 11.sp,
                                color = TextSecondary,
                                textScale = textScale
                            )
                        }
                    }
                }

                // Connect Button
                Button(
                    onClick = { showTotaInfoPopup = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SlateDark,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = "또타지하철 앱 연동 구동 및 세부 연도 확인",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textScale = textScale
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showTotaInfoPopup,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Snackbar(
                        action = {
                            TextButton(onClick = { showTotaInfoPopup = false }) {
                                BypassText(text = "확인", color = NeonBlue, fontSize = 12.sp, textScale = textScale)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = SlateCardHeader,
                        contentColor = Color.White
                    ) {
                        BypassText(
                            text = "서울교통공사 또타지하철 실시간 수리 점검 API와 관객 연수 경로가 가공 연동되었습니다.",
                            fontSize = 11.sp,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CongestionRow(
    location: String,
    statusText: String,
    isCongested: Boolean,
    textScale: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (isCongested) WarningYellow else SafeGreen,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            BypassText(
                text = location,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textScale = textScale
            )
        }

        BypassText(
            text = statusText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCongested) WarningYellow else SafeGreen,
            textScale = textScale
        )
    }
}
