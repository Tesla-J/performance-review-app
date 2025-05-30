package dev.rmarcos.model

import java.time.LocalDate

data class DevelopmentPlan (
    val id: Long,
    // val manager: Profile - does not make sense here
    val startDate: LocalDate,
    val endDate: LocalDate,
    val department: Department,
)