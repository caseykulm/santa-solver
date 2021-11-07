package com.caseykulm.santasolver

interface SantaSolver {
    fun solve(people: Set<Person>): List<Solution>
}