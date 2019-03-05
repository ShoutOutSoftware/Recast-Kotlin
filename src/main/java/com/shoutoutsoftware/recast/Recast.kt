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

    //MARK: Functions to transform all values

    fun transformByRemovingKeys(map: HashMap<String, Any?>, callback: RecastCallback) {
        transform(map, callback, KeyValueRemover())
    }

    fun transformByChangingValueTypes(map: HashMap<String, Any?>, callback: RecastCallback) {
        transform(map, callback, ValueTypeChanger())
    }

    fun transformByNullifyingValues(map: HashMap<String, Any?>, callback: RecastCallback) {
        transform(map, callback, ValueNullifyer())
    }

    private fun transform(map: HashMap<String, Any?>, callback: RecastCallback, transformer: Transformer) {
        if (map.isEmpty()) return

        for (key in map.keys) {
            transformValue(forKey = key, inMap = map, callback = callback, transformer = transformer)
        }
    }

    private fun transformValue(forKey: String, inMap: HashMap<String, Any?>, callback: RecastCallback, transformer: Transformer) {
        val newMap = HashMap(inMap)
        transformer.transform(newMap, forKey)
        callback.accept(newMap, forKey)

        val value = inMap[forKey]
        if (isAStringKeyHashMap(value)) {
            @Suppress("UNCHECKED_CAST")
            transform(value as HashMap<String, Any?>, callback = RecastCallback { nmp, rmk ->
                newMap[forKey] = nmp
                callback.accept(newMap, rmk)
            }, transformer = transformer)
        }
    }

    //MARK: Functions to transform values for the keys provided

    fun transformByRemovingKeys(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback) {
        transform(map, keysToAlter, callback, KeyValueRemover())
    }

    fun transformByChangingValueTypes(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback) {
        transform(map, keysToAlter, callback, ValueTypeChanger())
    }

    fun transformByNullifyingValues(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback) {
        transform(map, keysToAlter, callback, ValueNullifyer())
    }

    private fun transform(map: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback, transformer: Transformer) {
        if (map.isEmpty()) return

        for (key in map.keys) {
            transformValue(forKey = key, inMap = map, keysToAlter = keysToAlter, callback = callback, transformer = transformer)
        }
    }

    private fun transformValue(forKey: String, inMap: HashMap<String, Any?>, keysToAlter: List<String>, callback: RecastCallback, transformer: Transformer) {
        val newMap = HashMap(inMap)
        if (keysToAlter.contains(forKey)) {
            transformer.transform(newMap, forKey)
            callback.accept(newMap, forKey)
        }

        val value = inMap[forKey]
        if (isAStringKeyHashMap(value)) {
            @Suppress("UNCHECKED_CAST")
            transform(value as HashMap<String, Any?>, keysToAlter, callback = RecastCallback { nmp, rmk ->
                newMap[forKey] = nmp
                callback.accept(newMap, rmk)
            }, transformer = transformer)
        }
    }

    //MARK: Util function to check of a value is a string key Hash Map

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


}