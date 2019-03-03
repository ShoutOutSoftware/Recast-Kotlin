package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
import com.shoutoutsoftware.recast.utils.*
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Created on 04 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class RecastChangeValueTypeTransformationTests {

    @Test
    fun testEmptyMapTransformation() {
        val map = HashMap<String, Any?>()
        var numberOfCallbacks = 0
        Recast().transformByChangingValueTypes(map, callback = RecastCallback { _, _ ->
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 0)
    }

    @Test
    fun testTransformationOfaSingleValueHashMap() {
        val map = hashMapOf<String, Any?>("key" to "value")
        var numberOfCallbacks = 0

        Recast().transformByChangingValueTypes(map, callback = RecastCallback { newMap, keyAltered ->
            assertFalse(newMap[keyAltered] is String)
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

        Recast().transformByChangingValueTypes(map, callback = RecastCallback { newMap, key ->

            ifKey(
                key,
                equals = "string",
                assert = !newMap["string"].isString(),
                elseAssert = newMap["string"].isString()
            )
            ifKey(key, equals = "int", assert = !newMap["int"].isInt(), elseAssert = newMap["int"].isInt())
            ifKey(key, equals = "float", assert = !newMap["float"].isFloat(), elseAssert = newMap["float"].isFloat())
            ifKey(
                key,
                equals = "double",
                assert = !newMap["double"].isDouble(),
                elseAssert = newMap["double"].isDouble()
            )
            ifKey(
                key,
                equals = "boolean",
                assert = !newMap["boolean"].isBoolean(),
                elseAssert = newMap["boolean"].isBoolean()
            )
            ifKey(key, equals = "array", assert = !newMap["array"].isArray(), elseAssert = newMap["array"].isArray())

            if (key == "map") {
                assertFalse(newMap["map"].isHashMap())
            } else {
                assertTrue(newMap["map"].isHashMap())
                val subMap = newMap["map"] as HashMap<*, *>
                ifKey(
                    key,
                    equals = "subStr",
                    assert = !subMap["subStr"].isString(),
                    elseAssert = subMap["subStr"].isString()
                )
                ifKey(key, equals = "subInt", assert = !subMap["subInt"].isInt(), elseAssert = subMap["subInt"].isInt())
            }

            if (key == "multiNestedMap") {
                assertFalse(newMap["multiNestedMap"].isHashMap())
            } else {
                assertTrue(newMap["multiNestedMap"].isHashMap())

                val subMap0 = newMap["multiNestedMap"] as HashMap<*, *>
                if (key == "subMap1") {
                    assertFalse(subMap0["subMap1"].isHashMap())
                } else {
                    assertTrue(subMap0["subMap1"].isHashMap())

                    val subMap1 = subMap0["subMap1"] as HashMap<*, *>
                    if (key == "subMap2") {
                        assertFalse(subMap1["subMap2"].isHashMap())
                    } else {
                        assertTrue(subMap1["subMap2"].isHashMap())

                        val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                        ifKey(
                            key,
                            equals = "s",
                            assert = !subMap2["s"].isString(),
                            elseAssert = subMap2["s"].isString()
                        )
                        ifKey(key, equals = "i", assert = !subMap2["i"].isInt(), elseAssert = subMap2["i"].isInt())
                    }

                }
            }

            if (key == "mapWithNull") {
                assertFalse(newMap["mapWithNull"].isHashMap())
            } else {
                assertTrue(newMap["mapWithNull"].isHashMap())
                val subMap = newMap["mapWithNull"] as HashMap<*, *>
                ifKey(key, equals = "null", assert = !subMap["null"].isNull(), elseAssert = subMap["null"].isNull())
            }

            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 16)
    }

    private fun ifKey(key: String, equals: String, assert: Boolean, elseAssert: Boolean) = if (key == equals) {
        assertTrue(assert)
    } else {
        assertTrue(elseAssert)
    }

}