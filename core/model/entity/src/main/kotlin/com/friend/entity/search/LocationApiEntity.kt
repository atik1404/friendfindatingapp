package com.friend.entity.search

data class CountryApiEntity(
    val name: String,
    val value: String,
){
    override fun toString(): String {
        return name
    }
}

data class CityApiEntity(
    val name: String,
    val value: String,
){
    override fun toString(): String {
        return name
    }
}

data class StateApiEntity(
    val name: String,
    val value: String,
){
    override fun toString(): String {
        return name
    }
}