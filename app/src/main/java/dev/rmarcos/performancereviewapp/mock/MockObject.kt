package dev.rmarcos.performancereviewapp.mock

import dev.rmarcos.performancereviewapp.model.*
import java.time.LocalDate
import java.util.UUID

interface MockObject {
    fun users(): List<User>
    fun getUser(usename: String) : User
    fun developmentPlans(): List<DevelopmentPlan>
    fun newDevelopmentPlan(plan: DevelopmentPlan)
    fun departments(): MutableList<Department>
    fun goals(): List<Goal>
    fun newGoal(goal: Goal)
    fun assessments(): List<Assessment>
}

val mockObject = object: MockObject {
    private val department1 = Department (
        id = 1,
        name = "IT",
        description = "Responsible to maintain the company-s IT infrastructure"
    )
    private val department2 = Department (
        id = 2,
        name = "HR",
        description = "Human Resources Department"
    )
    private val developmentPlan1 = DevelopmentPlan (
        id = 1,
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusMonths(6),
        department = department1
    )
    private val developmentPlan2 = DevelopmentPlan (
        id = 2,
        startDate = LocalDate.now().minusMonths(4),
        endDate = LocalDate.now().plusMonths(2),
        department = department2
    )
    private val goal1 = Goal (
        id = 1,
        title = "New Infrastructure",
        description = "Build a new infrastructure for the new installations",
        weight = 2,
        points = 80,
        state = GoalState.DONE,
        developmentPlan = developmentPlan1
    )
    private val goal2 = Goal (
        id = 2,
        title = "Software Upgrade",
        description = "Upgrade used softwares",
        weight = 1,
        points = 20,
        state = GoalState.DONE,
        developmentPlan = developmentPlan1
    )
    private val goal3 = Goal (
        id = 3,
        title = "Increase IT cew",
        description = "Due to the new installations, we'll need more IT experts",
        weight = 3,
        points = 75,
        state = GoalState.DONE,
        developmentPlan = developmentPlan2
    )
    private val assessment1 = Assessment (
        id = 1,
        developmentPlan = developmentPlan1,
        score = (goal1.weight * goal1.points) + (goal2.weight * goal2.points),
        date = LocalDate.now(),
        minimalSuccessScore = 150
    )
    private val assessment2 = Assessment (
        id = 2,
        developmentPlan = developmentPlan2,
        score = (goal2.weight * goal2.points),
        date = LocalDate.now(),
        minimalSuccessScore = 200
    )
    private val profile1 = Profile (
        id = 1,
        name = "Leopoldo Mandume",
        phone = "+244 9XX XXX XXX",
        altPhone = "+244 9XX XXX XXX",
        role = "Gestor de IT",
        department = department1
    )
    private val profile2 = Profile (
        id = 2,
        name = "Ngola Samuel",
        phone = "+244 9XX XXX XXX",
        altPhone = "+244 9XX XXX XXX",
        role = "Gestor de RH",
        department = department2
    )
    private val profile3 = Profile (
        id = 3,
        name = "Nvuwu Mbasfumu",
        phone = "+244 9XX XXX XXX",
        altPhone = "+244 9XX XXX XXX",
        role = "HTML backend Developer",
        department = department1
    )
    private val user1 = User (
        uuid = UUID.randomUUID().toString(),
        username = "lmandume",
        email = "user@domain.com",
        password = "12345",
        permission = Permission.MANAGER,
        profile = profile1
    )
    private val user2 = User (
        uuid = UUID.randomUUID().toString(),
        username = "nsamuel",
        email = "user@domain.com",
        password = "12345",
        permission = Permission.ADMIN,
        profile = profile2
    )
    private val user3 = User (
        uuid = UUID.randomUUID().toString(),
        username = "nfumu",
        email = "user@domain.com",
        password = "12345",
        permission = Permission.COLABORATOR,
        profile = profile3
    )
    val userList = mutableListOf(user1, user2, user3)
    val developmentPlanList = mutableListOf(developmentPlan1, developmentPlan2)
    val departmentsList = mutableListOf(department1, department2)
    val goalsList = mutableListOf(goal1, goal2, goal3)
    val assessmentList = mutableListOf(assessment1, assessment2)
    override fun users(): MutableList<User> = userList
    override fun getUser(username: String): User = userList.filter({
        it.username == username
    }).get(0)
    override fun developmentPlans(): MutableList<DevelopmentPlan> = developmentPlanList
    override fun newDevelopmentPlan(plan: DevelopmentPlan) {
        developmentPlans().add(plan)
    }
    override fun departments(): MutableList<Department> = departmentsList
    override fun goals(): MutableList<Goal> = goalsList
    override fun newGoal(goal: Goal) {
        goalsList.add(goal)
    }
    override fun assessments(): MutableList<Assessment> = assessmentList
}