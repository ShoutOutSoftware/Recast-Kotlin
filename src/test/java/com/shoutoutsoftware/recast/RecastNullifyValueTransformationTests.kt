package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
import org.junit.Assert.*
import org.junit.Test
import java.util.*

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

        Recast().transformByNullifyingValues(map, callback = RecastCallback { alteredMap, key ->
            if (key == "string") assertNull(alteredMap["string"])
            else assertNotNull(alteredMap["string"])

            if (key == "int") assertNull(alteredMap["int"])
            else assertNotNull(alteredMap["int"])

            if (key == "float") assertNull(alteredMap["float"])
            else assertNotNull(alteredMap["float"])

            if (key == "double") assertNull(alteredMap["double"])
            else assertNotNull(alteredMap["double"])

            if (key == "boolean") assertNull(alteredMap["boolean"])
            else assertNotNull(alteredMap["boolean"])

            if (key == "array") assertNull(alteredMap["array"])
            else assertNotNull(alteredMap["array"])

            if (key == "arrayOfMaps") assertNull(alteredMap["arrayOfMaps"])
            else assertNotNull(alteredMap["arrayOfMaps"])


            if (key == "arraySubKeyOne") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapOne = arrayOfMaps[0]["arraySubKeyOne"]
                assertNull(arrayMapOne)
            } else if (key != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapOne = arrayOfMaps[0]["arraySubKeyOne"]
                assertNotNull(arrayMapOne)
            }


            if (key == "arraySubKeyTwo") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapTwo = arrayOfMaps[1]["arraySubKeyTwo"]
                assertNull(arrayMapTwo)
            } else if (key != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapTwo = arrayOfMaps[1]["arraySubKeyTwo"]
                assertNotNull(arrayMapTwo)
            }


            if (key == "arraySubKeyThree") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"]
                assertNull(arrayMapThree)
            } else if (key != "arrayOfMaps") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"]
                assertNotNull(arrayMapThree)
            }


            if (key == "arrayInnerKeyOne") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"] as HashMap<*, *>
                val arrayInnerMapValue = arrayMapThree["arrayInnerKeyOne"];
                assertNull(arrayInnerMapValue)
            } else if (key != "arrayOfMaps" && key != "arraySubKeyThree") {
                val arrayOfMaps = alteredMap["arrayOfMaps"] as Array<HashMap<*, *>>
                val arrayMapThree = arrayOfMaps[2]["arraySubKeyThree"] as HashMap<*, *>
                val arrayInnerMapValue = arrayMapThree["arrayInnerKeyOne"];
                assertNotNull(arrayInnerMapValue)
            }


            if (key == "listOfMaps") assertNull(alteredMap["listOfMaps"])
            else assertNotNull(alteredMap["listOfMaps"])


            if (key == "listSubKeyOne") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapOne = listOfMaps[0]["listSubKeyOne"]
                assertNull(listMapOne)
            } else if (key != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapOne = listOfMaps[0]["listSubKeyOne"]
                assertNotNull(listMapOne)
            }


            if (key == "listSubKeyTwo") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapTwo = listOfMaps[1]["listSubKeyTwo"]
                assertNull(listMapTwo)
            } else if (key != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapTwo = listOfMaps[1]["listSubKeyTwo"]
                assertNotNull(listMapTwo)
            }


            if (key == "listSubKeyThree") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"]
                assertNull(listMapThree)
            } else if (key != "listOfMaps") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"]
                assertNotNull(listMapThree)
            }


            if (key == "listInnerKeyOne") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"] as HashMap<*, *>
                val listInnerMapValue = listMapThree["listInnerKeyOne"];
                assertNull(listInnerMapValue)
            } else if (key != "listOfMaps" && key != "listSubKeyThree") {
                val listOfMaps = alteredMap["listOfMaps"] as List<HashMap<*, *>>
                val listMapThree = listOfMaps[2]["listSubKeyThree"] as HashMap<*, *>
                val listInnerMapValue = listMapThree["listInnerKeyOne"];
                assertNotNull(listInnerMapValue)
            }

            if (key == "map") {
                assertNull(alteredMap["map"])
            } else {
                assertNotNull(alteredMap["map"])
            }

            if (key == "subStr") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertNull(subMap["subStr"])
            } else if (key != "map") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertNotNull(subMap["subStr"])
            }

            if (key == "subInt") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertNull(subMap["subInt"])
            } else if (key != "map") {
                val subMap = alteredMap["map"] as HashMap<*, *>
                assertNotNull(subMap["subInt"])
            }


            if (key == "multiNestedMap") {
                assertNull(alteredMap["multiNestedMap"])
            } else {
                assertNotNull(alteredMap["multiNestedMap"])
            }

            if (key == "subMap1") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                assertNull(subMap["subMap1"])
            } else if (key != "multiNestedMap") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                assertNotNull(subMap["subMap1"])
            }

            if (key == "subMap2") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                assertNull(subMap1["subMap2"])
            } else if (key != "multiNestedMap" && key != "subMap1") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                assertNotNull(subMap1["subMap2"])
            }

            if (key == "s") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertNull(subMap2["s"])
            } else if (key != "multiNestedMap" && key != "subMap1" && key != "subMap2") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertNotNull(subMap2["s"])
            }

            if (key == "i") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertNull(subMap2["i"])
            } else if (key != "multiNestedMap" && key != "subMap1" && key != "subMap2") {
                val subMap = alteredMap["multiNestedMap"] as HashMap<*, *>
                val subMap1 = subMap["subMap1"] as HashMap<*, *>
                val subMap2 = subMap1["subMap2"] as HashMap<*, *>
                assertNotNull(subMap2["i"])
            }

            if (key == "mapWithNull") {
                assertNull(alteredMap["mapWithNull"])
            } else {
                assertNotNull(alteredMap["mapWithNull"])
            }

            if (key == "null") {
                val nullMap = alteredMap["mapWithNull"] as HashMap<*, *>
                assertNull(nullMap["null"])
            } else if (key != "mapWithNull") {
                val nullMap = alteredMap["mapWithNull"] as HashMap<*, *>
                assertNull(nullMap["null"])
            }

            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 26)
    }

}
