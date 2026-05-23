package com.example.data

import androidx.compose.ui.graphics.vector.ImageVector

// Core Theater Representation
data class Theater(
    val id: String,
    val name: String,
    val totalScore: Double, // 100 max, Bypass Universal Accessibility Index
    val reviewCount: Int,
    val distanceMeters: Int,
    val hasElevator: Boolean,
    val hasWheelchairSeat: Boolean,
    val hasAccessibleRestroom: Boolean,
    val hasAudioVisualGuides: Boolean,
    val restroomRating: Double,  // 5.0 max
    val entranceRating: Double,  // 5.0 max
    val staffRating: Double,     // 5.0 max
    val audioRating: Double,     // 5.0 max
    val details: String,
    val photoUrl: String = ""
)

// Pathway Coordinates & Accessibility Milestones
data class RouteStep(
    val id: String,
    val title: String,
    val description: String,
    val isAccessible: Boolean,
    val warningMessage: String? = null,
    val hasTactilePavement: Boolean = true,
    val minPassableWidthCm: Int = 110
)

data class BypassRoute(
    val id: String,
    val destinationTheaterId: String,
    val startLocationName: String,
    val steps: List<RouteStep>,
    val totalDistanceMeters: Int,
    val estimatedMinutes: Int
)

// Interactive Seat Layout Specs & Aesthetic Pre-guides
data class SeatInfo(
    val id: String,
    val row: String,
    val number: Int,
    val type: SeatType,
    val obstacleClearancePct: Int, // Simulate visual blocks by front rows
    val comfortScore: Double,      // 5.0 max
    val visualGuideDesc: String,   // Eye-level description of stage
    val barrierFreeNotes: String   // Extra details for special requirements
)

enum class SeatType {
    REGULAR,
    WHEELCHAIR_SLOT, // Ground level space
    EASY_ENTRY,       // Aisle seat with flip-up armrest for physical aids
    LOW_VISION_PAD   // Equipped with low-vision contrast screen & audio pad
}

// Pre-show Sensory Stimuli Map (For visual-impaired, hearing-impaired, and neurodivergent/sensory-sensitive guests)
data class SensoryGuide(
    val acousticLevel: String,         // "원활", "보통", "자극적(소형 폭발음 포함)"
    val strobeFlickerLevel: String,    // "음소거", "보통", "주의 요망(플래시 스트로브)"
    val sensoryWarmings: List<String>, // Alerts for autism, strobe etc.
    val stageConceptSummary: String,  // Description of set decoration
    val characterOutfits: String,      // Describe actors' visual features
    val outlineNarrative: String       // Narrative touchstone
)

// Real-Time Crowd Crowdsourced Waiting Queue
data class VirtualTicket(
    val id: String,
    val facilityName: String,
    val queueNumber: Int,
    val currentPosition: Int,
    val minutesRemaining: Int,
    val reservationCode: String
)

// Crowdsourced Route Obstacles/Hazards submitted by users
data class HazardReport(
    val id: String,
    val type: HazardType,
    val hazardLocation: String,
    val description: String,
    val upvoteCount: Int,
    val reportedMinutesAgo: Int,
    val resolvedStatusSecured: Boolean = false
)

enum class HazardType {
    STEP_BARRIER,       // 턱/단차 발생 (휠체어 불가)
    DAMAGED_DOTS,        // 점자블록 훼손/조경물 가로막힘 (시각장애 우려)
    ELEVATOR_OUTAGE,     // 엘리베이터 임시 점검/정지
    CONGESTED_PASSAGE    // 인도 좁음/불법 주정차 적재물
}

// Universal Community Theater Reviews
data class TheaterReview(
    val id: String,
    val userName: String,
    val badgeType: AccessibilityBadge,
    val rating: Double,
    val content: String,
    val reportedAgoDays: Int
)

enum class AccessibilityBadge {
    WHEELCHAIR_USER, // 휠체어 이용자
    VISUALLY_IMPAIRED, // 시각인 도우미
    HEARING_IMPAIRED,  // 청각 수어지원
    GENERAL_SUPPORT    // 배리어프리 공감 서포터
}
