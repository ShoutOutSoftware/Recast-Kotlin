package com.shoutoutsoftware.recast.transformers

import com.shoutoutsoftware.recast.exceptions.InvalidValueTypeException

class ValueTypeChanger : Transformer {

    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        val alteredMap = HashMap(map)
        val value = alteredMap[keyToAlter]
        alteredMap[keyToAlter] = changeValueType(
            value, keyToAlter
        )
        return alteredMap
    }

    private fun changeValueType(value: Any?, key: String): Any = if (value == null) {
        "some other random type"
    } else {
        when (value) {
            is String -> 1
            is Int -> "some other random type"
            is Float -> "some other random type"
            is Double -> "some other random type"
            is HashMap<*, *> -> "some other random type"
            is Boolean -> "some other random type"
            is Array<*> -> "some other random type"
            is List<*> -> "some other random type"
            else -> throw InvalidValueTypeException("The key \"$key\" has an invalid value type " + value.javaClass.name)
        }
    }
}