package com.shoutoutsoftware.recast.transformers

/**
 * Created on 08 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class ValueNullifyer : Transformer {

    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        map[keyToAlter] = null
        return map
    }
}
