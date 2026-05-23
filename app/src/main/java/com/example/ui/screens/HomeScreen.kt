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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BypassText
import com.example.ui.components.SectionTitle
import com.example.ui.components.BypassButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.BypassViewModel

// Sample Play/Performance Data Model representing AI recommendations
data class Performance(
    val title: String,
    val genre: String,
    val rank: Int,
    val serviceCount: Int, // 서비스 호수(회)
    val tags: List<String>,
    val dateRange: String,
    val theater: String
)

@Composable
fun HomeScreen(
    viewModel: BypassViewModel,
    textScale: Float,
    onNavigate: () -> Unit = {},
    onNarrate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Collect variables from ViewModel
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val activeQuickFilters by viewModel.activeQuickFilters.collectAsState()
    val supportersApplied by viewModel.supportersApplied.collectAsState()

    // 1. Static list of custom sample performances
    val originalPerformances = remember {
        listOf(
            Performance("배리어프리 빨래", "뮤지컬", 1, 15, listOf("휠체어 접근", "자막 제공", "음성 해설", "수어 통역"), "상시 공연", "동양예술극장"),
            Performance("헤드윅 투어 서울", "뮤지컬", 2, 12, listOf("휠체어 접근", "자막 제공"), "2026.06.10 - 08.15", "충무아트센터 대극장"),
            Performance("조씨고아, 복수의 씨앗", "연극", 3, 10, listOf("음성 해설", "수어 통역"), "2026.06.01 - 06.25", "국립극단 달오름극단"),
            Performance("조수미 보편적 갈라", "클래식", 4, 8, listOf("휠체어 접근", "음성 해설"), "2026.07.02 단 하루", "서울예술의전당"),
            Performance("악뮤 유니버설 힐링콘", "콘서트", 5, 6, listOf("자막 제공", "수어 통역"), "2026.06.20 - 06.22", "올림픽공원 체조경기장"),
            Performance("어둠 속의 목소리", "연극", 6, 4, listOf("자막 제공", "음성 해설"), "2026.06.15 - 07.10", "대학로 403 소극장"),
            Performance("비발디 사계 무장벽 세션", "클래식", 7, 3, listOf("휠체어 접근"), "2026.08.11", "금호아트홀 연세")
        )
    }

    // Interactive filter computations
    val filteredPerformances = remember(selectedGenre, activeQuickFilters) {
        originalPerformances.filter { perf ->
            // Genre Filter
            val genreMatch = selectedGenre == "전체" || perf.genre == selectedGenre
            
            // Quick Access Filters: All activated filters must be supported
            val quickFiltersMatch = activeQuickFilters.isEmpty() || activeQuickFilters.all { filter ->
                perf.tags.contains(filter)
            }
            
            genreMatch && quickFiltersMatch
        }.sortedBy { it.rank }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. TOP HEADER BRANDING ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                BypassText(
                    text = "UNIVERSAL STANDARD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue, // Royal Blue (#0066FF)
                    textScale = textScale,
                    style = TextStyle(letterSpacing = 2.sp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BypassText(
                        text = "403:",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        textScale = textScale
                    )
                    BypassText(
                        text = "BYPASS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricCyan, // Bright Cyan (#00D1FF)
                        textScale = textScale
                    )
                }
            }
            
            // Interactive quick narrator indicator
            IconButton(
                onClick = { 
                    viewModel.triggerTtsAnnouncement("403 바이패스 홈 안내 화면입니다. 선호 장르와 접근성 도구를 선택하면 맞춤 공연 랭킹이 제안됩니다.") 
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(BorderStroke(1.dp, SoftGray), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "어플리케이션 정보 해설",
                    tint = NeonBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // --- 2. OFFICIAL SUPPORTERS RECRIUTMENT BANNER (MANDATORY REQUIREMENT) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(SlateCardHeader, Color(0xFF0F253F))), 
                    shape = RoundedCornerShape(24.dp)
                )
                .border(BorderStroke(1.2.dp, NeonBlue.copy(alpha = 0.4f)), shape = RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BypassText(
                        text = "🔥 OFFICIAL SUPPORTERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        textScale = textScale,
                        style = TextStyle(letterSpacing = 1.sp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .background(NeonBlue.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                            .border(BorderStroke(0.8.dp, NeonBlue), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        BypassText(
                            text = "리워드 지급",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonBlue,
                            textScale = textScale
                        )
                    }
                }

                BypassText(
                    text = "403 서포터즈 1기 대모집!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textScale = textScale
                )

                BypassText(
                    text = "장벽 없는 관람 권리 확산을 위해 활동하고 '근접성 어바이터' 리워드 전용 뱃지 및 인센티브 혜택을 획득하세요.",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8), // slate-400
                    textScale = textScale
                )

                // Registration Action Button
                Button(
                    onClick = { viewModel.applySupporters() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (supportersApplied) SafeGreen else NeonBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    enabled = !supportersApplied
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (supportersApplied) Icons.Default.CheckCircle else Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BypassText(
                            text = if (supportersApplied) "지원 완료 (어바이터 뱃지 발급됨)" else "지원하기 바로가기",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textScale = textScale
                        )
                    }
                }
            }
        }

        // --- 3. ACCESSIBILITY QUICK FILTER ICONS (MANDATORY REQUIREMENT) ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BypassText(
                text = "⚡ 초간편 무장벽 공연 맞춤 필터",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textScale = textScale
            )

            // Horizontal Grid-like configuration of 4 access criteria
            val filtersList = listOf(
                Pair("휠체어 접근", Icons.Default.Accessibility),
                Pair("자막 제공", Icons.Default.ClosedCaption),
                Pair("음성 해설", Icons.Default.VolumeUp),
                Pair("수어 통역", Icons.Default.InterpreterMode)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filtersList.forEach { (filterName, iconVec) ->
                    val isActive = activeQuickFilters.contains(filterName)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isActive) NeonBlue else SlateCard,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                BorderStroke(
                                    width = 1.2.dp,
                                    color = if (isActive) NeonBlue else SoftGray
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                viewModel.toggleQuickFilter(filterName)
                            }
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = iconVec,
                                contentDescription = "$filterName 검색 조율기",
                                tint = if (isActive) Color.White else TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BypassText(
                                text = filterName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) Color.White else TextSecondary,
                                textScale = textScale,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // --- 4. GENRE FILTER TABS (MANDATORY REQUIREMENT) ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BypassText(
                text = "🎭 장르 필터",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textScale = textScale
            )

            val genres = listOf("전체", "뮤지컬", "연극", "콘서트", "클래식")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SlateCard, shape = RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, SoftGray), shape = RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                genres.forEach { genre ->
                    val isSelected = selectedGenre == genre
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) NeonBlue else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.setGenre(genre) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BypassText(
                            text = genre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else TextSecondary,
                            textScale = textScale
                        )
                    }
                }
            }
        }

        // --- 5. AI CUSTOM PLAY RECOMMENDATION LIST (MANDATORY REQUIREMENT) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                BypassText(
                    text = "나를 위한 맞춤 추천 공연",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    textScale = textScale
                )
                BypassText(
                    text = "선호 장르 및 무대 서비스 호수(회) 기반 권장 랭킹",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textScale = textScale
                )
            }
            
            BypassText(
                text = "총 ${filteredPerformances.size}건",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                textScale = textScale
            )
        }

        if (filteredPerformances.isEmpty()) {
            // Friendly Empty State
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, SoftGray)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "공연 검색 결과 없음",
                        tint = TextSecondary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BypassText(
                        text = "해당 접근성 편의 조건에 맞는 공연 데이터가 존재하지 않습니다.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textScale = textScale,
                        textAlign = TextAlign.Center
                    )
                    BypassText(
                        text = "상단의 퀵 필터 또는 장르 탭을 다르게 변경하여 더 넓은 범위의 무장벽 극장을 경험해보세요.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        textScale = textScale,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Render the items beautifully
            filteredPerformances.forEach { play ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNarrate("${play.title} 공연은 ${play.genre} 장르이며 실시간 서비스 ${play.serviceCount}회 지원 랭크 기록을 보유하고 있습니다.")
                        },
                    colors = CardDefaults.cardColors(containerColor = SlateCard),
                    border = BorderStroke(1.dp, SoftGray),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Display Rank indicator box with high visual contrast
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    if (play.rank == 1) NeonBlue else SlateDark,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(BorderStroke(1.dp, SoftGray), shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            BypassText(
                                text = "${play.rank}위",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (play.rank == 1) Color.White else TextPrimary,
                                textScale = textScale
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(ElectricCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    BypassText(
                                        text = play.genre,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricCyan,
                                        textScale = textScale
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                BypassText(
                                    text = "서비스 누적 ${play.serviceCount}회",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafeGreen,
                                    textScale = textScale
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            BypassText(
                                text = play.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textScale = textScale
                            )

                            BypassText(
                                text = "📍 ${play.theater} | ${play.dateRange}",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textScale = textScale
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Highlight Supported Accessibility Tags
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                play.tags.forEach { tagName ->
                                    Box(
                                        modifier = Modifier
                                            .background(SlateDark, shape = RoundedCornerShape(8.dp))
                                            .border(BorderStroke(0.6.dp, SoftGray), shape = RoundedCornerShape(8.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        BypassText(
                                            text = tagName,
                                            fontSize = 9.sp,
                                            color = TextSecondary,
                                            textScale = textScale
                                        )
                                    }
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "상세조회",
                            tint = SoftGray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Just quick spacer to clear floating button
        Spacer(modifier = Modifier.height(10.dp))
    }
}
