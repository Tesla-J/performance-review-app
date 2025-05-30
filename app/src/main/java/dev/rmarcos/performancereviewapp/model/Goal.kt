package dev.rmarcos.model

// TODO state can automatically change to
// doing after development plan starts
enum class GoalState {
    DONE,
    FAILED,
    PENDING,
}

data class Goal (
    val id: Long,
    val title: String,
    val description: String,
    val weight: Int,
    val points: Int,
    val state: GoalState,
    val developmentPlan: DevelopmentPlan,
)