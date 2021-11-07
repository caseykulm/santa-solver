package com.caseykulm.santasolver

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CspSantaSolverTest {
    private lateinit var testPeople: Set<Person>
    private val subj = CspSantaSolver()

    @BeforeTest
    fun init() {
        val testPeopleJsonStr: String = javaClass.getResource("/test_people.json")!!.readText()
        testPeople = Json.decodeFromString(testPeopleJsonStr)
    }

    @Test
    fun solveWithTestData() {
        println(testPeople)

        val actualSolutions = subj.solve(testPeople)

        printAllSolutions(actualSolutions)
        printRandomSolution(actualSolutions)
    }

    @Test
    fun solveWithRealData() {
        // Using a real_people.json file that won't be checked in, but if it does exist with real people
        // then this will output a randomly chosen solution.
        // If it needs to be regenerated, just copy the format of test_people.json
        val realPeopleJsonStr: String = javaClass.getResource("/real_people.json")!!.readText()
        val realPeople: Set<Person> = Json.decodeFromString(realPeopleJsonStr)

        val actualSolutions = subj.solve(realPeople)

        printRandomSolution(actualSolutions)
    }

    private fun printRandomSolution(actualSolutions: List<Solution>) {
        val randomSolutionIndex = Random.nextInt(until = actualSolutions.size)
        println("Randomly chosen solution is #$randomSolutionIndex")
        println("=================")
        println(actualSolutions[randomSolutionIndex].prettyAllGifterGifteeAnswers)
    }

    private fun printAllSolutions(actualSolutions: List<Solution>) {
        println("Total solution count: ${actualSolutions.size}\n")

        actualSolutions.forEachIndexed { index, solution ->
            println("Solution #$index")
            println("=================")
            println(solution.prettyAllGifterGifteeAnswers)
            println("\n")
        }
    }
}