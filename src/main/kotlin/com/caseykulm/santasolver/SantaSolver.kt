package com.caseykulm.santasolver

interface SantaSolver {
    /**
     * By default, this will have 2 constraints:
     * 1. A gifter may not be assigned a giftee in the same household. This implicitly prevents a person from being
     *  assigned themselves as a giftee
     * 2. Each person may only be a giftee once. This prevents a scenario where one person gets all the gifts.
     *
     * An optional second parameter allows you to force a gifter to giftee pairing.
     */
    fun solve(
        people: Set<Person>,
        gifterGifteeConstraints: Map<Person, Person> = emptyMap()
    ): List<Solution>
}