package com.shoutoutsoftware.recast

import com.shoutoutsoftware.recast.callback.RecastCallback
import com.shoutoutsoftware.recast.exceptions.NonStringKeyException
import com.shoutoutsoftware.recast.transformers.KeyValueRemover
import com.shoutoutsoftware.recast.transformers.Transformer
import com.shoutoutsoftware.recast.transformers.ValueNullifyer
import com.shoutoutsoftware.recast.transformers.ValueTypeChanger

/**
 * Created on 24 September 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class Recast {

    private lateinit var transformer: Transformer
    private var keysToAlter: List<String> = listOf()


    //MARK: Functions to transform all values

    fun transformByRemovingKeys(map: HashMap<String, Any?>, callback: RecastCallback) {
        transformer = KeyValueRemover()
        transform(map, callback)
    }

    fun transformByChangingValueTypes(map: HashMap<String, Any?>, callback: RecastCallback) {
        transformer = ValueTypeChanger()
        transform(map, callback)
    }

    fun transformByNullifyingValues(map: HashMap<String, Any?>, callback: RecastCallback) {
        transformer = ValueNullifyer()
        transform(map, callback)
    }

    //MARK: Functions to transform values for the keys provided

    fun transformByRemovingKeys(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback) {
        transformer = KeyValueRemover()
        this.keysToAlter = keysToAlter
        transform(map, callback)
    }

    fun transformByChangingValueTypes(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback) {
        transformer = ValueTypeChanger()
        this.keysToAlter = keysToAlter
        transform(map, callback)
    }

    fun transformByNullifyingValues(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback) {
        transformer = ValueNullifyer()
        this.keysToAlter = keysToAlter
        transform(map, callback)
    }

    private fun transform(map: HashMap<String, Any?>, callback: RecastCallback) {
        if (map.isEmpty()) return

        for (key in map.keys) {
            transformValue(forKey = key, inMap = map, callback = callback)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun transformValue(forKey: String, inMap: HashMap<String, Any?>, callback: RecastCallback) {
        val mapToAlter = HashMap(inMap)
        if (shouldTransformKey(forKey, keysToAlter)) {
            val alteredMap = transformer.transform(mapToAlter, forKey)
            callback.accept(alteredMap, forKey)
        }

        //Checking if the value is a complex type - HashMap, Array, or List
        val value = mapToAlter[forKey]

        if (isAStringKeyHashMap(value)) {
            transformNestedMap(value as HashMap<String, Any?>, forKey, mapToAlter, callback)
        } else if (isAStringKeyHashMapArray(value)) {
            transformHashMapArray(value as Array<HashMap<*, *>>, forKey, mapToAlter, callback)
        }
    }

    private fun shouldTransformKey(key: String, keysToAlter: List<String>): Boolean {
        if (keysToAlter.isEmpty()) {
            return true
        } else {
            return keysToAlter.contains(key)
        }
    }

    private fun transformNestedMap(nestedMap: HashMap<String, Any?>, nestedMapKey: String, mapToAlter: HashMap<String, Any?>, callback: RecastCallback) {
        transform(nestedMap, callback = RecastCallback { alteredMap, keyAltered ->
            mapToAlter[nestedMapKey] = alteredMap
            callback.accept(mapToAlter, keyAltered)
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun transformHashMapArray(arrayOfHashMaps: Array<HashMap<*, *>>, arrayKey: String, mapToAlter: HashMap<String, Any?>, callback: RecastCallback) {
        for (i in 0 until arrayOfHashMaps.size) {
            val map = arrayOfHashMaps[i]
            transform(map as HashMap<String, Any?>, callback = RecastCallback { alteredMap, keyAltered ->
                val newArray = arrayOfHashMaps.copyOf()
                newArray[i] = alteredMap
                mapToAlter[arrayKey] = newArray
                callback.accept(mapToAlter, keyAltered)
            })
        }
    }

    //MARK: Util function to check if a value is a string-key HashMap

    @Throws
    private fun isAStringKeyHashMap(value: Any?): Boolean {
        if (value is HashMap<*, *>) {
            val nestedMap: HashMap<*, *> = value
            val nestedMapKeys = nestedMap.keys.filterIsInstance<String>()

            return if (nestedMapKeys.size != nestedMap.keys.size) {
                val nonStringKeys = nestedMap.keys.minus(nestedMapKeys)
                val errorMessage = "Hash map: $value.\n" +
                        "Looks like this hash map has non-string keys. The list of non-string keys is $nonStringKeys"
                throw NonStringKeyException(errorMessage)
            } else {
                true
            }
        }
        return false
    }

    //MARK: Util function to check of a value is a string-key HashMap Array

    @Throws
    private fun isAStringKeyHashMapArray(value: Any?): Boolean {
        if (value is Array<*>) {
            val array: Array<*> = value

            if (array.isEmpty()) return false

            for (item in array) {
                if (item !is HashMap<*, *> || !isAStringKeyHashMap(item)) {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

}