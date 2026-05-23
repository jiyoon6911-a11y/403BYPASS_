package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BypassTab {
    HOME,       // 홈 (Home)
    MOBILITY,   // 안내맵 (Live Map Guidance)
    VISIBILITY, // 매칭예약 (Match & Reserve)
    FLOW_SAFETY,// 나의티켓 (Tickets & Calendar)
    ECOSYSTEM   // 마이페이지 (My Profile & History)
}

class BypassViewModel : ViewModel() {

    // Bottom Navigation tab
    private val _currentTab = MutableStateFlow(BypassTab.HOME)
    val currentTab: StateFlow<BypassTab> = _currentTab.asStateFlow()

    // --- Dynamic Universal Accessibility States ---
    private val _textScale = MutableStateFlow(1.2f) // Default readable text size
    val textScale: StateFlow<Float> = _textScale.asStateFlow()

    private val _highContrastMode = MutableStateFlow(false) // Pure Pitch Black + Neon blue/white shapes
    val highContrastMode: StateFlow<Boolean> = _highContrastMode.asStateFlow()

    private val _ttsSimulatedActive = MutableStateFlow(false) // Toggle simulated in-app screen reader voices
    val ttsSimulatedActive: StateFlow<Boolean> = _ttsSimulatedActive.asStateFlow()

    private val _ttsNarrativeLine = MutableStateFlow("403 BYPASS 유니버설 안내 및 탐색 센터에 오신 것을 환영합니다.") // Audio reader console text
    val ttsNarrativeLine: StateFlow<String> = _ttsNarrativeLine.asStateFlow()

    // --- Home Screen States ---
    private val _selectedGenre = MutableStateFlow("전체")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _activeQuickFilters = MutableStateFlow(setOf<String>())
    val activeQuickFilters: StateFlow<Set<String>> = _activeQuickFilters.asStateFlow()

    private val _supportersApplied = MutableStateFlow(false)
    val supportersApplied: StateFlow<Boolean> = _supportersApplied.asStateFlow()

    // --- Guidance/Map Screen States ---
    private val _selectedTheaterId = MutableStateFlow("theater_403")
    val selectedTheaterId: StateFlow<String> = _selectedTheaterId.asStateFlow()

    private val _arSimulatorActive = MutableStateFlow(false)
    val arSimulatorActive: StateFlow<Boolean> = _arSimulatorActive.asStateFlow()

    private val _threeDViewActive = MutableStateFlow(false)
    val threeDViewActive: StateFlow<Boolean> = _threeDViewActive.asStateFlow()

    private val _subwayElevatorOutOfOrder = MutableStateFlow(true) // Simulate Hyehwa station elevator fault
    val subwayElevatorOutOfOrder: StateFlow<Boolean> = _subwayElevatorOutOfOrder.asStateFlow()

    // --- Match & Book Screen States ---
    private val _accEscortPref = MutableStateFlow(true) // Escort needed
    private val _selectedAssistanceTypes = MutableStateFlow(setOf("휠체어 동행 지원"))
    val selectedAssistanceTypes: StateFlow<Set<String>> = _selectedAssistanceTypes.asStateFlow()

    private val _managerBookingStatus = MutableStateFlow<String?>(null) // null, "PENDING", "CONFIRMED"
    val managerBookingStatus: StateFlow<String?> = _managerBookingStatus.asStateFlow()

    private val _glassesRentalStatus = MutableStateFlow<String?>(null) // null, "RENTED"
    val glassesRentalStatus: StateFlow<String?> = _glassesRentalStatus.asStateFlow()

    private val _requirementsText = MutableStateFlow("")
    val requirementsText: StateFlow<String> = _requirementsText.asStateFlow()

    // --- Tickets & Calendar Screen States ---
    private val _ticketTabIndex = MutableStateFlow(0) // 0: 예매완료 탭, 1: 지난 관람일 탭
    val ticketTabIndex: StateFlow<Int> = _ticketTabIndex.asStateFlow()

    // --- Profile & History Screen States ---
    private val _userNickname = MutableStateFlow("장벽없는예술가")
    val userNickname: StateFlow<String> = _userNickname.asStateFlow()

    private val _historyReviews = MutableStateFlow<List<String>>(emptyList()) // Empty initial state as requested
    val historyReviews: StateFlow<List<String>> = _historyReviews.asStateFlow()

    init {
        // Observe tickets and synchronize initial paths
        triggerTtsAnnouncement("403 바이패스가 초기화되었습니다. 안전한 관람 여정을 준비합니다.")
    }

    // --- General Tab Navigation Actions ---
    fun setTab(tab: BypassTab) {
        _currentTab.value = tab
        val message = when (tab) {
            BypassTab.HOME -> "홈 화면. 선호 장르별 AI 맞춤 공연 추천과 서포터즈 정보를 확인해보세요."
            BypassTab.MOBILITY -> "안내맵 화면. 실시간 층별 혼잡도 및 AR 카메라 길안내, 에스맵 3D 도면, 지하철 엘리베이터 고장 현황을 조회합니다."
            BypassTab.VISIBILITY -> "매칭 및 예약 화면. 1대1 현장 보편적 접근성 매니저 배정과 실시간 자막 전용 안경 대기열 예약을 신청해보세요."
            BypassTab.FLOW_SAFETY -> "나의 티켓 화면. 예정된 관람 일정 캘린더와 상세 모바일 입장권, 티켓 취소 환불 규정 및 공연 팁 안내 구역입니다."
            BypassTab.ECOSYSTEM -> "마이페이지 화면. 나만의 배리어프리 프로필 상태와 개인적인 관람 히스토리 평가 기록을 관리합니다."
        }
        triggerTtsAnnouncement(message)
    }

