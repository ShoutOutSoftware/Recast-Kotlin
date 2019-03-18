package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
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
        Recast().transformByRemovingKeys(map, callback = RecastCallback { _, _ ->
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 0)
    }

    @Test
    fun testTransformationOfaSingleValueHashMap() {
        val map = hashMapOf<String, Any?>("key" to "value")
        var numberOfCallbacks = 0

        Recast().transformByRemovingKeys(map, callback = RecastCallback { newMap, alteredKey ->
            assertNull(newMap["key"])
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 1)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun testTransformationOfComplexHashMap() {
        val map = java.util.HashMap<String, Any?>()
        map["string"] = "value"
        map["multiNestedMap"] = hashMapOf("subMap1" to hashMapOf("subMap2" to hashMapOf("multiNestedString" to "x", "multiNestedInt" to 1)))
        map["arrayOfMaps"] = arrayOf(
            hashMapOf("arraySubKeyOne" to "arraySubValueOne"),
            hashMapOf("arraySubKeyTwo" to "arraySubValueTwo"),
            hashMapOf("arraySubKeyThree" to hashMapOf("arrayInnerKeyOne" to "arrayInnerValueOne"))
        )
        map["listOfMaps"] = listOf(
            hashMapOf("listSubKeyOne" to "listSubValueOne"),
            hashMapOf("listSubKeyTwo" to "listSubValueTwo"),
            hashMapOf("listSubKeyThree" to hashMapOf("listInnerKeyOne" to "listInnerValueOne"))
        )

        var numberOfCallbacks = 0

        Recast().transformByRemovingKeys(map, callback = RecastCallback { alteredMap, keyAltered ->
            if (keyAltered == "string") assertFalse(alteredMap.keys.contains("string"))
            else assertTrue(alteredMap.keys.contains("string"))

            if (keyAltered == "multiNestedMap") assertFalse(alteredMap.keys.contains("multiNestedMap"))
            else assertTrue(alteredMap.keys.contains("multiNestedMap"))

            if (keyAltered == "subMap1") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                assertFalse(multiNestedMap.keys.contains("subMap1"))
            } else if (keyAltered != "multiNestedMap") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                assertTrue(multiNestedMap.keys.contains("subMap1"))
            }

            if (keyAltered == "subMap2") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = multiNestedMap["subMap1"] as HashMap<*, *>
                assertFalse(subMap1.keys.contains("subMap2"))
            } else if (keyAltered != "multiNestedMap" && keyAltered != "subMap1") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = multiNestedMap["subMap1"] as HashMap<*, *>
                assertTrue(subMap1.keys.contains("subMap2"))
            }

            if (keyAltered == "multiNestedString") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = multiNestedMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertFalse(subMap2.keys.contains("multiNestedString"))
            } else if (keyAltered != "multiNestedMap" && keyAltered != "subMap1" && keyAltered != "subMap2") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = multiNestedMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertTrue(subMap2.keys.contains("multiNestedString"))
            }

            if (keyAltered == "multiNestedInt") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = multiNestedMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertFalse(subMap2.keys.contains("multiNestedInt"))
            } else if (keyAltered != "multiNestedMap" && keyAltered != "subMap1" && keyAltered != "subMap2") {
                val multiNestedMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = multiNestedMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertTrue(subMap2.keys.contains("multiNestedInt"))
            }

            if (keyAltered == "arrayOfMaps") assertFalse(alteredMap.containsKey("arrayOfMaps"))
            else assertTrue(alteredMap.containsKey("arrayOfMaps"))


            if (keyAltered == "arraySubKeyOne") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapOne = arrayOfMaps[0]
                assertFalse(arrayMapOne.containsKey("arraySubKeyOne"))
            } else if (keyAltered != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapOne = arrayOfMaps[0]
                assertTrue(arrayMapOne.containsKey("arraySubKeyOne"))
            }


            if (keyAltered == "arraySubKeyTwo") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapTwo = arrayOfMaps[1]
                assertFalse(arrayMapTwo.containsKey("arraySubKeyTwo"))
            } else if (keyAltered != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapTwo = arrayOfMaps[1]
                assertTrue(arrayMapTwo.containsKey("arraySubKeyTwo"))
            }


            if (keyAltered == "arraySubKeyThree") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]
                assertFalse(arrayMapThree.containsKey("arraySubKeyThree"))
            } else if (keyAltered != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]
                assertTrue(arrayMapThree.containsKey("arraySubKeyThree"))
            }


            if (keyAltered == "arrayInnerKeyOne") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"] as java.util.HashMap<*, *>
                assertFalse(arrayMapThree.containsKey("arrayInnerKeyOne"))
            } else if (keyAltered != "arrayOfMaps" && keyAltered != "arraySubKeyThree") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<java.util.HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"] as java.util.HashMap<*, *>
                assertTrue(arrayMapThree.containsKey("arrayInnerKeyOne"))
            }


            if (keyAltered == "listOfMaps") assertFalse(alteredMap.containsKey("listOfMaps"))
            else assertTrue(alteredMap.containsKey("listOfMaps"))


            if (keyAltered == "listSubKeyOne") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapOne = listOfMaps[0]
                assertFalse(listMapOne.containsKey("listSubKeyOne"))
            } else if (keyAltered != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapOne = listOfMaps[0]
                assertTrue(listMapOne.containsKey("listSubKeyOne"))
            }


            if (keyAltered == "listSubKeyTwo") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapTwo = listOfMaps[1]
                assertFalse(listMapTwo.containsKey("listSubKeyTwo"))
            } else if (keyAltered != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapTwo = listOfMaps[1]
                assertTrue(listMapTwo.containsKey("listSubKeyTwo"))
            }


            if (keyAltered == "listSubKeyThree") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapThree = listOfMaps[2]
                assertFalse(listMapThree.containsKey("listSubKeyThree"))
            } else if (keyAltered != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapThree = listOfMaps[2]
                assertTrue(listMapThree.containsKey("listSubKeyThree"))
            }


            if (keyAltered == "listInnerKeyOne") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"] as java.util.HashMap<*, *>
                assertFalse(listMapThree.containsKey("listInnerKeyOne"))
            } else if (keyAltered != "listOfMaps" && keyAltered != "listSubKeyThree") {
                val listOfMaps = alteredMap["listOfMaps"] as List<java.util.HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"] as java.util.HashMap<*, *>
                assertTrue(listMapThree.containsKey("listInnerKeyOne"))
            }

            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 16)
    }

}
