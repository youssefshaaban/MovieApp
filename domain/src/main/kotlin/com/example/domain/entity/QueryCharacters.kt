package com.example.domain.entity

class QueryCharacters(
    val language: String = "en-US",
    val page: Int = 1
) {
    fun toQueryMap(): Map<String, String> {
        return mapOf( "page" to "$page","language" to language)
    }
}