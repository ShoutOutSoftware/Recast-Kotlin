package com.shoutoutsoftware.recast.transformers

import com.shoutoutsoftware.recast.exceptions.InvalidValueTypeException

/**
 * Created on 07 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class ValueTypeChanger : Transformer {
    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        val value = map[keyToAlter]
        map[keyToAlter] = changeValueType(value, keyToAlter
        )
        return map
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
            else -> throw InvalidValueTypeException("The key \"$key\" has an invalid value type " + value.javaClass.name)
        }
    }
}