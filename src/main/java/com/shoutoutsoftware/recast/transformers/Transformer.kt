package com.shoutoutsoftware.recast.transformers

/**
 * Created on 07 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

interface Transformer {
    fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?>
}
