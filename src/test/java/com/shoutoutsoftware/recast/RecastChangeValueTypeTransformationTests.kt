package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
import com.shoutoutsoftware.recast.utils.*
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

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
            assertFalse(newMap[keyAltered].isString())
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 1)
    }

    @Suppress("UNCHECKED_CAST")
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
        map["multiNestedMap"] = hashMapOf("subMap1" to hashMapOf("subMap2" to hashMapOf("s" to "x", "i" to 1)))
        map["mapWithNull"] = hashMapOf("null" to null)
        var numberOfCallbacks = 0

        Recast().transformByChangingValueTypes(map, callback = RecastCallback { alteredMap, key ->
            if (key == "string") assertFalse(alteredMap["string"].isString())
            else assertTrue(alteredMap["string"].isString())

            if (key == "int") assertFalse(alteredMap["int"].isInt())
            else assertTrue(alteredMap["int"].isInt())

            if (key == "float") assertFalse(alteredMap["float"].isFloat())
            else assertTrue(alteredMap["float"].isFloat())

            if (key == "double") assertFalse(alteredMap["double"].isDouble())
            else assertTrue(alteredMap["double"].isDouble())

            if (key == "boolean") assertFalse(alteredMap["boolean"].isBoolean())
            else assertTrue(alteredMap["boolean"].isBoolean())

            if (key == "array") assertFalse(alteredMap["array"].isArray())
            else assertTrue(alteredMap["array"].isArray())

            if (key == "arrayOfMaps") assertFalse(alteredMap["arrayOfMaps"].isArray())
            else assertTrue(alteredMap["arrayOfMaps"].isArray())


            if (key == "arraySubKeyOne") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapOne = arrayOfMaps[0]["arraySubKeyOne"]
                assertFalse(arrayMapOne.isString())
            } else if (key != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapOne = arrayOfMaps[0]["arraySubKeyOne"]
                assertTrue(arrayMapOne.isString())
            }


            if (key == "arraySubKeyTwo") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapTwo = arrayOfMaps[1]["arraySubKeyTwo"]
                assertFalse(arrayMapTwo.isString())
            } else if (key != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapTwo = arrayOfMaps[1]["arraySubKeyTwo"]
                assertTrue(arrayMapTwo.isString())
            }


            if (key == "arraySubKeyThree") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"]
                assertFalse(arrayMapThree.isHashMap())
            } else if (key != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"]
                assertTrue(arrayMapThree.isHashMap())
            }


            if (key == "arrayInnerKeyOne") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"] as HashMap<*, *>
                val arrayInnerMapValue = arrayMapThree["arrayInnerKeyOne"];
                assertFalse(arrayInnerMapValue.isString())
            } else if (key != "arrayOfMaps" && key != "arraySubKeyThree") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"] as HashMap<*, *>
                val arrayInnerMapValue = arrayMapThree["arrayInnerKeyOne"];
                assertTrue(arrayInnerMapValue.isString())
            }

            if (key == "listOfMaps") assertFalse(alteredMap["listOfMaps"].isList())
            else assertTrue(alteredMap["listOfMaps"].isList())


            if (key == "listSubKeyOne") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapOne = listOfMaps[0]["listSubKeyOne"]
                assertFalse(listMapOne.isString())
            } else if (key != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapOne = listOfMaps[0]["listSubKeyOne"]
                assertTrue(listMapOne.isString())
            }


            if (key == "listSubKeyTwo") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapTwo = listOfMaps[1]["listSubKeyTwo"]
                assertFalse(listMapTwo.isString())
            } else if (key != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapTwo = listOfMaps[1]["listSubKeyTwo"]
                assertTrue(listMapTwo.isString())
            }


            if (key == "listSubKeyThree") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"]
                assertFalse(listMapThree.isHashMap())
            } else if (key != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"]
                assertTrue(listMapThree.isHashMap())
            }


            if (key == "listInnerKeyOne") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"] as HashMap<*, *>
                val listInnerMapValue = listMapThree["listInnerKeyOne"];
                assertFalse(listInnerMapValue.isString())
            } else if (key != "listOfMaps" && key != "listSubKeyThree") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"] as HashMap<*, *>
                val listInnerMapValue = listMapThree["listInnerKeyOne"];
                assertTrue(listInnerMapValue.isString())
            }

            if (key == "map") {
                assertFalse(alteredMap["map"].isHashMap())
            } else {
                assertTrue(alteredMap["map"].isHashMap())
            }

            if (key == "subStr") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertFalse(subMap["subStr"].isString())
            } else if (key != "map") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertTrue(subMap["subStr"].isString())
            }

            if (key == "subInt") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertFalse(subMap["subInt"].isInt())
            } else if (key != "map") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertTrue(subMap["subInt"].isInt())
            }


            if (key == "multiNestedMap") {
                assertFalse(alteredMap["multiNestedMap"].isHashMap())
            } else {
                assertTrue(alteredMap["multiNestedMap"].isHashMap())
            }

            if (key == "subMap1") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                assertFalse(subMap["subMap1"].isHashMap())
            } else if (key != "multiNestedMap") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                assertTrue(subMap["subMap1"].isHashMap())
            }

            if (key == "subMap2") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                assertFalse(subMap1["subMap2"].isHashMap())
            } else if (key != "multiNestedMap" && key != "subMap1") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                assertTrue(subMap1["subMap2"].isHashMap())
            }

            if (key == "s") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertFalse(subMap2["s"].isString())
            } else if (key != "multiNestedMap" && key != "subMap1" && key != "subMap2") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertTrue(subMap2["s"].isString())
            }

            if (key == "i") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertFalse(subMap2["i"].isInt())
            } else if (key != "multiNestedMap" && key != "subMap1" && key != "subMap2") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertTrue(subMap2["i"].isInt())
            }

            if (key == "mapWithNull") {
                assertFalse(alteredMap["mapWithNull"].isHashMap())
            } else {
                assertTrue(alteredMap["mapWithNull"].isHashMap())
            }

            if (key == "null") {
                val nullMap = alteredMap["mapWithNull"] as HashMap<*, *>
                assertFalse(nullMap["null"].isNull())
            } else if (key != "mapWithNull") {
                val nullMap = alteredMap["mapWithNull"] as HashMap<*, *>
                assertTrue(nullMap["null"].isNull())
            }

            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 26)
    }

}