    fun updateTextScale(scale: Float) {
        _textScale.value = scale
        triggerTtsAnnouncement("텍스트 크기가 ${String.format("%.1f", scale)}배로 변경되었습니다.")
    }

    fun toggleHighContrast() {
        _highContrastMode.update { !it }
        val state = if (_highContrastMode.value) "켜짐" else "꺼짐"
        triggerTtsAnnouncement("접근성 고대비 모드가 ${state}으로 설정되었습니다.")
    }

    fun toggleTtsSimulated() {
        _ttsSimulatedActive.update { !it }
        val state = if (_ttsSimulatedActive.value) "활성화" else "비활성화"
        triggerTtsAnnouncement("인앱 화면 자막 해설 음성안내가 ${state}되었습니다.")
    }

    fun triggerTtsAnnouncement(line: String) {
        _ttsNarrativeLine.value = line
    }

    // --- UI Dynamic Interactions ---
    fun setGenre(genre: String) {
        _selectedGenre.value = genre
        triggerTtsAnnouncement("공연 선호 장르 조건이 ${genre}로 반영되었습니다.")
    }

    fun toggleQuickFilter(filter: String) {
        _activeQuickFilters.update { current ->
            if (current.contains(filter)) current - filter else current + filter
        }
        triggerTtsAnnouncement("접근성 빠른 필터 [${filter}] 구성을 토글하였습니다.")
    }

    fun applySupporters() {
        _supportersApplied.value = true
        triggerTtsAnnouncement("축하합니다! 403 서포터즈 1기 지원서가 성공적으로 접수되었습니다. 근접성 어바이터 뱃지를 획득하셨습니다.")
    }

    fun toggleArSimulator() {
        _arSimulatorActive.update { !it }
        _threeDViewActive.value = false
        val state = if (_arSimulatorActive.value) "켜짐" else "꺼짐"
        triggerTtsAnnouncement("내 위치 기반 AI AR 카메라 안내 가이드가 ${state}되었습니다.")
    }

    fun toggleThreeDView() {
        _threeDViewActive.update { !it }
        _arSimulatorActive.value = false
        val state = if (_threeDViewActive.value) "설치완료" else "해제"
        triggerTtsAnnouncement("S-MAP 연동 3D 건물 도면 가상 검토기가 ${state}되었습니다.")
    }

    fun toggleSubwayElevator() {
        _subwayElevatorOutOfOrder.update { !it }
        val state = if (_subwayElevatorOutOfOrder.value) "안내중: 고장 복구 대기" else "정상 작동 중"
        triggerTtsAnnouncement("지하철 엘리베이터 상태 점검: 혜화역 4번출구 엘리베이터가 ${state}입니다.")
    }

    fun toggleAssistanceType(type: String) {
        _selectedAssistanceTypes.update { current ->
            if (current.contains(type)) current - type else current + type
        }
        triggerTtsAnnouncement("지원 리소스 [${type}]이 선택되었습니다.")
    }

    fun setRequirements(text: String) {
        _requirementsText.value = text
    }

    fun bookManager() {
        _managerBookingStatus.value = "CONFIRMED"
        triggerTtsAnnouncement("배리어프리 1:1 현장 동행 매니저 전용 예약 처리가 확정되었습니다.")
    }

    fun cancelManagerBooking() {
        _managerBookingStatus.value = null
        triggerTtsAnnouncement("매니저 예약 일정을 취소하였습니다.")
    }

    fun rereserveGlasses() {
        _glassesRentalStatus.value = "RENTED"
        triggerTtsAnnouncement("실시간 자막 스마트 자막안경 임시 수령 현장 대여 예약이 완료되었습니다.")
    }

    fun cancelGlassesRental() {
        _glassesRentalStatus.value = null
        triggerTtsAnnouncement("자막안경 수령 예약을 취소하였습니다.")
    }

    fun setTicketTab(index: Int) {
        _ticketTabIndex.value = index
        val name = if (index == 0) "예매완료 티켓" else "지난 관람일 내역"
        triggerTtsAnnouncement("${name} 내역을 보여줍니다.")
    }

    fun addPastReviewSimulation(playName: String, starRating: Int, text: String) {
        val review = "🎭 $playName (★ $starRating) - $text"
        _historyReviews.update { current -> listOf(review) + current }
        triggerTtsAnnouncement("지난 공연 평가 관람 후기 수집이 새로 등재되었습니다.")
    }

    fun clearHistory() {
        _historyReviews.value = emptyList()
        triggerTtsAnnouncement("관람 기록이 초기화되었습니다. 현재 히스토리가 존재하지 않습니다.")
    }
}
