package com.caseykulm.santasolver

import org.chocosolver.solver.Model
import org.chocosolver.solver.search.limits.TimeCounter
import org.chocosolver.solver.Solution as ChocoSolverSolution
import org.chocosolver.solver.variables.IntVar
import org.chocosolver.solver.variables.SetVar
import java.util.*
import java.util.concurrent.TimeUnit

class CspSantaSolver : SantaSolver {
    private val model = Model("Choco Solver CSP Santa Solver")
    private val personToIntVarMap: MutableMap<Person, IntVar> = mutableMapOf()
    private val personToModelIndexMap: MutableMap<Person, Int> = mutableMapOf()

    override fun solve(
        people: Set<Person>,
        gifterGifteeConstraints: Map<Person, Person>
    ): List<Solution> {
        val peopleSortedSet: SortedSet<Person> = people.toSortedSet { o1, o2 -> o1.name.compareTo(o2.name) }
        peopleSortedSet.forEachIndexed { index, person -> personToModelIndexMap[person] = index }
        val householdToPeopleIndexMap = peopleSortedSet.groupBy(
            keySelector = { it.householdName },
            valueTransform = { peopleSortedSet.indexOf(it) },
        )

        val uniqueNameCount = people.size

        val gifteeIntVars = mutableListOf<IntVar>()
        val householdToPeopleIndexSetVarMap = mutableMapOf<String, SetVar>()

        initChocoSolverVars(
            peopleSortedSet,
            gifteeIntVars,
            uniqueNameCount,
            householdToPeopleIndexMap,
            householdToPeopleIndexSetVarMap,
        )

        initChocoSolverConstraints(
            peopleSortedSet,
            gifteeIntVars,
            householdToPeopleIndexSetVarMap,
            gifterGifteeConstraints,
        )

        val chocoSolverSolutions = model.solver.findAllSolutions(
            TimeCounter(model, TimeUnit.MINUTES.toNanos(5)),
        )

        return chocoSolverSolutions.map {
            mapChocoSolverSolutionToDomainSolution(
                chocoSolverSolution = it,
                peopleSortedSet = peopleSortedSet,
                gifteeIntVars = gifteeIntVars,
            )
        }
    }

    private fun initChocoSolverConstraints(
        peopleSortedSet: SortedSet<Person>,
        gifteeIntVars: MutableList<IntVar>,
        householdToPeopleIndexSetVarMap: MutableMap<String, SetVar>,
        gifterGifteeConstraints: Map<Person, Person>,
    ) {
        peopleSortedSet.forEachIndexed { index, person ->
            // A person cannot be assigned to themselves
            // This constraint is actually redundant with the one below
            // model.arithm(gifteeIntVars[index], "!=", index).post()

            // A person cannot be assigned to someone in their household
            val householdPeopleSetVar: SetVar = householdToPeopleIndexSetVarMap[person.householdName]!!
            model.notMember(gifteeIntVars[index], householdPeopleSetVar).post()
        }

        // Every person can only be a giftee once
        model.allDifferent(*gifteeIntVars.toTypedArray()).post()

        gifterGifteeConstraints.forEach { (gifter, giftee) ->
            val gifterIntVar = personToIntVarMap[gifter]
            val gifteeModelIndex = personToModelIndexMap[giftee]
            println("Giving gifter: $gifter ($gifterIntVar), giftee: $giftee ($gifteeModelIndex)")
            model.arithm(gifterIntVar, "=", gifteeModelIndex!!).post()
        }

        // TODO: 2 people having each other can be boring. Probably make this a configuration option
    }

    private fun initChocoSolverVars(
        peopleSortedSet: SortedSet<Person>,
        gifteeIntVars: MutableList<IntVar>,
        uniqueNameCount: Int,
        householdToPeopleIndexMap: Map<String, List<Int>>,
        householdToPeopleIndexSetVarMap: MutableMap<String, SetVar>
    ) {
        assignEachPersonAGifteeIntVar(peopleSortedSet, gifteeIntVars, uniqueNameCount)
        assignEachPersonToAHouseholdSetVar(householdToPeopleIndexMap, householdToPeopleIndexSetVarMap)
    }

    private fun assignEachPersonToAHouseholdSetVar(
        householdToPeopleIndexMap: Map<String, List<Int>>,
        householdToPeopleIndexSetVarMap: MutableMap<String, SetVar>
    ) = householdToPeopleIndexMap.forEach { (householdName, peopleList) ->
        householdToPeopleIndexSetVarMap[householdName] = model.setVar(householdName, *peopleList.toIntArray())
    }

    private fun assignEachPersonAGifteeIntVar(
        peopleSortedSet: SortedSet<Person>,
        gifteeIntVars: MutableList<IntVar>,
        uniqueNameCount: Int
    ) = peopleSortedSet.forEach { person ->
        // Generate IntVar from Person
        val gifteeIntVar = model.intVar(person.name, 0, uniqueNameCount - 1)

        gifteeIntVars.add(gifteeIntVar)
        personToIntVarMap[person] = gifteeIntVar
    }

    private fun mapChocoSolverSolutionToDomainSolution(
        chocoSolverSolution: ChocoSolverSolution,
        peopleSortedSet: SortedSet<Person>,
        gifteeIntVars: List<IntVar>,
    ): Solution {
        return Solution(
            gifterToGifteeMap = peopleSortedSet.mapIndexed { index, person ->
                val gifteeIndex: Int = chocoSolverSolution.getIntVal(gifteeIntVars[index])
                val giftee: Person = peopleSortedSet.elementAt(gifteeIndex)
                Pair(person, giftee)
            }.toMap()
                .toSortedMap { o1, o2 -> o1.name.compareTo(o2.name) }
        )
    }
}