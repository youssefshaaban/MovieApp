package com.example.movieapp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class CustomNavType<T : Any>(
    private val serializer: KSerializer<T>,
    override val isNullableAllowed: Boolean = false
) : NavType<T>(isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let {
            //   val decodedValue = Uri.decode(it)
            Json.decodeFromString(serializer, it)
        }

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, serializeAsValue(value))

    override fun parseValue(value: String): T {
        // val decodedValue = Uri.decode(value)
        return Json.decodeFromString(serializer, value)
    }

    override fun serializeAsValue(value: T): String {
        val jsonString = Json.encodeToString(serializer, value)
        return jsonString
    }

    override val name: String = serializer.descriptor.serialName
}


inline fun <reified T : Any> navigationCustomArgument(isNullable: Boolean = false): Pair<KType, CustomNavType<T>> {
    val serializer: KSerializer<T> = serializer()
    return typeOf<T>() to CustomNavType(serializer, isNullable)
}