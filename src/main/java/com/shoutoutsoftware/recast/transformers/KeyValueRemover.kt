package com.shoutoutsoftware.recast.transformers

/**
 * Created on 07 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class KeyValueRemover : Transformer {
    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        map.remove(keyToAlter)
        return map
    }
}
