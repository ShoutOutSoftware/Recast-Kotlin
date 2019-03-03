package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.util.*

/**
 * Created on 08 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class RecastNullifyValueTransformationTests {

    @Test
    fun testEmptyMapTransformation() {
        val map = HashMap<String, Any?>()
        var numberOfCallbacks = 0
        Recast().transformByNullifyingValues(map, callback = RecastCallback { _, _ ->
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 0)
    }

    @Test
    fun testTransformationOfaSingleValueHashMap() {
        val map = hashMapOf<String, Any?>("key" to "value")
        var numberOfCallbacks = 0

        Recast().transformByNullifyingValues(map, callback = RecastCallback { newMap, keyAltered ->
            assertNull(newMap[keyAltered])
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 1)
    }

    @Test
    fun testTransformationOfaComplexHashMap() {
        val map = java.util.HashMap<String, Any?>()
        map["string"] = "value"
        map["int"] = 1
        map["float"] = 1.2.toFloat()
        map["double"] = 1.2
        map["map"] = hashMapOf("subStr" to "x", "subInt" to 1)
        map["boolean"] = false
        map["array"] = arrayOf(1, 2, 3, 4)
        map["multiNestedMap"] = hashMapOf("subMap1" to hashMapOf("subMap2" to hashMapOf("s" to "x", "i" to 1)))
        map["mapWithNull"] = hashMapOf("null" to null)
        var numberOfCallbacks = 0

        Recast().transformByNullifyingValues(map, callback = RecastCallback { newMap, key ->

            ifKey(key, equals = "string", assertNull = newMap["string"], elseNotNull = newMap["string"])
            ifKey(key, equals = "int", assertNull = newMap["int"], elseNotNull = newMap["int"])
            ifKey(key, equals = "float", assertNull = newMap["float"], elseNotNull = newMap["float"])
            ifKey(key, equals = "double", assertNull = newMap["double"], elseNotNull = newMap["double"])
            ifKey(key, equals = "boolean", assertNull = newMap["boolean"], elseNotNull = newMap["boolean"])
            ifKey(key, equals = "array", assertNull = newMap["array"], elseNotNull = newMap["array"])

            if (key == "map") {
                assertNull(newMap["map"])
            } else {
                assertNotNull(newMap["map"])
                val subMap = newMap["map"] as HashMap<*, *>
                ifKey(key, equals = "subStr", assertNull = subMap["subStr"], elseNotNull = subMap["subStr"])
                ifKey(key, equals = "subInt", assertNull = subMap["subInt"], elseNotNull = subMap["subInt"])
            }

            if (key == "multiNestedMap") {
                assertNull(newMap["multiNestedMap"])
            } else {
                assertNotNull(newMap["multiNestedMap"])

                val subMap0 = newMap["multiNestedMap"] as HashMap<*, *>
                if (key == "subMap1") {
                    assertNull(subMap0["subMap1"])
                } else {
                    assertNotNull(subMap0["subMap1"])

                    val subMap1 = subMap0["subMap1"] as HashMap<*, *>
                    if (key == "subMap2") {
                        assertNull(subMap1["subMap2"])
                    } else {
                        assertNotNull(subMap1["subMap2"])

                        val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                        ifKey(key, equals = "s", assertNull = subMap2["s"], elseNotNull = subMap2["s"])
                        ifKey(key, equals = "i", assertNull = subMap2["i"], elseNotNull = subMap2["i"])
                    }

                }
            }

            if (key == "mapWithNull") {
                assertNull(newMap["mapWithNull"])
            } else {
                assertNotNull(newMap["mapWithNull"])
                val subMap = newMap["mapWithNull"] as HashMap<*, *>
                assertNull(subMap["null"])
            }

            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 16)
    }

    private fun ifKey(key: String, equals: String, assertNull: Any?, elseNotNull: Any?) = if (key == equals) {
        Assert.assertNull(assertNull)
    } else {
        Assert.assertNotNull(elseNotNull)
    }

}
