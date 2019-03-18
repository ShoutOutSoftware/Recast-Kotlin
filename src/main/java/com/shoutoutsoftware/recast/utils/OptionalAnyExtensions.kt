package com.shoutoutsoftware.recast.utils

/**
 * Created on 07 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

fun Any?.isString(): Boolean = this is String
fun Any?.isInt(): Boolean = this is Int
fun Any?.isFloat(): Boolean = this is Float
fun Any?.isDouble(): Boolean = this is Double
fun Any?.isBoolean(): Boolean = this is Boolean
fun Any?.isHashMap(): Boolean = this is HashMap<*, *>
fun Any?.isArray(): Boolean = this is Array<*>
fun Any?.isList(): Boolean = this is List<*>
fun Any?.isNull(): Boolean = this == null