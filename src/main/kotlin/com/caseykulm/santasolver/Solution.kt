package com.caseykulm.santasolver

data class Solution(
    val gifterToGifteeMap: Map<Person, Person>,
)

fun Solution.gifteeFrom(gifter: Person) = gifterToGifteeMap[gifter]

fun Solution.prettyGifteeFrom(gifter: Person): String = "Gifter ${gifter.name} is assigned to Giftee ${gifterToGifteeMap[gifter]!!.name}"

val Solution.prettyAllGifterGifteeAnswers get() = gifterToGifteeMap.keys
    .map { prettyGifteeFrom(it) }
    .toList()
    .sorted()
    .joinToString(separator = "\n")