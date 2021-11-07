package com.caseykulm.santasolver

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val name: String,
    val householdName: String,
) {
    override fun toString(): String = "$name of House $householdName"
}