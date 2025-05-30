package dev.rmarcos.performancereviewapp.model

import java.time.LocalDate

data class Assessment (
    val id: Long,
    val developmentPlan: DevelopmentPlan,
    val score: Int,
    val date: LocalDate,
    val minimalSuccessScore: Int,
)