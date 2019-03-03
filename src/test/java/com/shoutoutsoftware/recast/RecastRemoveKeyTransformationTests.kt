package com.shoutoutsoftware.recast

import org.junit.Assert.*
import org.junit.Test

/**
 * Created on 24 September 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class RecastRemoveKeyTransformationTests {

    @Test
    fun testEmptyMapTransformation() {
        val map = HashMap<String, Any?>()
        var numberOfCallbacks = 0
        Recast().transformByRemovingKeys(map, callback = { _, _ ->
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 0)
    }

    @Test
    fun testTransformationOfaSingleValueHashMap() {
        val map = hashMapOf<String, Any?>("key" to "value")
        var numberOfCallbacks = 0

        Recast().transformByRemovingKeys(map, callback = { newMap, alteredKey ->
            assertNull(newMap["key"])
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 1)
    }

    @Test
    fun testTransformationOfComplexHashMap() {
        val map = java.util.HashMap<String, Any?>()
        map["string"] = "value"
        map["multiNestedMap"] =
            hashMapOf("subMap1" to hashMapOf("subMap2" to hashMapOf("multiNestedString" to "x", "multiNestedInt" to 1)))

        var numberOfCallbacks = 0

        Recast().transformByRemovingKeys(map, callback = { newMap, alteredKey ->
            when (alteredKey) {
                "string" -> {
                    assertFalse(newMap.keys.contains("string"))
                    assertTrue(newMap.keys.contains("multiNestedMap"))
                    assertTrue((newMap["multiNestedMap"] as HashMap<*, *>).keys.contains("subMap1"))
                    assertTrue(((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>).keys.contains("subMap2"))
                    assertTrue(
                        (((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>)["subMap2"] as HashMap<*, *>).containsKey(
                            "multiNestedString"
                        )
                    )
                    assertTrue(
                        (((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>)["subMap2"] as HashMap<*, *>).containsKey(
                            "multiNestedInt"
                        )
                    )
                }
                "multiNestedMap" -> {
                    assertTrue(newMap.keys.contains("string"))
                    assertFalse(newMap.keys.contains("multiNestedMap"))
                }
                "subMap1" -> {
                    assertTrue(newMap.keys.contains("string"))
                    assertTrue(newMap.keys.contains("multiNestedMap"))
                    assertFalse((newMap["multiNestedMap"] as HashMap<*, *>).keys.contains("subMap1"))
                }
                "subMap2" -> {
                    assertTrue(newMap.keys.contains("string"))
                    assertTrue(newMap.keys.contains("multiNestedMap"))
                    assertTrue((newMap["multiNestedMap"] as HashMap<*, *>).keys.contains("subMap1"))
                    assertFalse(
                        ((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>).keys.contains(
                            "subMap2"
                        )
                    )
                }
                "multiNestedString" -> {
                    assertTrue(newMap.keys.contains("string"))
                    assertTrue(newMap.keys.contains("multiNestedMap"))
                    assertTrue((newMap["multiNestedMap"] as HashMap<*, *>).keys.contains("subMap1"))
                    assertTrue(((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>).keys.contains("subMap2"))
                    assertFalse(
                        (((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>)["subMap2"] as HashMap<*, *>).containsKey(
                            "multiNestedString"
                        )
                    )
                    assertTrue(
                        (((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>)["subMap2"] as HashMap<*, *>).containsKey(
                            "multiNestedInt"
                        )
                    )
                }
                "multiNestedInt" -> {
                    assertTrue(newMap.keys.contains("string"))
                    assertTrue(newMap.keys.contains("multiNestedMap"))
                    assertTrue((newMap["multiNestedMap"] as HashMap<*, *>).keys.contains("subMap1"))
                    assertTrue(((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>).keys.contains("subMap2"))
                    assertTrue(
                        (((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>)["subMap2"] as HashMap<*, *>).containsKey(
                            "multiNestedString"
                        )
                    )
                    assertFalse(
                        (((newMap["multiNestedMap"] as HashMap<*, *>)["subMap1"] as HashMap<*, *>)["subMap2"] as HashMap<*, *>).containsKey(
                            "multiNestedInt"
                        )
                    )
                }
            }
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 6)
    }

}
