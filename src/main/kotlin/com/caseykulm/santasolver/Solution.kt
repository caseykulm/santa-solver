package com.caseykulm.santasolver

data class Solution(
    val gifterToGifteeMap: Map<Person, Person>,
)

fun Solution.gifteeFrom(gifter: Person) = gifterToGifteeMap[gifter]

fun Solution.prettyGifteeFrom(gifter: Person): String = String.format(
    "%6s is getting a gift for %-6s",
    gifter.name,
    gifterToGifteeMap[gifter]!!.name
)

val Solution.prettyAllGifterGifteeAnswers get() = gifterToGifteeMap.keys
    .map { prettyGifteeFrom(it) }
    .toList()
    .sorted()
    .joinToString(separator = "\n")