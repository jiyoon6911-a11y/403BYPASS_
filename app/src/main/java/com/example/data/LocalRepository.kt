package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object LocalRepository {

    // Initial Daehakro Theaters List
    private val initialTheaters = listOf(
        Theater(
            id = "theater_403",
            name = "403 BYPASS 블루홀",
            totalScore = 96.5,
            reviewCount = 138,
            distanceMeters = 150,
            hasElevator = true,
            hasWheelchairSeat = true,
            hasAccessibleRestroom = true,
            hasAudioVisualGuides = true,
            restroomRating = 4.9,
            entranceRating = 4.8,
            staffRating = 5.0,
            audioRating = 4.9,
            details = "대학로 최초의 100% 배리어프리 유니버설 독립 전용관. 출입구 경사각 3도 이하, 무대 관람 구역 맞춤 높낮이 대가 구치되어 있으며, 점필 배리어프리와 서브우퍼 진동 시트가 탑재되었습니다."
        ),
        Theater(
            id = "theater_dream",
            name = "대학로 드림아트센터 (1관)",
            totalScore = 86.0,
            reviewCount = 42,
            distanceMeters = 340,
            hasElevator = true,
            hasWheelchairSeat = true,
            hasAccessibleRestroom = true,
            hasAudioVisualGuides = false,
            restroomRating = 4.2,
            entranceRating = 4.4,
            staffRating = 4.5,
            audioRating = 3.0,
            details = "주요 공공 접근성 요건을 충족하는 관영 연계 중형 극장. 엘리베이터 및 휠체어 전용 하차램프가 완비되어 있으나, 다인 극장의 혼잡 상황 시 시야 가이드가 파편화되어 제공됩니다."
        ),
        Theater(
            id = "theater_hanye",
            name = "한예 소극장",
            totalScore = 67.2,
            reviewCount = 19,
            distanceMeters = 480,
            hasElevator = false,
            hasWheelchairSeat = true,
            hasAccessibleRestroom = false,
            hasAudioVisualGuides = true,
            restroomRating = 2.1,
            entranceRating = 3.1,
            staffRating = 4.8,
            audioRating = 4.2,
            details = "휠체어 전용 가변석을 가동하고 있으나 입구 단차가 5cm 존재하여 간이 철제 램프를 호출해야 진입할 수 있으며, 전용 장애인 화장실이 미비하여 주변 인근 전철역 화장실을 이용해야 합니다."
        ),
        Theater(
            id = "theater_haesung",
            name = "대학로 소극장 혜성",
            totalScore = 32.4,
            reviewCount = 5,
            distanceMeters = 720,
            hasElevator = false,
            hasWheelchairSeat = false,
            hasAccessibleRestroom = false,
            hasAudioVisualGuides = false,
            restroomRating = 1.0,
            entranceRating = 1.5,
            staffRating = 3.5,
            audioRating = 1.0,
            details = "1997 편의증진법 이전 완공된 민간 소극장의 사각지대로, 3미터 지하형 원형 계단 구조라 휠체어 진입이 불가능합니다. 403 BYPASS 캠페인 우선 개선 대상 시설로 등록되어 있습니다."
        )
    )

    // Initial Path Routes starting from Hyehwa Station (혜화역)
    private val initialRoutes = mapOf(
        "theater_403" to BypassRoute(
            id = "route_403",
            destinationTheaterId = "theater_403",
            startLocationName = "혜화역 2번 출구 (엘리베이터 방향)",
            totalDistanceMeters = 150,
            estimatedMinutes = 4,
            steps = listOf(
                RouteStep(
                    id = "step1",
                    title = "혜화역 내부 엘리베이터 탑승",
                    description = "지하 2층 대합실에서 지상 1층 출입구로 수직 엘리베이터 연결. 음성 안내 유도기 가동 중.",
                    isAccessible = true,
                ),
                RouteStep(
                    id = "step2",
                    title = "점자블록 연속 횡단 통로",
                    description = "혜화역 앞 광장에서부터 보도블록 유도선 연속성 100% 보장. 균열 및 파손 구간 수선 완료.",
                    isAccessible = true,
                ),
                RouteStep(
                    id = "step3",
                    title = "403 BYPASS 진입 슬라이드",
                    description = "턱 없음(차이 0.5cm 이하). 폭 150cm의 완만한 슬로프 및 무동력 자동개폐식 입구 진입.",
                    isAccessible = true,
                )
            )
        ),
        "theater_dream" to BypassRoute(
            id = "route_dream",
            destinationTheaterId = "theater_dream",
            startLocationName = "혜화역 4번 출구 (대면 턱 개선 진입로)",
            totalDistanceMeters = 340,
            estimatedMinutes = 8,
            steps = listOf(
                RouteStep(
                    id = "step_dr1",
                    title = "4번 출구 좌측 연석 경사",
                    description = "원래 보차도 경계턱 15cm이 존재했으나 최근 '커브컷 프로젝트'로 경사 폭 2m 확보.",
                    isAccessible = true,
                ),
                RouteStep(
                    id = "step_dr2",
                    title = "마로니에 산책길 인도 통과",
                    description = "평안한 보도 구조이나, 주말 프리마켓 플리마켓 전개 시 혼잡 단차 및 점자 장애물 주의.",
                    isAccessible = true,
                    warningMessage = "주말 야외 좌판 설치 구역으로 시각 및 유모차 병목 발생 우려"
                ),
                RouteStep(
                    id = "step_dr3",
                    title = "기존 철골 계단 램프 우회로",
                    description = "정문 출입구는 계단 구조이므로 후면부 드림램프(화물 리프트 유도 기호) 보조 통로로 진입해야 함.",
                    isAccessible = true,
                    minPassableWidthCm = 105
                )
            )
        ),
        "theater_hanye" to BypassRoute(
            id = "route_hanye",
            destinationTheaterId = "theater_hanye",
            startLocationName = "혜화역 1번 출구 에스컬레이터로",
            totalDistanceMeters = 480,
            estimatedMinutes = 11,
            steps = listOf(
                RouteStep(
                    id = "step_hy1",
                    title = "1번 출구 급경사 인도",
                    description = "좁은 보도지형으로 배달 이륜차 인도 주정차 빈번하여 휠체어폭 위협.",
                    isAccessible = false,
                    warningMessage = "폭 90cm 수준 협소 구간 존재, 불법 적치물 확인 요망"
                ),
                RouteStep(
                    id = "step_hy2",
                    title = "골목길 아스팔트 균열단차",
                    description = "파손된 아스팔트로 불규칙한 요철 단차가 4cm 이상 발생하여 진동 주의.",
                    isAccessible = false,
                    warningMessage = "심한 균열 및 파편 돌멩이 많음"
                )
            )
        )
    )

    // Interactive Seat Specs configuration for "403 BYPASS" Show: "우리에게 열려있는 밤"
    val sampleSeats = List(15) { index ->
        val row = if (index < 5) "A" else if (index < 10) "B" else "C"
        val number = (index % 5) + 1
        val type = when {
            row == "A" && (number == 2 || number == 4) -> SeatType.WHEELCHAIR_SLOT
            row == "B" && number == 1 -> SeatType.EASY_ENTRY
            row == "C" && number == 5 -> SeatType.LOW_VISION_PAD
            else -> SeatType.REGULAR
        }
        val comfort = when (type) {
            SeatType.WHEELCHAIR_SLOT -> 5.0
            SeatType.EASY_ENTRY -> 4.7
            SeatType.LOW_VISION_PAD -> 4.8
            else -> 4.2
        }
        val clearance = if (row == "A") 100 else if (row == "B") 85 else 70
        val visual = when (row) {
            "A" -> "무대 전체가 안구 눈높이에 직사 배치되어 배우 눈동자 흐름까지 고해상 전안 시야에 들어옵니다. 스크린 자막 필요 시 가중 패드 바로 가용한 완벽한 시각 지점."
            "B" -> "앞사람에 약간의 정수리 간섭이 있으나 단차가 설계식 15cm 확보되어 등받이 가림 없이 무대를 통쾌하게 내려다볼 수 있는 각도."
            "C" -> "전반적 시야 안정도가 좋아 세트장 원경 감상에 적절. 단, 저시력 관객의 경우 C열 우측 패드 기기를 연동해 무대 스케일 클로즈업 서포터 활용 추천."
            else -> ""
        }
        val notes = when (type) {
            SeatType.WHEELCHAIR_SLOT -> "의자 없이 비어있는 평지 슬롯. 안전 고정 벨트와 바닥 전용 앵커 링이 결속선에 있습니다."
            SeatType.EASY_ENTRY -> "좌석 외측 다이얼 플립형 팔걸이 장착. 신체 지지용 손잡이를 딛고 복도에서 좌석으로 부드러운 평행 이동이 가능합니다."
            SeatType.LOW_VISION_PAD -> "점자 스티커 좌석 등받이 및 7인치 배리어프리 다크 스케일 스크린 대여 거치대 및 헤드폰 무선 잭 완비 구역."
            else -> "일반 쿠션형 시트. 폭 48cm, 깊이 45cm 규격 보장."
        }
        SeatInfo("seat_$index", row, number, type, clearance, comfort, visual, notes)
    }

    // Sensory details for the play "우리에게 열려있는 밤"
    val showSensoryGuide = SensoryGuide(
        acousticLevel = "약화 (돌발 소음 차단 댐퍼 가동)",
        strobeFlickerLevel = "안정 (플래시 조명 유도용 소프트 디퓨저 도입)",
        sensoryWarmings = listOf(
            "자연광 연계 조명으로 눈부심 유발 요인이 원천 차단되었습니다.",
            "3막의 긴박한 격돌 씬 중 드럼 타격 소리가 일시적으로 65dB까지 상승하나 3초 전 촉각 진동 시트로 사전 진동 알림이 울려 심박 안정을 보조합니다.",
            "강한 스트로브 플래시는 사용되지 않으며 점진적 일출 조도로 페이드 인 연출됩니다."
        ),
        stageConceptSummary = "세트는 원형 목조 카페 가구로, 출입구 경로는 관객 중심 좌우 구도이며, 바닥은 배우 및 스태프 이동 소음을 최소화하는 특수 고무 코팅 처리된 검은 슬라브 지형입니다.",
        characterOutfits = "주인공 한울(파란 긴 가죽 코트, 갈색 가방), 조력자 수혁(실버 자켓 및 베이지 하의), 이야기꾼 희원(은은한 회색 원피스 차림, 목에 노란 마이크 거치)",
        outlineNarrative = "세 사람이 높은 편의 장벽이 가로막힌 오래된 공연장을 허물고 모두를 위한 해방의 403 극장을 만들어가는 따스한 예술 연대의 이야기."
    )

    // Initial Crowdsourced Obstacle Reports
    private val initialHazards = listOf(
        HazardReport(
            id = "hz1",
            type = HazardType.STEP_BARRIER,
            hazardLocation = "혜화역 2번 출구 앞 주차장 연석 보도 차이",
            description = "점포 하차 트럭 차단으로 인도 통행 지장되어 휠체어가 우회해야 하는데, 연석 내릴 부분 단차 4.5cm 발생으로 임시 도움 필요.",
            upvoteCount = 24,
            reportedMinutesAgo = 12
        ),
        HazardReport(
            id = "hz2",
            type = HazardType.DAMAGED_DOTS,
            hazardLocation = "마로니에 공원 서측 화장실 유도 블록 뒤",
            description = "가로수 야외 전시물 스탠드가 노란 점자블록 중심선을 그대로 깔고 설치되어 있어 시각장애인 지팡이가 통째로 걸리는 사고 우려.",
            upvoteCount = 18,
            reportedMinutesAgo = 45
        ),
        HazardReport(
            id = "hz3",
            type = HazardType.ELEVATOR_OUTAGE,
            hazardLocation = "혜화역 승강설비 공공 1호 엘리베이터",
            description = "일요일 전원 정기 안전점검으로 당일 13:00~15:00 임시 정지. 휠체어 승객은 리프트 보조 요원 사전 승강장 대면 호출해야 함.",
            upvoteCount = 56,
            reportedMinutesAgo = 8
        )
    )

    // Initial Reviews
    private val initialReviews = listOf(
        TheaterReview("rv1", "민지우", AccessibilityBadge.WHEELCHAIR_USER, 4.9, "소극장 공연 보는 게 원래 극악의 난이도였는데, 403 BYPASS 블루홀은 완벽하게 혼자서 들어가서 관람할 수 있었습니다. 특히 경사가 부드럽고 가이드가 실물 동선이랑 정확히 맞아 울컥했네요.", 1),
        TheaterReview("rv2", "김수찬", AccessibilityBadge.VISUALLY_IMPAIRED, 4.8, "배우들의 옷차림과 무대 소품 배치를 공연 시작 전에 음성과 전용 촉지도로 읽고 가니 머릿속에서 구체적인 연극 장면이 환상적으로 복원되었습니다. 감동입니다.", 2),
        TheaterReview("rv3", "정혜인", AccessibilityBadge.GENERAL_SUPPORT, 5.0, "저는 휠체어를 안 쓰는 비장애인 관객이지만, 화장실 혼잡도 대기표 온라인 발급으로 대기줄도 줄고 공연장 전체 분위기가 배려를 당연한 권리로 대우해줘서 감격적인 공연 경험이었습니다. 403 BYPASS 최고!", 3)
    )

    // Observable States
    private val _theaters = MutableStateFlow(initialTheaters)
    val theaters: StateFlow<List<Theater>> = _theaters.asStateFlow()

    private val _hazards = MutableStateFlow(initialHazards)
    val hazards: StateFlow<List<HazardReport>> = _hazards.asStateFlow()

    private val _reviews = MutableStateFlow(initialReviews)
    val reviews: StateFlow<List<TheaterReview>> = _reviews.asStateFlow()

    private val _activeTickets = MutableStateFlow<List<VirtualTicket>>(emptyList())
    val activeTickets: StateFlow<List<VirtualTicket>> = _activeTickets.asStateFlow()

    fun getRoute(theaterId: String): BypassRoute? {
        return initialRoutes[theaterId] ?: initialRoutes["theater_403"]
    }

    // Interactive updates
    fun addReview(theaterId: String, name: String, badge: AccessibilityBadge, rating: Double, text: String) {
        val newReview = TheaterReview(
            id = "rv_${System.currentTimeMillis()}",
            userName = name,
            badgeType = badge,
            rating = rating,
            content = text,
            reportedAgoDays = 0
        )
        _reviews.update { listOf(newReview) + it }

        // Dynamically increment total review count & adjust score in memory
        _theaters.update { current ->
            current.map { th ->
                if (th.id == theaterId) {
                    val updatedCount = th.reviewCount + 1
                    val updatedScore = ((th.totalScore * th.reviewCount) + (rating * 20)) / updatedCount
                    th.copy(
                        reviewCount = updatedCount,
                        totalScore = kotlin.math.round(updatedScore * 10.0) / 10.0
                    )
                } else th
            }
        }
    }

    fun submitHazardReport(type: HazardType, location: String, desc: String) {
        val newHz = HazardReport(
            id = "hz_${System.currentTimeMillis()}",
            type = type,
            hazardLocation = location,
            description = desc,
            upvoteCount = 1,
            reportedMinutesAgo = 1
        )
        _hazards.update { listOf(newHz) + it }
    }

    fun upvoteHazard(id: String) {
        _hazards.update { items ->
            items.map {
                if (it.id == id) {
                    it.copy(upvoteCount = it.upvoteCount + 1)
                } else it
            }
        }
    }

    fun issueVirtualTicket(facility: String): VirtualTicket {
        val ticketNo = 100 + _activeTickets.value.size + 1
        val newTicket = VirtualTicket(
            id = "tkt_${System.currentTimeMillis()}",
            facilityName = facility,
            queueNumber = ticketNo,
            currentPosition = _activeTickets.value.size * 2 + 3,
            minutesRemaining = _activeTickets.value.size * 3 + 5,
            reservationCode = "BP-${(1000..9999).random()}"
        )
        _activeTickets.update { it + newTicket }
        return newTicket
    }

    fun cancelVirtualTicket(id: String) {
        _activeTickets.update { it.filterNot { t -> t.id == id } }
    }
}
