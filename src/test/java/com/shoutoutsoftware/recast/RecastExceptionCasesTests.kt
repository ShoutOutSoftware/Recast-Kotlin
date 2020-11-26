package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
import com.shoutoutsoftware.recast.exceptions.InvalidValueTypeException
import com.shoutoutsoftware.recast.exceptions.NonStringKeyException
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap

class RecastExceptionCasesTests {

    @Test
    fun testThrowsExceptionForNonStringKey() {
        val map = HashMap<String, Any?>()
        map["key"] = hashMapOf(1 to "val", true to "val")
        try {
            Recast().transformByChangingValueTypes(map, callback = RecastCallback { _, _ -> })
            fail("did not throw an exception")
        } catch (exception: NonStringKeyException) {
            val expectedErrorMessage = "Hash map: " + map["key"] + ".\n" +
                    "Looks like this hash map has non-string keys. The list of non-string keys is [1, true]"
            assertEquals(expectedErrorMessage, exception.message)
        }
    }

    @Test
    fun testThrowsExceptionForNonStringKeyInANestedMap() {
        val map = HashMap<String, Any?>()
        val nonStringKeyOne = 1.2
        val nonStringKeyTwo = Date()

        map["key"] = hashMapOf(nonStringKeyOne to hashMapOf(true to "val"), "stringKey" to "val", nonStringKeyTwo to 54)
        try {
            Recast().transformByChangingValueTypes(map, callback = RecastCallback { _, _ -> })
            fail("did not throw an exception")
        } catch (exception: NonStringKeyException) {
            val subMap = map["key"] as HashMap<*, *>
            val expectedErrorMessage = "Hash map: " + subMap + ".\n" +
                    "Looks like this hash map has non-string keys. The list of non-string keys is [$nonStringKeyOne, $nonStringKeyTwo]"
            assertEquals(expectedErrorMessage, exception.message)
        }
    }

    @Test
    fun testThrowsExceptionForNonStringKeyInAnArray() {
        val map = HashMap<String, Any?>()
        val nonStringKeyOne = 1.2

        map["key"] = arrayOf(hashMapOf(nonStringKeyOne to "val"))

        try {
            Recast().transformByChangingValueTypes(map, callback = RecastCallback { response, _ -> })
            fail("did not throw an exception")
        } catch (exception: NonStringKeyException) {
            val mapArray = map["key"] as Array<*>
            val subMap = mapArray[0]
            val expectedErrorMessage = "Hash map: " + subMap + ".\n" +
                    "Looks like this hash map has non-string keys. The list of non-string keys is [$nonStringKeyOne]"
            assertEquals(expectedErrorMessage, exception.message)
        }
    }

    @Test
    fun testThrowsExceptionForInvalidValueType() {
        val map = HashMap<String, Any?>()
        val key = "date"
        map[key] = Date()
        try {
            Recast().transformByChangingValueTypes(map, callback = RecastCallback { _, _ -> })
            fail("did not throw an exception")
        } catch (exception: InvalidValueTypeException) {
            val expectedErrorMessage = "The key \"$key\" has an invalid value type " + map[key]!!.javaClass.name
            assertEquals(expectedErrorMessage, exception.message)
        }

    }

}
